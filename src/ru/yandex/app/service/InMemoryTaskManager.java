package ru.yandex.app.service;

import ru.yandex.app.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Epic> epicTaskMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    private HashMap<Integer, CommonAbstractTask> commonTaskMap = new HashMap<>();
    private int nextId = 1;

    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addCommonTask(CommonAbstractTask commonTask) {
        commonTask.setIdTask(generateNextID());
        commonTaskMap.put(commonTask.getIdTask(), commonTask);
    }

    @Override
    public void addEpicTask(Epic epicTask) {
        epicTask.setIdTask(generateNextID());
        epicTaskMap.put(epicTask.getIdTask(), epicTask);

    }

    @Override
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

    @Override
    public ArrayList<Epic> returnAllEpic() {
        enumerationMap(epicTaskMap);
        return new ArrayList<>(epicTaskMap.values());
    }

    @Override
    public ArrayList<CommonAbstractTask> returnAllCommonTask() {
        enumerationMap(commonTaskMap);
        return new ArrayList<>(commonTaskMap.values());
    }

    @Override
    public ArrayList<Subtask> returnAllSubtask() {
        enumerationMap(subTaskMap);
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public ArrayList<AbstractTask> returnAllTask() {
        ArrayList<AbstractTask> allTask = new ArrayList<>();

        for (Map.Entry<Integer, Epic> entry : epicTaskMap.entrySet()) {
            allTask.add(entry.getValue());
            historyManager.add(entry.getValue());
        }
        for (Map.Entry<Integer, Subtask> entry : subTaskMap.entrySet()) {
            allTask.add(entry.getValue());
            historyManager.add(entry.getValue());
        }
        for (Map.Entry<Integer, CommonAbstractTask> entry : commonTaskMap.entrySet()) {
            allTask.add(entry.getValue());
            historyManager.add(entry.getValue());
        }
        return allTask;
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epicTaskMap.get(id);
        ArrayList<Integer> listSubtask = epic.getSubtasksId();
        for (Integer integer : listSubtask) {
            subTaskMap.remove(integer);
            historyManager.remove(integer);
        }
        historyManager.remove(epic.getIdTask());
        epicTaskMap.remove(epic.getIdTask());
    }

    @Override
    public void removeSubtask(Integer id) {
        Epic epicParentToSubtask = epicTaskMap.get(subTaskMap.get(id).getEpicId());
        epicParentToSubtask.getSubtasksId().remove(id);
        subTaskMap.remove(id);
        historyManager.remove(id);
        setEpicTaskStatus(epicParentToSubtask);
    }

    @Override
    public void removeCommonTask(int id) {
        commonTaskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeAllCommonTask() {
        for (Integer id : commonTaskMap.keySet()) {
            historyManager.remove(id);
        }
        commonTaskMap.clear();
    }

    @Override
    public void removeAllSubtask() {
        for (Integer id : subTaskMap.keySet()) {
            historyManager.remove(id);
        }
        for (Epic epic : epicTaskMap.values()) {
            epic.getSubtasksId().clear();
            epic.setStatusTask(Status.NEW);
        }
        subTaskMap.clear();
    }

    @Override
    public void removeAllEpic() {
        for (Integer id : subTaskMap.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : epicTaskMap.keySet()) {
            historyManager.remove(id);
        }
        subTaskMap.clear();
        epicTaskMap.clear();
    }

    @Override
    public AbstractTask returnTaskById(Integer id) {

        if (subTaskMap.containsKey(id)) {
            historyManager.add(subTaskMap.get(id));
            return subTaskMap.get(id);
        } else if (commonTaskMap.containsKey(id)) {
            historyManager.add(commonTaskMap.get(id));
            return commonTaskMap.get(id);
        } else {
            historyManager.add(epicTaskMap.get(id));
            return epicTaskMap.get(id);
        }
    }

    @Override
    public void updateCommonTask(CommonAbstractTask commonTask) {
        commonTaskMap.put(commonTask.getIdTask(), commonTask);
    }

    @Override
    public void updateSubtaskTask(Subtask subtask) {
        subTaskMap.put(subtask.getIdTask(), subtask);
        Epic epic = epicTaskMap.get(subtask.getEpicId());
        setEpicTaskStatus(epic);
    }

    @Override
    public void updateEpicCommonTask(Epic epic) {
        Epic oldEpic = epicTaskMap.get(epic.getIdTask());
        oldEpic.setNameTask(epic.getNameTask());
        oldEpic.setDescriptionTask(epic.getDescriptionTask());
    }

    @Override
    public ArrayList<AbstractTask> returnTaskByEpic(int id) {
        ArrayList<AbstractTask> allSubTaskByEpic = new ArrayList<>();
        Epic epic = epicTaskMap.get(id);
        for (Integer idSubtask : epic.getSubtasksId()) {
            allSubTaskByEpic.add(subTaskMap.get(idSubtask));
            historyManager.add(subTaskMap.get(idSubtask));
        }
        return allSubTaskByEpic;
    }

    @Override
    public void setEpicTaskStatus(Epic epicTask) {
        int countDone = 0;
        int countNew = 0;
        int countSubtaskInEpic = epicTask.getSubtasksId().size();
        for (Integer subtaskId : epicTask.getSubtasksId()) {
            String status = String.valueOf(subTaskMap.get(subtaskId).getStatusTask());
            if (status.equalsIgnoreCase("done")) {
                countDone++;
            } else if (status.equalsIgnoreCase("new")) {
                countNew++;
            } else {
                epicTask.setStatusTask(Status.IN_PROGRESS);
                return;
            }
        }
        if (countNew == countSubtaskInEpic) {
            epicTask.setStatusTask(Status.NEW);
        } else if (countDone == countSubtaskInEpic) {
            epicTask.setStatusTask(Status.DONE);
        } else {
            epicTask.setStatusTask(Status.IN_PROGRESS);
        }

    }

    @Override
    public List<AbstractTask> getHistory() {
        return historyManager.getHistoryNode();
    }

    public <T extends AbstractTask> void enumerationMap(HashMap<Integer, T> taskMap) {
        for (Map.Entry<Integer, T> task : taskMap.entrySet()) {
            historyManager.add(task.getValue());
        }
    }

    private int generateNextID() {
        return nextId++;
    }

    public HashMap<Integer, Epic> getEpicTaskMap() {
        return epicTaskMap;
    }

    public HashMap<Integer, Subtask> getSubTaskMap() {
        return subTaskMap;
    }

    public HashMap<Integer, CommonAbstractTask> getCommonTaskMap() {
        return commonTaskMap;
    }
}
