package ru.yandex.app.service;


import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.app.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private Gson gson;
    private TaskManager taskManager;


    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handle);

        addT();//удалить для теста
        gson = createGson();
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdaper)

    }

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    private void handle(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    getTask(httpExchange, pathParts, path);
                    break;
                }
                case "POST": {
                    postTask(httpExchange, pathParts, path);
                    break;

                }
                case "DELETE": {
                    deleteTask(httpExchange, pathParts, path);
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

    private void getTask(HttpExchange httpExchange, String[] pathPart, String path) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getQuery();
        if (pathPart.length == 2 && "tasks".equalsIgnoreCase(pathPart[1])
                && query == null) {
            String response = gson.toJson(taskManager.returnAllTask());
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
        if (pathPart.length == 4 && "epic".equalsIgnoreCase(pathPart[2])) {
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
    }

    private void postTask(HttpExchange httpExchange, String[] pathPart, String path) throws IOException {
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
                    CommonTask commonTask = gson.fromJson(body, CommonTask.class);
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
    }

    private void deleteTask(HttpExchange httpExchange, String[] pathPart, String path) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getQuery();
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
            ;
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
            ;
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

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private Gson createGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Epic.class, (JsonSerializer<Epic>) (epic, type, jsonSerializationContext) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Type", String.valueOf(TypeTask.EPIC));
                    jsonObject.addProperty("idTask", epic.getIdTask());
                    jsonObject.addProperty("nameTask", epic.getNameTask());
                    jsonObject.addProperty("descriptionTask", epic.getDescriptionTask());
                    jsonObject.addProperty("subtasksId", String.valueOf(epic.getSubtasksId()));
                    jsonObject.addProperty("statusTask", String.valueOf(epic.getStatusTask()));
                    jsonObject.addProperty("startTime", epic.getStartTime());
                    jsonObject.addProperty("duration", epic.getDuration());
                    return jsonObject;
                })
                .registerTypeAdapter(Subtask.class, (JsonSerializer<Subtask>) (subtask, type, jsonSerializationContext) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Type", String.valueOf(TypeTask.SUBTASK));
                    jsonObject.addProperty("idTask", subtask.getIdTask());
                    jsonObject.addProperty("nameTask", subtask.getNameTask());
                    jsonObject.addProperty("descriptionTask", subtask.getDescriptionTask());
                    jsonObject.addProperty("epicId", String.valueOf(subtask.getEpicId()));
                    jsonObject.addProperty("statusTask", String.valueOf(subtask.getStatusTask()));
                    jsonObject.addProperty("startTime", subtask.getStartTime());
                    jsonObject.addProperty("duration", subtask.getDuration());
                    return jsonObject;
                })
                .registerTypeAdapter(CommonTask.class, (JsonSerializer<CommonTask>) (commonTask, type, jsonSerializationContext) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Type: ", String.valueOf(TypeTask.TASK));
                    jsonObject.addProperty("idTask: ", commonTask.getIdTask());
                    jsonObject.addProperty("nameTask: ", commonTask.getNameTask());
                    jsonObject.addProperty("descriptionTask: ", commonTask.getDescriptionTask());
                    jsonObject.addProperty("statusTask: ", String.valueOf(commonTask.getStatusTask()));
                    jsonObject.addProperty("startTime: ", commonTask.getStartTime());
                    jsonObject.addProperty("duration: ", commonTask.getDuration());
                    return jsonObject;
                }).create();
    }
//    private Status getStatus(St)

    public static void main(String[] args) throws IOException {

        HttpTaskServer server = new HttpTaskServer();
        server.start();
//        server.stop();
    }

    public void addT() {
        Epic epic1 = new Epic("Купить костюм на свадьбу", "Нужно собрать костюм на свадьбу друга");
        taskManager.addEpicTask(epic1);
        Epic epic2 = new Epic("Купить костюм на свадьбу", "Нужно собрать костюм на свадьбу друга");
        taskManager.addEpicTask(epic2);
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер", Status.DONE, 1, "2022.10.23 11:30", "80");
        taskManager.addSubTask(subtask1);
        CommonTask commonTask1 = new CommonTask("Сходить на почту"
                , "получить поссылку из деревни"
                , Status.IN_PROGRESS, "2022.10.23 14:30", "80");
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты"
                , Status.IN_PROGRESS, "2022.10.23 13:30", "80");
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой"
                , "Нужно собрать костюм на свадьбу друга"
                , Status.NEW, 1, "2022.10.23 15:30", "80");
        taskManager.addSubTask(subtask2);
        taskManager.addCommonTask(commonTask1);
        taskManager.addCommonTask(commonTask2);

    }

}
