package ru.yandex.app.model;

public class Subtask extends AbstractTask {
    private final int epicId;

    public Subtask(String nameTask, String descriptionTask, Status statusTask, int epicId) {
        super(nameTask, descriptionTask);
        this.statusTask = statusTask;
        this.epicId = epicId;

    }
    public Subtask(String nameTask, String descriptionTask, Status statusTask
            , int epicId,String startTime, String duration) {
        super(nameTask, descriptionTask);
        this.statusTask = statusTask;
        this.startTime = startTime;
        this.duration = duration;
        this.epicId = epicId;

    }

    public Subtask(int idTask, String nameTask, String descriptionTask, Status statusTask, int epicId) {
        super(idTask, nameTask, descriptionTask);
        this.statusTask = statusTask;
        this.epicId = epicId;

    }

    public int getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return idTask + ","
                + TypeTask.SUBTASK + ","
                + nameTask + ","
                + statusTask + ","
                + descriptionTask + ","
                + epicId;
    }
}
