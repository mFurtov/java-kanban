import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private HashMap<Integer, Epic> epicTaskMap = new HashMap<>();
    private HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    private HashMap<Integer, CommonTask> commonTaskMap = new HashMap<>();
    private int nextId = 1;

    public HashMap<Integer, Epic> getEpicTaskMap() {
        return epicTaskMap;
    }

    public void setEpicTaskMap(HashMap<Integer, Epic> epicTaskMap) {
        this.epicTaskMap = epicTaskMap;
    }

    public HashMap<Integer, Subtask> getSubTaskMap() {
        return subTaskMap;
    }

    public void setSubTaskMap(HashMap<Integer, Subtask> subTaskMap) {
        this.subTaskMap = subTaskMap;
    }

    public HashMap<Integer, CommonTask> getCommonTaskMap() {
        return commonTaskMap;
    }

    public void setCommonTaskMap(HashMap<Integer, CommonTask> commonTaskMap) {
        this.commonTaskMap = commonTaskMap;
    }


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
            putSubTaskIdInEpic(subtask);
            Epic epic = epicTaskMap.get(subtask.getEpicId());
            setEpicTaskStatus(epic);
        } else {
            System.out.println("Нужен номер подзадчи для " + subtask.getNameTask() + ", она не может быть ноль");

        }

    }


    public ArrayList<Map.Entry<Integer, Epic>> returnAllEpic() {
        ArrayList<Map.Entry<Integer, Epic>> allEpic = new ArrayList<>();
        for (Map.Entry<Integer, Epic> entry : epicTaskMap.entrySet()) {
            allEpic.add(entry);
        }
        return allEpic;
    }

    public ArrayList<Map.Entry<Integer, CommonTask>> returnAllCommonTask() {
        ArrayList<Map.Entry<Integer, CommonTask>> allCommonTask = new ArrayList<>();
        for (Map.Entry<Integer, CommonTask> entry : commonTaskMap.entrySet()) {
            allCommonTask.add(entry);
        }
        return allCommonTask;
    }

    public ArrayList<Map.Entry<Integer, Subtask>> returnAllSubtask() {
        ArrayList<Map.Entry<Integer, Subtask>> allSubtask = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> entry : subTaskMap.entrySet()) {
            allSubtask.add(entry);
        }
        return allSubtask;
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

    public void removeEpic(Epic epic) {
        ArrayList<Integer> listSubtask = epic.getSubtasksId();
        for (Integer integer : listSubtask) {
            subTaskMap.remove(integer);
        }
        epicTaskMap.remove(epic.getIdTask());
    }

    public void removeSubtask(Subtask subtask) {
        Epic epicParentToSubtask = epicTaskMap.get(subtask.getEpicId());
        for (int i = 0; i < epicParentToSubtask.getSubtasksId().size(); i++) {
            if (epicParentToSubtask.getSubtasksId().get(i) == subtask.getIdTask()) {
                epicParentToSubtask.getSubtasksId().remove(i);
            }
        }
        subTaskMap.remove(subtask.getIdTask());
        setEpicTaskStatus(epicParentToSubtask);
    }

    public void removeCommonTask(CommonTask commonTask) {
        commonTaskMap.remove(commonTask.getIdTask());
    }

    public void removeAllTask() {
        epicTaskMap.clear();
        subTaskMap.clear();
        commonTaskMap.clear();

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
        Epic oldEpic = epicTaskMap.get(epic.idTask);
        oldEpic.setNameTask(epic.getNameTask());
        oldEpic.setDescriptionTask(epic.getDescriptionTask());
    }

    public void removeById(Integer id) {
        if (commonTaskMap.containsKey(id)) {
            commonTaskMap.remove(id);
        } else if (subTaskMap.containsKey(id)) {
            Epic epicParentToSubtask = epicTaskMap.get(subTaskMap.get(id).getEpicId());
            for (int i = 0; i < epicParentToSubtask.getSubtasksId().size(); i++) {
                if (epicParentToSubtask.getSubtasksId().get(i) == subTaskMap.get(id).getIdTask()) {
                    epicParentToSubtask.getSubtasksId().remove(i);
                }
            }
            subTaskMap.remove(id);
            setEpicTaskStatus(epicParentToSubtask);
        } else if (epicTaskMap.containsKey(id)) {
            Epic epic = epicTaskMap.get(id);
            for (Integer item : epic.getSubtasksId()) {
                subTaskMap.remove(item);
            }
            epicTaskMap.remove(id);
        }
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
            String status = subTaskMap.get(subtaskId).statusTask;
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

    private void putSubTaskIdInEpic(Subtask subtask) {
        for (Integer key : epicTaskMap.keySet()) {
            if (key == subtask.getEpicId()) {
                epicTaskMap.get(key).getSubtasksId().add(subtask.getIdTask());
            }
        }
    }

    private int generateNextID() {
        return nextId++;
    }

}
