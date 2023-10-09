package ru.yandex.app.service;

import ru.yandex.app.model.CommonTask;
import ru.yandex.app.model.Epic;
import ru.yandex.app.model.Subtask;
import ru.yandex.app.model.AbstractTask;

import java.util.ArrayList;
import java.util.List;


public interface TaskManager {
    void addCommonTask(CommonTask commonTask);

    void addEpicTask(Epic epicTask);

    void addSubTask(Subtask subtask);

    ArrayList<Epic> returnAllEpic();

    ArrayList<CommonTask> returnAllCommonTask();

    ArrayList<Subtask> returnAllSubtask();

    ArrayList<AbstractTask> returnAllTask();

    void removeEpic(int id);

    void removeSubtask(Integer id);

    void removeCommonTask(int id);

    void removeAllSubtask();

    void removeAllEpic();

    void removeAllCommonTask();

    AbstractTask returnTaskById(Integer id);

    void updateCommonTask(CommonTask commonTask);

    void updateSubtaskTask(Subtask subtask);

    void updateEpicCommonTask(Epic epic);

    ArrayList<AbstractTask> returnTaskByEpic(int id);

    void setEpicTaskStatus(Epic epicTask);

    List<AbstractTask> getHistory();

}
