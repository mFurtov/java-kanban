package ru.yandex.app.model;

public class CommonAbstractTask extends AbstractTask {

    public CommonAbstractTask(String nameTask, String descriptionTask, Status statusTask) {
        super(nameTask, descriptionTask);
        this.statusTask = statusTask;
    }

    public CommonAbstractTask(int idTask, String nameTask, String descriptionTask, Status statusTask) {
        super(idTask, nameTask, descriptionTask);
        this.statusTask = statusTask;
    }

    @Override
    public String toString() {
        return "CommonTask{" +
                "idTask=" + idTask +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                '}';
    }
}
