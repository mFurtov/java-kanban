import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    HashMap<Integer, Epic> epicTaskMap = new HashMap<>();
    HashMap<Integer, Subtask> subTaskMap = new HashMap<>();
    HashMap<Integer, CommonTask> commonTaskMap = new HashMap<>();

    private int nextId = 1;

    public void addCommonTask(CommonTask commonTask) {
        commonTask.setIdTask(nextId);
        commonTaskMap.put(commonTask.getIdTask(), commonTask);
        nextId++;
    }

    public void addEpicTask(Epic epicTask) {
        epicTask.setIdTask(nextId);
        nextId++;
        updateListEpicTask(epicTask);
        setEpicTaskStatus(epicTask);
        epicTaskMap.put(epicTask.getIdTask(), epicTask);

    }

    public void addSubTask(Subtask subtask) {
        subtask.setIdTask(nextId);
        subTaskMap.put(subtask.getIdTask(), subtask);
        nextId++;
        if (subtask.getEpicID() != 0) {
            Epic epic = epicTaskMap.get(subtask.getEpicID());
            epic.getSubtaskID().add(subtask.getIdTask());
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
        if (subtask.getEpicID() != 0) {
            Epic epic = epicTaskMap.get(subtask.getEpicID());
            if (epic.getSubtaskID().contains(subtask.getIdTask())) {
                setEpicTaskStatus(epic);
            } else {
                epic.getSubtaskID().add(subtask.getIdTask());
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
            epic.getSubtaskID();
            for (Integer item : epic.getSubtaskID()) {
                subTaskMap.remove(item);
            }
            epicTaskMap.remove(id);
        }
    }

    public ArrayList<Object> returnTaskByEpic(Epic epic) {
        ArrayList<Object> allSubTaskByEpic = new ArrayList<>();
        for (Integer idSubtask : epic.getSubtaskID()) {
            allSubTaskByEpic.add(subTaskMap.get(idSubtask));
        }
        return allSubTaskByEpic;
    }

    private void updateListEpicTask(Epic epicTask) {
        ArrayList<Integer> subtaskID = epicTask.getSubtaskID();
        for (Integer integer : subtaskID) {
            for (Map.Entry<Integer, Subtask> item : subTaskMap.entrySet()) {
                if (item.getKey() == integer) {
                    item.getValue().setEpicID(epicTask.getIdTask());
                }
            }
        }

    }

    private void setEpicTaskStatus(Epic epicTask) {
        ArrayList<String> status = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> item : subTaskMap.entrySet()) {
            if (item.getValue().getEpicID() == epicTask.getIdTask()) {
                status.add(item.getValue().getStatusTask());
            }
        }
        int countDone = 0;
        int countNew = 0;
        int countSubtaskInEpic = epicTask.getSubtaskID().size();
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

}
