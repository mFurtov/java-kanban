package ru.yandex.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected int idTask;
    protected String nameTask;
    protected String descriptionTask;
    protected Status statusTask;
    protected String duration;
    protected String startTime;

    private Task(){

    }
    public Task(String nameTask, String descriptionTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
    }

    public Task(int idTask, String nameTask, String descriptionTask) {
        this.idTask = idTask;
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
    }

    public Task(String nameTask, String descriptionTask, Status statusTask, String startTime, String duration) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.duration = duration;
        this.startTime = startTime;
        this.statusTask = statusTask;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public Status getStatusTask() {
        return statusTask;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStatusTask(Status statusTask) {
        this.statusTask = statusTask;
    }

    public String getEndTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        Long durationAtTask = Long.parseLong(duration);
        LocalDateTime startTimeAtTask = LocalDateTime.parse(startTime, dateTimeFormatter).plusMinutes(durationAtTask);
        return startTimeAtTask.format(dateTimeFormatter);
    }

}
