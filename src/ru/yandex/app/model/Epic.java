package ru.yandex.app.model;

import java.util.ArrayList;


public class Epic extends AbstractTask {
    private final ArrayList<Integer> subtasksId = new ArrayList<>();

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


    @Override
    public String toString() {
        return idTask + ","
                + TypeTask.EPIC + ","
                + nameTask + ","
                + statusTask + ","
                + descriptionTask;
    }
}
