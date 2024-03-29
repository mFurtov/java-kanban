package ru.yandex.app.service;

import ru.yandex.app.model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Epic> epicTaskMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    private HashMap<Integer, CommonTask> commonTaskMap = new HashMap<>();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(new StartTaskComparator());
    private int nextId = 1;


    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addCommonTask(CommonTask commonTask) {
        commonTask.setIdTask(generateNextID());
        getPrioritizedTasks();
        intersectionCheck(commonTask);
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
            getPrioritizedTasks();
            intersectionCheck(subtask);
            subTaskMap.put(subtask.getIdTask(), subtask);
            Epic epic = epicTaskMap.get(subtask.getEpicId());
            epic.getSubtasksId().add(subtask.getIdTask());
            epic.getSubtaskInEpic().put(subtask.getIdTask(), subtask);
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
    public ArrayList<CommonTask> returnAllCommonTask() {
        enumerationMap(commonTaskMap);
        return new ArrayList<>(commonTaskMap.values());
    }

    @Override
    public ArrayList<Subtask> returnAllSubtask() {
        enumerationMap(subTaskMap);
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public ArrayList<Task> returnAllTask() {
        ArrayList<Task> allTask = new ArrayList<>();

        for (Map.Entry<Integer, Epic> entry : epicTaskMap.entrySet()) {
            allTask.add(entry.getValue());
            historyManager.add(entry.getValue());
        }
        for (Map.Entry<Integer, Subtask> entry : subTaskMap.entrySet()) {
            allTask.add(entry.getValue());
            historyManager.add(entry.getValue());
        }
        for (Map.Entry<Integer, CommonTask> entry : commonTaskMap.entrySet()) {
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
        epicParentToSubtask.getSubtaskInEpic().remove(id);
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
            epic.getSubtaskInEpic().clear();
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
    public Task returnTaskById(Integer id) {

        if (subTaskMap.containsKey(id)) {
            historyManager.add(subTaskMap.get(id));
            return subTaskMap.get(id);
        } else if (commonTaskMap.containsKey(id)) {
            historyManager.add(commonTaskMap.get(id));
            return commonTaskMap.get(id);
        } else if (epicTaskMap.containsKey(id)) {
            historyManager.add(epicTaskMap.get(id));
            return epicTaskMap.get(id);
        } else {
            return new Task(-1, "EmptyTask", "EmptyTask");
        }
    }

    @Override
    public void updateCommonTask(CommonTask commonTask) {
        getPrioritizedTasks();
        intersectionCheck(commonTask);
        commonTaskMap.put(commonTask.getIdTask(), commonTask);
    }

    @Override
    public void updateSubtaskTask(Subtask subtask) {
        getPrioritizedTasks();
        intersectionCheck(subtask);
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
    public ArrayList<Task> returnTaskByEpic(int id) {
        ArrayList<Task> allSubTaskByEpic = new ArrayList<>();
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
            if ("done".equalsIgnoreCase(status)) {//status.equalsIgnoreCase("done")
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        prioritizedTasks.addAll(subTaskMap.values());
        prioritizedTasks.addAll(commonTaskMap.values());
        return prioritizedTasks;
    }


    private void intersectionCheck(Task task) {
        for (Task pt : prioritizedTasks) {
            if (task.getStartTime() == pt.getStartTime() && task.getStartTime() != null
                    && task.getIdTask() != pt.getIdTask()) {
                throw new IntersectionTimeException("Задача " + task.getIdTask() + " " + task.getNameTask()
                        + " не может пересекаться по началу времени с " + pt.getIdTask() + " " + pt.getNameTask());
            }
//            if (task.getStartTime() != pt.getStartTime()
//                    || task.getStartTime() == null || task.getIdTask()==pt.getIdTask()) {
//
//            } else {
//                throw new RuntimeException("Задача " + task.getIdTask() + " " + task.getNameTask()
//                        + " не может пересекаться по началу времени с " + pt.getIdTask() + " " + pt.getNameTask());
//            }
        }
    }

    private <T extends Task> void enumerationMap(HashMap<Integer, T> taskMap) {
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

    public HashMap<Integer, CommonTask> getCommonTaskMap() {
        return commonTaskMap;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
