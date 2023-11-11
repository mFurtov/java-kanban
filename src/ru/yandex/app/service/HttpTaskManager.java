package ru.yandex.app.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.yandex.app.model.*;
import java.lang.reflect.Type;
import java.util.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private Gson gson;
    private String url = "http://localhost:8078";

    public HttpTaskManager() {
        client = new KVTaskClient(url);
        gson = Managers.getDefaultGson();
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

    @Override
    public void save() {
        ArrayList<Task> allTask = inGeneralList();
        ArrayList<Task> epicList = new ArrayList<>();
        ArrayList<Task> subtaskList = new ArrayList<>();
        ArrayList<Task> commonTaskList = new ArrayList<>();
        ArrayList<Integer> history = new ArrayList<>();
        for (Task task : allTask) {
            if (task instanceof Epic) {
                epicList.add(task);
            } else if (task instanceof CommonTask) {
                commonTaskList.add(task);
            } else if (task instanceof Subtask) {
                subtaskList.add(task);
            } else {
                System.out.println("Задача " + task + " не соответствует ни одному типу");
            }
        }
        if (!epicList.isEmpty()) client.put("epic", gson.toJson(epicList));
        if (!subtaskList.isEmpty()) client.put("subtask", gson.toJson(subtaskList));
        if (!commonTaskList.isEmpty()) client.put("task", gson.toJson(commonTaskList));
        for (Task task : getHistory()) {
            history.add(task.getIdTask());
        }
        if (!history.isEmpty()) client.put("history", gson.toJson(history));
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


    public void loadFromServer(String url) {
        Type epicListType = new TypeToken<List<Epic>>() {
        }.getType();
        String epicJson = (client.load("epic"));
        List<Epic> epicList = gson.fromJson(epicJson, epicListType);
        for (Epic epic : epicList) {
            addEpicTask(epic);
        }

        Type subTaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        String subtaskJson = (client.load("subtask"));
        List<Subtask> subtaskList = gson.fromJson(subtaskJson, subTaskType);
        for (Subtask subTask : subtaskList) {
            addSubTask(subTask);
        }
        Type commonTaskType = new TypeToken<List<CommonTask>>() {
        }.getType();
        String taskJson = (client.load("task"));
        List<CommonTask> commonTaskList = gson.fromJson(taskJson, commonTaskType);
        for (CommonTask commonTask : commonTaskList) {
            addCommonTask(commonTask);
        }
        String historyJson = (client.load("history"));
        Type listType = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> historyList = new Gson().fromJson(historyJson, listType);
        for (Integer integer : historyList) {
            getHistoryManager().add(returnHistoryTask(integer));
        }
    }

    private Task returnHistoryTask(Integer numbers) {
        HashMap<Integer, CommonTask> commonTaskHashMap = getCommonTaskMap();
        HashMap<Integer, Subtask> subtaskHashMap = getSubTaskMap();
        HashMap<Integer, Epic> epicHashMap = getEpicTaskMap();
        if (commonTaskHashMap.containsKey(numbers)) {
            return commonTaskHashMap.get(numbers);
        } else if (subtaskHashMap.containsKey(numbers)) {
            return subtaskHashMap.get(numbers);
        } else {
            return epicHashMap.get(numbers);
        }
    }

}


