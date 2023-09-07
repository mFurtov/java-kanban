package ru.yandex.app.service;

import ru.yandex.app.model.TaskClass;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static List<TaskClass> tasksHistoryList = new ArrayList<>();

    @Override
    public void add(TaskClass task) {
        if (tasksHistoryList.size() < 10) {
            tasksHistoryList.add(task);
        } else {
            tasksHistoryList.remove(0);
            tasksHistoryList.add(task);
        }
    }

    @Override
    public List<TaskClass> getHistory() {
        return tasksHistoryList;
    }
}

