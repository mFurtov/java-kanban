package ru.yandex.app.service;

import org.junit.jupiter.api.Test;
import ru.yandex.app.model.CommonTask;
import ru.yandex.app.model.Epic;
import ru.yandex.app.model.Status;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager>{
     abstract T createTaskManager();

    @Test
    void addCommonTaskTest() {
        T taskManager = createTaskManager();
        CommonTask commonTask = new CommonTask("Test task","Test task discription", Status.NEW);
        taskManager.addCommonTask(commonTask);
        

    }

    @Test
    void addEpicTask() {
    }

    @Test
    void addSubTask() {
    }

    @Test
    void returnAllEpic() {
    }

    @Test
    void returnAllCommonTask() {
    }

    @Test
    void returnAllSubtask() {
    }

    @Test
    void returnAllTask() {
    }

    @Test
    void removeEpic() {
    }

    @Test
    void removeSubtask() {
    }

    @Test
    void removeCommonTask() {
    }

    @Test
    void removeAllCommonTask() {
    }

    @Test
    void removeAllSubtask() {
    }

    @Test
    void removeAllEpic() {
    }

    @Test
    void returnTaskById() {
    }

    @Test
    void updateCommonTask() {
    }

    @Test
    void updateSubtaskTask() {
    }

    @Test
    void updateEpicCommonTask() {
    }

    @Test
    void returnTaskByEpic() {
    }

    @Test
    void setEpicTaskStatus() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void enumerationMap() {
    }

    @Test
    void getEpicTaskMap() {
    }

    @Test
    void getSubTaskMap() {
    }

    @Test
    void getCommonTaskMap() {
    }

    @Test
    void getHistoryManager() {
    }
}