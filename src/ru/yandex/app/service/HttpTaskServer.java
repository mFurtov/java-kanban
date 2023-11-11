package ru.yandex.app.service;


import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.app.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;


import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private Gson gson;


    public TaskManager getTaskManager() {
        return taskManager;
    }

    private TaskManager taskManager;


    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            System.out.println("При создании сервера в HttpTaskServer произошла ошибка");
            e.printStackTrace();
        }
        server.createContext("/tasks", this::handle);
        gson = Managers.getDefaultGson();


    }

    public HttpTaskServer() {
        this(Managers.getDefault());
    }

    private void handle(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    getTask(httpExchange, pathParts);
                    break;
                }
                case "POST": {
                    postTask(httpExchange, pathParts);
                    break;
                }
                case "DELETE": {
                    deleteTask(httpExchange, pathParts);
                    break;
                }
                default: {
                    System.out.println("Ждем корректный метод");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private void getTask(HttpExchange httpExchange, String[] pathPart) {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getQuery();
        try {
            if (pathPart.length == 2 && "tasks".equalsIgnoreCase(pathPart[1])
                    && query == null) {
                String response = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(httpExchange, response);
                System.out.println("Все задачи выведены");
                return;
            }
            if (pathPart.length == 3 && "history".equalsIgnoreCase(pathPart[2])
                    && query == null) {
                String response = gson.toJson(taskManager.getHistory());
                sendText(httpExchange, response);
                System.out.println("Все задачи выведены");
                return;
            }

            if (pathPart.length == 3 && "epic".equalsIgnoreCase(pathPart[2])
                    && query == null) {
                String response = gson.toJson(taskManager.returnAllEpic());
                sendText(httpExchange, response);
                System.out.println("Все эпики выведены");
                return;
            }
            if (pathPart.length == 3 && "task".equalsIgnoreCase(pathPart[2])
                    && query == null) {
                String response = gson.toJson(taskManager.returnAllCommonTask());
                sendText(httpExchange, response);
                System.out.println("Все обычные задачи выведены");
                return;
            }
            if (pathPart.length == 3 && "subtask".equalsIgnoreCase(pathPart[2])) {
                String response = gson.toJson(taskManager.returnAllSubtask());
                sendText(httpExchange, response);
                System.out.println("Все подзадачи выведены");
                return;
            }
            if (pathPart.length == 2 && "tasks".equalsIgnoreCase(pathPart[1])) {
                int idTask = parseTaskId(query.split("=")[1]);
                String response;
                try {
                    response = gson.toJson(taskManager.returnTaskById(idTask));
                } catch (NullPointerException e) {
                    System.out.println("Такой задачи нет: " + idTask);
                    httpExchange.sendResponseHeaders(405, 0);
                    return;
                }
                if (idTask != -1) {
                    System.out.println("Выведена задача под номером: " + idTask);
                    sendText(httpExchange, response);
                    return;
                } else {
                    System.out.println("Некорректный id: " + idTask);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
            if (pathPart.length == 3 && "epic".equalsIgnoreCase(pathPart[2])) {
                int idEpic = parseTaskId(query.split("=")[1]);
                String response;
                try {
                    response = gson.toJson(taskManager.returnTaskByEpic(idEpic));
                } catch (NullPointerException e) {
                    System.out.println("Такой задачи нет: " + idEpic);
                    httpExchange.sendResponseHeaders(405, 0);
                    return;
                }
                if (idEpic != -1) {
                    System.out.println("Выведены подзадачи  эпика под номером: " + idEpic);
                    JsonElement jsonElement = gson.toJsonTree(taskManager.returnTaskByEpic(idEpic));
                    if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() == 0) {
                        sendText(httpExchange, "У эпика нет подзадач");

                    } else {
                        sendText(httpExchange, response);
                    }
                } else {
                    System.out.println("Некорректный id: " + idEpic);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postTask(HttpExchange httpExchange, String[] pathPart) {
        try {
            if ("epic".equalsIgnoreCase(pathPart[2])) {
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), UTF_8);
                System.out.println("Тело запроса: " + body);
                JsonElement jsonElement = JsonParser.parseString(body);
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String name = jsonObject.get("nameTask").getAsString();
                    String descriptionTask = jsonObject.get("descriptionTask").getAsString();
                    if (jsonObject.has("idTask")) {
                        int epicId = jsonObject.get("idTask").getAsInt();
                        taskManager.updateEpicCommonTask(new Epic(epicId, name, descriptionTask));
                        System.out.println("Эпик обновлен");
                        sendText(httpExchange, "Эпик обновлен");
                    } else {
                        taskManager.addEpicTask(new Epic(name, descriptionTask));
                        System.out.println("Эпик добавлен");
                        sendText(httpExchange, "Эпик добавлен");
                    }
                } catch (Exception e) {
                    httpExchange.sendResponseHeaders(405, 0);
                    e.printStackTrace();
                }
                return;
            }
            if ("task".equalsIgnoreCase(pathPart[2])) {
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), UTF_8);
                System.out.println("Тело запроса: " + body);
                JsonElement jsonElement = JsonParser.parseString(body);
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("idTask")) {
                        int idTask = jsonObject.get("idTask").getAsInt();
                        String name = jsonObject.get("nameTask").getAsString();
                        String descriptionTask = jsonObject.get("descriptionTask").getAsString();
                        String statusTaskString = jsonObject.get("statusTask").getAsString();
                        Status statusTask = Status.valueOf(statusTaskString);
                        String startTime = jsonObject.get("startTime").getAsString();
                        String duration = jsonObject.get("duration").getAsString();
                        CommonTask commonTask = new CommonTask(idTask, name, descriptionTask, statusTask, startTime, duration);
                        taskManager.updateCommonTask(commonTask);
                        System.out.println("Обычная задача обновлена");
                        sendText(httpExchange, "Обычная задача обновлена");
                    } else {
                        CommonTask commonTask = gson.fromJson(body, CommonTask.class);
                        taskManager.addCommonTask(commonTask);
                        System.out.println("Обычная задача добавлена");
                        sendText(httpExchange, "Обычная задача добавлена");
                    }
                } catch (Exception e) {
                    httpExchange.sendResponseHeaders(405, 0);
                    e.printStackTrace();
                }
                return;
            }
            if ("subtask".equalsIgnoreCase(pathPart[2])) {
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), UTF_8);
                System.out.println("Тело запроса: " + body);
                JsonElement jsonElement = JsonParser.parseString(body);
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("idTask")) {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        taskManager.updateSubtaskTask(subtask);
                        System.out.println("Подзадача обновлена");
                        sendText(httpExchange, "Подзадача обновлена");
                    } else {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        taskManager.addSubTask(subtask);
                        System.out.println("Подзадача добавлена");
                        sendText(httpExchange, "Подзадача добавлена");
                    }
                } catch (Exception e) {
                    httpExchange.sendResponseHeaders(405, 0);
                    e.printStackTrace();
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTask(HttpExchange httpExchange, String[] pathPart) {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getQuery();
        try {
            if ("epic".equalsIgnoreCase(pathPart[2]) && query != null) {
                int numberTask = parseTaskId(query.split("=")[1]);
                if (numberTask != -1) {
                    try {
                        taskManager.removeEpic(numberTask);
                        System.out.println("Удалил эпик под номером: " + numberTask);
                        sendText(httpExchange, "Удалил эпик под номером: " + numberTask);
                        return;
                    } catch (NullPointerException e) {
                        System.out.println("Эпик невозможно удалить так как эпика под таким id нет: " + numberTask);
                        httpExchange.sendResponseHeaders(405, 0);
                    }

                } else {
                    System.out.println("Такой задачи нет: " + numberTask);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
            if ("task".equalsIgnoreCase(pathPart[2]) && query != null) {
                int numberTask = parseTaskId(query.split("=")[1]);
                if (numberTask != -1) {
                    taskManager.removeCommonTask(numberTask);
                    System.out.println("Удалена задача под номером: " + numberTask);
                    sendText(httpExchange, "Удалена задача под номером: " + numberTask);
                    return;
                } else {
                    System.out.println("Такой задачи нет: " + numberTask);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
            if ("subtask".equalsIgnoreCase(pathPart[2]) && query != null) {
                int numberTask = parseTaskId(query.split("=")[1]);
                if (numberTask != -1) {
                    try {
                        taskManager.removeSubtask(numberTask);
                        System.out.println("Удалена подзадача под номером: " + numberTask);
                        sendText(httpExchange, "Удалена подзадача под номером: " + numberTask);
                        return;
                    } catch (NullPointerException e) {
                        System.out.println("Такой задачи нет: " + numberTask);
                        httpExchange.sendResponseHeaders(405, 0);
                    }

                } else {
                    System.out.println("Такой задачи нет: " + numberTask);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
            if (pathPart.length == 3 && "task".equalsIgnoreCase(pathPart[2]) && query == null) {
                taskManager.removeAllCommonTask();
                System.out.println("Все обычные задачи удалены");
                sendText(httpExchange, "Все обычные задачи удалены");
                return;

            }
            if (pathPart.length == 3 && "epic".equalsIgnoreCase(pathPart[2]) && query == null) {
                taskManager.removeAllEpic();
                System.out.println("Все эпики и связанные с ним подзадачи удалены");
                sendText(httpExchange, "Все эпики и связанные с ним подзадачи удалены");
                return;
            }
            if (pathPart.length == 3 && "subtask".equalsIgnoreCase(pathPart[2]) && query == null) {
                taskManager.removeAllSubtask();
                System.out.println("Все подзадачи удалены");
                sendText(httpExchange, "Все подзадачи удалены");


            } else {
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int parseTaskId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен");
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

}
