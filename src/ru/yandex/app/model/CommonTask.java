package ru.yandex.app.model;


public class CommonTask extends AbstractTask {

    public CommonTask(String nameTask, String descriptionTask, Status statusTask) {
        super(nameTask, descriptionTask);
        this.statusTask = statusTask;

    }

    public CommonTask(String nameTask, String descriptionTask, Status statusTask, String startTime, String duration) {
        super(nameTask, descriptionTask);
        this.duration = duration;
        this.startTime = startTime;
        this.statusTask = statusTask;
    }

    public CommonTask(int idTask, String nameTask, String descriptionTask, Status statusTask, String startTime, String duration) {
        super(idTask, nameTask, descriptionTask);
        this.duration = duration;
        this.startTime = startTime;
        this.statusTask = statusTask;
    }

    public CommonTask(int idTask, String nameTask, String descriptionTask, Status statusTask) {
        super(idTask, nameTask, descriptionTask);
        this.statusTask = statusTask;

    }


    @Override
    public String toString() {
        return idTask + ","
                + TypeTask.TASK + ","
                + nameTask + ","
                + statusTask + ","
                + descriptionTask;
    }
}
