package ru.yandex.app.service;

import ru.yandex.app.model.AbstractTask;

import java.util.List;

public interface HistoryManager {
    void add(AbstractTask task);

    List<AbstractTask> getHistoryNode();

    void remove(int id);
}
