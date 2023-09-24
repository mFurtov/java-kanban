package ru.yandex.app.service;

import ru.yandex.app.model.TaskClass;

import java.util.List;

public interface HistoryManager {
    void add(TaskClass task);

    List<TaskClass> getHistoryNode();

    void remove(int id);
}
