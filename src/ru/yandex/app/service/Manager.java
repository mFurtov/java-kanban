package ru.yandex.app.service;

import ru.yandex.app.model.CommonTask;
import ru.yandex.app.model.Epic;
import ru.yandex.app.model.Subtask;
import ru.yandex.app.model.TaskClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private HashMap<Integer, Epic> epicTaskMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    private HashMap<Integer, CommonTask> commonTaskMap = new HashMap<>();
    private int nextId = 1;

    public void addCommonTask(CommonTask commonTask) {
        commonTask.setIdTask(generateNextID());
        commonTaskMap.put(commonTask.getIdTask(), commonTask);
    }

    public void addEpicTask(Epic epicTask) {
        epicTask.setIdTask(generateNextID());
        epicTaskMap.put(epicTask.getIdTask(), epicTask);

    }

    public void addSubTask(Subtask subtask) {
        if (subtask.getEpicId() != 0) {
            subtask.setIdTask(generateNextID());
            subTaskMap.put(subtask.getIdTask(), subtask);
            Epic epic = epicTaskMap.get(subtask.getEpicId());
            epic.getSubtasksId().add(subtask.getIdTask());
            setEpicTaskStatus(epic);
        } else {
            System.out.println("Нужен номер эпика для " + subtask.getNameTask() + ", она не может быть ноль");

        }

    }


    public ArrayList<Epic> returnAllEpic() {
        return new ArrayList<>(epicTaskMap.values());
    }

    public ArrayList<CommonTask> returnAllCommonTask() {
        return new ArrayList<>(commonTaskMap.values());
    }

    public ArrayList<Subtask> returnAllSubtask() {

        return new ArrayList<>(subTaskMap.values());
    }

    public ArrayList<TaskClass> returnAllTask() {
        ArrayList<TaskClass> allTask = new ArrayList<>();

        for (Map.Entry<Integer, Epic> entry : epicTaskMap.entrySet()) {
            allTask.add(entry.getValue());
        }
        for (Map.Entry<Integer, Subtask> entry : subTaskMap.entrySet()) {
            allTask.add(entry.getValue());
        }
        for (Map.Entry<Integer, CommonTask> entry : commonTaskMap.entrySet()) {
            allTask.add(entry.getValue());
        }
        return allTask;
    }

    public void removeEpic(int id) {
        Epic epic = epicTaskMap.get(id);
        ArrayList<Integer> listSubtask = epic.getSubtasksId();
        for (Integer integer : listSubtask) {
            subTaskMap.remove(integer);
        }
        epicTaskMap.remove(epic.getIdTask());
    }

    public void removeSubtask(Integer id) {
        Epic epicParentToSubtask = epicTaskMap.get(subTaskMap.get(id).getEpicId());
        epicParentToSubtask.getSubtasksId().remove(id);
        subTaskMap.remove(id);
        setEpicTaskStatus(epicParentToSubtask);

    }

    public void removeCommonTask(int id) {
        commonTaskMap.remove(id);
    }

    public void removeAllCommonTask() {
        commonTaskMap.clear();
    }

    public void removeAllSubtask() {
        for (Epic epic : epicTaskMap.values()) {
            epic.getSubtasksId().clear();
            epic.setStatusTask("NEW");
        }
        subTaskMap.clear();


    }

    public void removeAllEpic() {
        subTaskMap.clear();
        epicTaskMap.clear();
    }


    public TaskClass returnTaskById(Integer id) {

        if (subTaskMap.containsKey(id)) {
            return subTaskMap.get(id);
        } else if (commonTaskMap.containsKey(id)) {
            return commonTaskMap.get(id);
        } else {
            return epicTaskMap.get(id);
        }
    }

    public void updateCommonTask(CommonTask commonTask) {
        commonTaskMap.put(commonTask.getIdTask(), commonTask);
    }

    public void updateSubtaskTask(Subtask subtask) {
        subTaskMap.put(subtask.getIdTask(), subtask);
        Epic epic = epicTaskMap.get(subtask.getEpicId());
        setEpicTaskStatus(epic);
    }

    public void updateEpicCommonTask(Epic epic) {
        Epic oldEpic = epicTaskMap.get(epic.getIdTask());
        oldEpic.setNameTask(epic.getNameTask());
        oldEpic.setDescriptionTask(epic.getDescriptionTask());
    }

    public ArrayList<TaskClass> returnTaskByEpic(int id) {
        ArrayList<TaskClass> allSubTaskByEpic = new ArrayList<>();
        Epic epic = epicTaskMap.get(id);
        for (Integer idSubtask : epic.getSubtasksId()) {
            allSubTaskByEpic.add(subTaskMap.get(idSubtask));
        }
        return allSubTaskByEpic;
    }


    private void setEpicTaskStatus(Epic epicTask) {
        int countDone = 0;
        int countNew = 0;
        int countSubtaskInEpic = epicTask.getSubtasksId().size();
        for (Integer subtaskId : epicTask.getSubtasksId()) {
            String status = subTaskMap.get(subtaskId).getStatusTask();
            if (status.equalsIgnoreCase("done")) {
                countDone++;
            } else if (status.equalsIgnoreCase("new")) {
                countNew++;
            } else {
                epicTask.setStatusTask("IN_PROGRESS");
                return;
            }
        }
        if (countNew == countSubtaskInEpic) {
            epicTask.setStatusTask("NEW");
        } else if (countDone == countSubtaskInEpic) {
            epicTask.setStatusTask("DONE");
        } else {
            epicTask.setStatusTask("IN_PROGRESS");
        }

    }

    private int generateNextID() {
        return nextId++;
    }

}
