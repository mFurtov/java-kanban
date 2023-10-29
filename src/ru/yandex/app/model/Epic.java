package ru.yandex.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Epic extends AbstractTask {
    private final ArrayList<Integer> subtasksId = new ArrayList<>();

    private final HashMap<Integer, Subtask> subtaskInEpic = new HashMap<>();
    protected String endTime;


    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
        this.statusTask = Status.NEW;

    }

    public Epic(int idTask, String nameTask, String descriptionTask) {
        super(idTask, nameTask, descriptionTask);
        this.statusTask = Status.NEW;
    }

    public ArrayList<Integer> getSubtasksId() {

        return subtasksId;
    }

    public String getEndTimeEpic() {
        Long durationAllSubTask = 0L;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        ArrayList<LocalDateTime> startTimeList = new ArrayList<>();
        ArrayList<LocalDateTime> endTimeList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> subtask : subtaskInEpic.entrySet()) {
            LocalDateTime startTime = LocalDateTime.parse(subtask.getValue().getStartTime(), dateTimeFormatter);
            startTimeList.add(startTime);
            Long durationAtTask = Long.valueOf(subtask.getValue().getDuration());
            LocalDateTime endTime = LocalDateTime.parse(subtask.getValue().getStartTime()
                    , dateTimeFormatter).plusMinutes(durationAtTask);
            endTimeList.add(endTime);
            durationAllSubTask += durationAtTask;
        }
        duration = String.valueOf(durationAllSubTask);
        Collections.sort(startTimeList);
        Collections.sort(endTimeList);
        startTime = startTimeList.get(0).format(dateTimeFormatter);
        endTime = endTimeList.get(startTimeList.size() - 1).format(dateTimeFormatter);
        return endTime;
    }

    @Override
    public String getEndTime() {
        return getEndTimeEpic();
    }

    public HashMap<Integer, Subtask> getSubtaskInEpic() {
        return subtaskInEpic;
    }

    @Override
    public String toString() {
        return idTask + ","
                + TypeTask.EPIC + ","
                + nameTask + ","
                + statusTask + ","
                + descriptionTask;
    }
}
