package ru.yandex.app.service;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078");
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
