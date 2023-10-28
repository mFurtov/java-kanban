package ru.yandex.app.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;


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

    public void getEndTimeEpic() {
//        Long durationAllSubTask = 0L;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        ArrayList<LocalTime> startTimeList = new ArrayList<>();
        ArrayList<LocalTime> endTimeList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> subtask : subtaskInEpic.entrySet()) {
            if (subtask.getValue().getStatusTask() != Status.DONE) {

                LocalTime startTime = LocalTime.parse(subtask.getValue().getStartTime(), dateTimeFormatter);
                startTimeList.add(startTime);
                Long durationAtTask = Long.valueOf(subtask.getValue().getDuration());
                LocalTime endTime = LocalTime.parse(subtask.getValue().getStartTime()
                        , dateTimeFormatter).plusMinutes(durationAtTask);
                endTimeList.add(endTime);
                duration += durationAtTask;
            }
        }
        Collections.sort(startTimeList);
        Collections.sort(endTimeList);
        startTime = startTimeList.get(0).format(dateTimeFormatter);
        endTime = endTimeList.get(startTimeList.size()-1).format(dateTimeFormatter);
        System.out.println(duration);
    }
    @Override
    public String getEndTime(){
        getEndTimeEpic();
        return null;
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
