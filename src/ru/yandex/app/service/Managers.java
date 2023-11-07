package ru.yandex.app.service;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager();
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
