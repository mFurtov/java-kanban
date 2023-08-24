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
        if(subtask.getEpicID()!=0){
            Epic epic = epicTaskMap.get(subtask.getEpicID());
            epic.getSubtaskID().add(subtask.getIdTask());
            setEpicTaskStatus(epic);
        }

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
        int countSubtaskInEpic = epicTask.subtaskID.size();
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
