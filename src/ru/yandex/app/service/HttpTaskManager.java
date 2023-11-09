package ru.yandex.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import ru.yandex.app.model.*;


import java.io.FileWriter;
import java.io.IOException;

import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient client;
    Gson gson;

    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);
        gson = createGson();
    }

    public void addCommonTask(CommonTask commonTask) {
        super.addCommonTask(commonTask);
        save();
    }

    public void addEpicTask(Epic epicTask) {
        super.addEpicTask(epicTask);
        save();
    }

    public void addSubTask(Subtask subtask) {
        super.addSubTask(subtask);
        save();
    }

    public ArrayList<Epic> returnAllEpic() {
        ArrayList<Epic> allEpic = super.returnAllEpic();
        save();
        return allEpic;

    }

    public ArrayList<CommonTask> returnAllCommonTask() {
        ArrayList<CommonTask> allCommonTask = super.returnAllCommonTask();
        save();
        return allCommonTask;

    }

    public ArrayList<Subtask> returnAllSubtask() {
        ArrayList<Subtask> allSubtask = super.returnAllSubtask();
        save();
        return allSubtask;
    }

    public ArrayList<Task> returnAllTask() {
        ArrayList<Task> allTask = super.returnAllTask();
        save();
        return allTask;
    }

    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    public void removeSubtask(Integer id) {
        super.removeSubtask(id);
        save();
    }

    public void removeCommonTask(int id) {
        super.removeCommonTask(id);
        save();
    }

    public void removeAllCommonTask() {
        super.removeAllCommonTask();
        save();
    }

    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    public Task returnTaskById(Integer id) {
        Task task = super.returnTaskById(id);
        save();
        return task;
    }

    public void updateCommonTask(CommonTask commonTask) {
        super.updateCommonTask(commonTask);
        save();
    }

    public void updateSubtaskTask(Subtask subtask) {
        super.updateSubtaskTask(subtask);
        save();
    }

    public void updateEpicCommonTask(Epic epic) {
        super.updateEpicCommonTask(epic);
        save();
    }

    public ArrayList<Task> returnTaskByEpic(int id) {
        ArrayList<Task> taskByEpic = super.returnTaskByEpic(id);
        save();
        return taskByEpic;
    }

    public void save() {
        ArrayList<Task> allTask = inGeneralList();
        ArrayList<Task> epicList = new ArrayList<>();
        ArrayList<Task> subtaskList = new ArrayList<>();
        ArrayList<Task> commonTaskList = new ArrayList<>();


        for (Task task : allTask) {
            if (task instanceof Epic) {
                epicList.add(task);
            } else if (task instanceof CommonTask) {
                commonTaskList.add(task);
            } else if (task instanceof Subtask) {
                subtaskList.add(task);
            }
        }
        client.put("epic", gson.toJson(epicList));
        client.put("task", gson.toJson(subtaskList));
        client.put("subtask", gson.toJson(commonTaskList));
    }

    private ArrayList<Task> inGeneralList() {
        ArrayList<Task> generalList = new ArrayList<>();

        for (Map.Entry<Integer, Epic> entry : super.getEpicTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        for (Map.Entry<Integer, Subtask> entry : super.getSubTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        for (Map.Entry<Integer, CommonTask> entry : super.getCommonTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        return generalList;
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

    public static HttpTaskManager loadFromServer(String url) {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try {
            String fileLine = Files.readString(Path.of(file));
            String[] line = fileLine.split("\n");
            int fileContainsSomething = 2;
            if (line.length >= fileContainsSomething) {
                loadTask(fileBackedTasksManager, line);
            }
            loadHistory(fileBackedTasksManager, line);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return fileBackedTasksManager;
    }

    private static void loadTask(FileBackedTasksManager fileBackedTasksManager, String[] line) {
//        for (int i = 1; i < line.length; i++) {
//            if (!line[i].trim().isEmpty()) {
//                Task task = fileBackedTasksManager.fromString(line[i]);
//                if (task instanceof Epic) {
//                    HashMap<Integer, Epic> epicTaskMap = fileBackedTasksManager.getEpicTaskMap();
//                    epicTaskMap.put(task.getIdTask(), (Epic) task);
//                } else if (task instanceof Subtask) {
//                    HashMap<Integer, Subtask> subTaskMap = fileBackedTasksManager.getSubTaskMap();
//                    subTaskMap.put(task.getIdTask(), (Subtask) task);
//                } else if (task instanceof CommonTask) {
//                    HashMap<Integer, CommonTask> commonTaskMap = fileBackedTasksManager.getCommonTaskMap();
//                    commonTaskMap.put(task.getIdTask(), (CommonTask) task);
//                } else {
//                    throw new IllegalArgumentException("Необходимо проверить файл, у одной из указанных задач не верный тип");
//                }
//            } else {
//                break;
//            }
//        }
    }


    private static void loadHistory(FileBackedTasksManager fbt, String[] line) {
//        int fileContainsSomethingAndHistory = 2;
//        if (line.length > fileContainsSomethingAndHistory
//                && line[line.length - 2].trim().isEmpty()) {
//            for (Integer integer : historyFromString(line[line.length - 1])) {
//                fbt.getHistoryManager().add(fbt.returnHistoryTask(integer, fbt));
//            }
//        } else {
//            return;
//        }
    }

    private Task returnHistoryTask(Integer number, FileBackedTasksManager fbtm) {
        HashMap<Integer, CommonTask> commonTaskHashMap = fbtm.getCommonTaskMap();
        HashMap<Integer, Subtask> subtaskHashMap = fbtm.getSubTaskMap();
        HashMap<Integer, Epic> epicHashMap = fbtm.getEpicTaskMap();
        if (commonTaskHashMap.containsKey(number)) {
            return commonTaskHashMap.get(number);
        } else if (subtaskHashMap.containsKey(number)) {
            return subtaskHashMap.get(number);
        } else {
            return epicHashMap.get(number);
        }
    }

    private Task fromString(String value) {
        int firstComma = value.indexOf(",") + 1;
        int secondComma = value.indexOf(",", firstComma);
        TypeTask whatTheTask = TypeTask.valueOf(value.substring(firstComma, secondComma));
        String[] partString = value.split(",");
        switch (whatTheTask) {
            case TASK:
                return new CommonTask(Integer.parseInt(partString[0])
                        , partString[2], partString[4], Status.valueOf(partString[3]), partString[5], partString[6]);
            case SUBTASK:
                return new Subtask(Integer.parseInt(partString[0]), partString[2], partString[4]
                        , Status.valueOf(partString[3]), Integer.parseInt(partString[5]), partString[6], partString[7]);

            case EPIC:
                Epic epicTask = new Epic(Integer.parseInt(partString[0]), partString[2], partString[4]);
                epicTask.setStatusTask(Status.valueOf(partString[3]));
                return epicTask;
            default:
                return null;
        }
    }

    static List<Integer> historyFromString(String value) {
        String[] valueArray = value.split(",");
        List<Integer> list = new ArrayList<>();

        for (String s : valueArray) {
            list.add(Integer.parseInt(s));
        }
        Collections.reverse(list);
        return list;
    }

    private static String historyToString(HistoryManager manager) {
        ArrayList<String> history = new ArrayList<>();

        for (Task task : manager.getHistory()) {
            history.add(Integer.toString(task.getIdTask()));
        }
        return String.join(",", history);
    }

    public static void main(String[] args) {
        CommonTask commonTask1 = new CommonTask("Сходить на почту"
                , "получить поссылку из деревни"
                , Status.IN_PROGRESS, "2022.10.23 14:30", "80");
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты"
                , Status.IN_PROGRESS, "2022.10.23 13:30", "80");

        Epic epic1 = new Epic("Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        Epic epic2 = new Epic(2, "Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер"
                , Status.DONE, 1, "2022.10.23 11:30", "80");
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой"
                , "Нужно собрать костюм на свадьбу друга"
                , Status.NEW, 3, "2022.10.23 15:30", "80");
        Subtask subtask3 = new Subtask("Купить букет жениху и невесте"
                , "Нужно собрать костюм на свадьбу друга"
                , Status.NEW, 3, "2022.10.23 04:30", "80");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.addEpicTask(epic1);
        fileBackedTasksManager.addSubTask(subtask1);
        System.out.println(fileBackedTasksManager.returnAllTask());

        FileBackedTasksManager newFileBackedTasksManager = fileBackedTasksManager.loadFromFile("task.csv");
        System.out.println(newFileBackedTasksManager.returnAllTask());

    }

}


