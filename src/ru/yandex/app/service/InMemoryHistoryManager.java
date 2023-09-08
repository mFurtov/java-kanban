package ru.yandex.app.service;

import ru.yandex.app.model.TaskClass;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_LIST_SIZE = 10;
    private static List<TaskClass> tasksHistoryList = new LinkedList<>();

    @Override
    public void add(TaskClass task) {
        if (tasksHistoryList.size() < MAX_LIST_SIZE) {
            tasksHistoryList.add(task);
        } else {
            tasksHistoryList.remove(0);
            tasksHistoryList.add(task);
        }
    }

    @Override
    public List<TaskClass> getHistory() {
        List<TaskClass> copyListReturn = new LinkedList<>(tasksHistoryList);
        return copyListReturn;

    }
}

