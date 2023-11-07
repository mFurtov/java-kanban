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
                    postTask(httpExchange,pathParts,path);
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
        if (pathPart.length == 2 && "tasks".equalsIgnoreCase(pathPart[1])) {
            String response = gson.toJson(taskManager.returnAllTask());
            sendText(httpExchange, response);
            System.out.println("Все задачи выведены");
            return;
        }
        if (pathPart.length == 3 && "epic".equalsIgnoreCase(pathPart[2])) {
            String response = gson.toJson(taskManager.returnAllEpic());
            sendText(httpExchange, response);
            System.out.println("Все эпики выведены");
            return;
        }
        if (pathPart.length == 3 && "task".equalsIgnoreCase(pathPart[2])) {
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
        if (pathPart.length == 3 && "tasks".equalsIgnoreCase(pathPart[1])
                && Pattern.matches("^/tasks/\\d+$", path)) {
            int idTask = parseTaskId(pathPart[2]);
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
        if (pathPart.length == 4 && "epic".equalsIgnoreCase(pathPart[2])
                && Pattern.matches("^/tasks/epic/\\d+$", path)) {
            int idEpic = parseTaskId(pathPart[3]);
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
                return;

            } else {
                System.out.println("Некорректный id: " + idEpic);
                httpExchange.sendResponseHeaders(405, 0);
            }
        } else {
            httpExchange.sendResponseHeaders(405, 0);

        }
    }
    private void postTask(HttpExchange httpExchange, String[] pathPart, String path) throws IOException {
        if ("epic".equalsIgnoreCase(pathPart[2])){
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), UTF_8);
            System.out.println("Тело запроса: " + body);
            JsonElement jsonElement = JsonParser.parseString(body);
            try {
                JsonObject jsonObject =jsonElement.getAsJsonObject();
                String name = jsonObject.get("nameTask").getAsString();
                String descriptionTask = jsonObject.get("descriptionTask").getAsString();
               if(jsonObject.has("id")){
                   int epicId = jsonObject.get("id").getAsInt();
                   taskManager.updateEpicCommonTask(new Epic(epicId,name,descriptionTask));
                   sendText(httpExchange, "Эпик обновлен");
               }else {
                   taskManager.addEpicTask(new Epic(name,descriptionTask));
                   sendText(httpExchange, "Эпик добавлен");
               }
            }catch (Exception e) {
                System.out.println("При добавление/обновлении что то пошло нет так");
            }
        }if ("task".equalsIgnoreCase(pathPart[2])){
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), UTF_8);
            System.out.println("Тело запроса: " + body);
            JsonElement jsonElement = JsonParser.parseString(body);
            try {
                JsonObject jsonObject =jsonElement.getAsJsonObject();
                String name = jsonObject.get("nameTask").getAsString();
                String descriptionTask = jsonObject.get("descriptionTask").getAsString();

                if(jsonObject.has("id")){
                    int epicId = jsonObject.get("id").getAsInt();
                    taskManager.updateEpicCommonTask(new Epic(epicId,name,descriptionTask));
                    sendText(httpExchange, "Эпик обновлен");
                }else {
                    taskManager.addEpicTask(new Epic(name,descriptionTask));
                    sendText(httpExchange, "Эпик добавлен");
                }
            }catch (Exception e) {
                System.out.println("При добавление/обновлении что то пошло нет так");
            }
        }
    }

    private void deleteTask(HttpExchange httpExchange, String[] pathPart, String path) throws IOException {
        if ("epic".equalsIgnoreCase(pathPart[2]) && Pattern.matches("^/tasks/epic/\\d+$", path)) {
            int numberTask = parseTaskId(pathPart[3]);
            if (numberTask != -1) {
                try {
                    taskManager.removeEpic(numberTask);
                    System.out.println("Удалил эпик под номером: " + numberTask);
                    httpExchange.sendResponseHeaders(200, 0);
                } catch (NullPointerException e) {
                    System.out.println("Эпик невозможно удалить так как эпика под таким id нет: " + numberTask);
                    httpExchange.sendResponseHeaders(405, 0);
                    return;
                }

            } else {
                System.out.println("Такой задачи нет: " + numberTask);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
        if ("task".equalsIgnoreCase(pathPart[2]) && Pattern.matches("^/tasks/task/\\d+$", path)) {
            int numberTask = parseTaskId(pathPart[3]);
            if (numberTask != -1) {
                taskManager.removeCommonTask(numberTask);
                System.out.println("Удалена задача под номером: " + numberTask);
                httpExchange.sendResponseHeaders(200, 0);

            } else {
                System.out.println("Такой задачи нет: " + numberTask);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
        if ("subtask".equalsIgnoreCase(pathPart[2]) && Pattern.matches("^/tasks/subtask/\\d+$", path)) {
            int numberTask = parseTaskId(pathPart[3]);
            if (numberTask != -1) {
                try {
                    taskManager.removeSubtask(numberTask);
                    System.out.println("Удалена подзадача под номером: " + numberTask);
                    httpExchange.sendResponseHeaders(200, 0);
                } catch (NullPointerException e) {
                    System.out.println("Такой задачи нет: " + numberTask);
                    httpExchange.sendResponseHeaders(405, 0);
                }

            } else {
                System.out.println("Такой задачи нет: " + numberTask);
                httpExchange.sendResponseHeaders(405, 0);
            }
        }
        if (pathPart.length == 3 && "task".equalsIgnoreCase(pathPart[2])) {
            taskManager.removeAllCommonTask();
            System.out.println("Все обычные задачи удалены");
            httpExchange.sendResponseHeaders(200, 0);
            return;
        }
        if (pathPart.length == 3 && "epic".equalsIgnoreCase(pathPart[2])) {
            taskManager.removeAllEpic();
            System.out.println("Все эпики и связанные с ним подзадачи удалены");
            httpExchange.sendResponseHeaders(200, 0);
            return;
        }
        if (pathPart.length == 3 && "subtask".equalsIgnoreCase(pathPart[2])) {
            taskManager.removeAllSubtask();
            System.out.println("Все подзадачи удалены");
            httpExchange.sendResponseHeaders(200, 0);
            return;
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
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Epic.class, (JsonSerializer<Epic>) (epic, type, jsonSerializationContext) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Type: ", String.valueOf(TypeTask.EPIC));
            jsonObject.addProperty("ID: ", epic.getIdTask());
            jsonObject.addProperty("Name: ", epic.getNameTask());
            jsonObject.addProperty("Description: ", epic.getDescriptionTask());
            jsonObject.addProperty("StatusTask: ", String.valueOf(epic.getStatusTask()));
            jsonObject.addProperty("StartTime: ", epic.getStartTime());
            jsonObject.addProperty("Duration: ", epic.getDuration());
            return jsonObject;
        }).registerTypeAdapter(Subtask.class, (JsonSerializer<Subtask>) (subtask, type, jsonSerializationContext) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Type: ", String.valueOf(TypeTask.SUBTASK));
            jsonObject.addProperty("ID: ", subtask.getIdTask());
            jsonObject.addProperty("Name: ", subtask.getNameTask());
            jsonObject.addProperty("Description: ", subtask.getDescriptionTask());
            jsonObject.addProperty("EpicId: ", String.valueOf(subtask.getEpicId()));
            jsonObject.addProperty("StatusTask: ", String.valueOf(subtask.getStatusTask()));
            jsonObject.addProperty("StartTime: ", subtask.getStartTime());
            jsonObject.addProperty("Duration: ", subtask.getDuration());
            return jsonObject;
        }).registerTypeAdapter(CommonTask.class, (JsonSerializer<CommonTask>) (commonTask, type, jsonSerializationContext) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Type: ", String.valueOf(TypeTask.TASK));
            jsonObject.addProperty("ID: ", commonTask.getIdTask());
            jsonObject.addProperty("Name: ", commonTask.getNameTask());
            jsonObject.addProperty("Description: ", commonTask.getDescriptionTask());
            jsonObject.addProperty("StatusTask: ", String.valueOf(commonTask.getStatusTask()));
            jsonObject.addProperty("StartTime: ", commonTask.getStartTime());
            jsonObject.addProperty("Duration: ", commonTask.getDuration());
            return jsonObject;
        }).create();
    }

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
