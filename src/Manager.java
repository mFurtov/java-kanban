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
        updateListEpicTask(epicTask);
        setEpicTaskStatus(epicTask);
        epicTaskMap.put(epicTask.getIdTask(), epicTask);

    }

    public void addSubTask(Subtask subtask) {
        subtask.setIdTask(generateNextID());
        subTaskMap.put(subtask.getIdTask(), subtask);
        if (subtask.getEpicId() != 0) {
            //subTaskMap.put(subtask.getIdTask(), subtask);
            Epic epic = epicTaskMap.get(subtask.getEpicId());
            epic.getSubtasksId().add(subtask.getIdTask());
            setEpicTaskStatus(epic);
        }

    }

    public ArrayList<Object> returnAllTask() {
        ArrayList<Object> allTask = new ArrayList<>();
        for (Map.Entry<Integer, Epic> entry : epicTaskMap.entrySet()) {
            allTask.add(entry);
        }
        for (Map.Entry<Integer, Subtask> entry : subTaskMap.entrySet()) {
            allTask.add(entry);
        }
        for (Map.Entry<Integer, CommonTask> entry : commonTaskMap.entrySet()) {
            allTask.add(entry);
        }
        return allTask;
    }

    public void removeAllTask() {
        epicTaskMap.clear();
        subTaskMap.clear();
        commonTaskMap.clear();

    }

    public Object returnTaskById(Integer id) {

        if (subTaskMap.containsKey(id)) {
            return subTaskMap.get(id);
        } else if (commonTaskMap.containsKey(id)) {
            return commonTaskMap.get(id);
        } else if (epicTaskMap.containsKey(id)) {
            return epicTaskMap.get(id);
        } else {
            return null;
        }
    }

    public void updateCommonTask(CommonTask commonTask) {
        commonTaskMap.put(commonTask.getIdTask(), commonTask);
    }

    public void updateSubtaskTask(Subtask subtask) {
        subTaskMap.put(subtask.getIdTask(), subtask);
        if (subtask.getEpicId() != 0) {
            Epic epic = epicTaskMap.get(subtask.getEpicId());
            if (epic.getSubtasksId().contains(subtask.getIdTask())) {
                setEpicTaskStatus(epic);
            } else {
                epic.getSubtasksId().add(subtask.getIdTask());
                setEpicTaskStatus(epic);
            }
        }
    }

    public void updateEpicCommonTask(Epic epic) {
        epicTaskMap.put(epic.getIdTask(), epic);
        updateListEpicTask(epic);
        setEpicTaskStatus(epic);
    }

    public void removeById(Integer id) {
        if (commonTaskMap.containsKey(id)) {
            commonTaskMap.remove(id);
        } else if (subTaskMap.containsKey(id)) {
            subTaskMap.remove(id);
        } else if (epicTaskMap.containsKey(id)) {
            Epic epic = epicTaskMap.get(id);
            epic.getSubtasksId();
            for (Integer item : epic.getSubtasksId()) {
                subTaskMap.remove(item);
            }
            epicTaskMap.remove(id);
        }
    }

    public ArrayList<Object> returnTaskByEpic(Epic epic) {
        ArrayList<Object> allSubTaskByEpic = new ArrayList<>();
        for (Integer idSubtask : epic.getSubtasksId()) {
            allSubTaskByEpic.add(subTaskMap.get(idSubtask));
        }
        return allSubTaskByEpic;
    }

    private void updateListEpicTask(Epic epicTask) {
        ArrayList<Integer> subtaskID = epicTask.getSubtasksId();
        for (Integer integer : subtaskID) {
            for (Map.Entry<Integer, Subtask> item : subTaskMap.entrySet()) {
                if (item.getKey() == integer) {
                    item.getValue().setEpicId(epicTask.getIdTask());
                }
            }
        }

    }

    private void setEpicTaskStatus(Epic epicTask) {
        ArrayList<String> status = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> item : subTaskMap.entrySet()) {
            if (item.getValue().getEpicId() == epicTask.getIdTask()) {
                status.add(item.getValue().getStatusTask());
            }
        }
        int countDone = 0;
        int countNew = 0;
        int countSubtaskInEpic = epicTask.getSubtasksId().size();
        for (String value : status) {
            if (value.equalsIgnoreCase("done")) {
                countDone++;
            } else if (value.equalsIgnoreCase("new")) {
                countNew++;
            } else {
                continue;
            }
        }
        if (status.size() == 0 || countNew == countSubtaskInEpic) {
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
