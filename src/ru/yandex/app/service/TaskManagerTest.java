package ru.yandex.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.app.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {

    protected abstract T generateTaskManager();
    @Test
    void addCommonTask() {
        T taskManager = generateTaskManager();
        CommonTask commonTask = new CommonTask("Test addNewTask"
                , "Test addNewTask description", Status.NEW);
        taskManager.addCommonTask(commonTask);
        final CommonTask savedTask = (CommonTask) taskManager.returnTaskById(1);
        assertNotNull(savedTask,"Задача не найдена");
        assertEquals(savedTask,commonTask,"Задачи разные");

        final List<CommonTask> tasks = taskManager.returnAllCommonTask();
        assertNotNull(tasks,"Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(commonTask, tasks.get(0), "Задачи разные");

        assertThrows(NullPointerException.class, () -> taskManager.returnTaskById(0),
                "Запрос пустой задачи вызывает NullPointerException");

    }

    @Test
    void addEpicTaskTest() {
        T taskManager = generateTaskManager();
        Epic epicTask = new Epic("Test addEpicTaskTest"
                , "Test addEpicTaskTest description");
        taskManager.addEpicTask(epicTask);
        final Epic savedTask = (Epic) taskManager.returnTaskById(1);
        assertNotNull(savedTask,"Задача не найдена");
        assertEquals(savedTask,epicTask,"Задачи разные");

        final List<Epic> tasks = taskManager.returnAllEpic();
        assertNotNull(tasks,"Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epicTask, tasks.get(0), "Задачи разные");

        assertThrows(NullPointerException.class, () -> taskManager.returnTaskById(0),
                "Запрос пустой задачи вызывает NullPointerException");
        assertEquals(epicTask.getStatusTask(),Status.NEW);
        Subtask subtask = new Subtask("Test addEpicTaskTest"
                , "Test addEpicTaskTest description",Status.DONE,1);
        taskManager.addSubTask(subtask);
        assertEquals(epicTask.getStatusTask(),Status.DONE);

    }

    @Test
    void addSubTaskTest() {
        T taskManager = generateTaskManager();
        Epic epicTask = new Epic("Test addSubTaskTest"
                , "Test addSubTaskTest description");
        taskManager.addEpicTask(epicTask);
        Subtask subtaskNew = new Subtask("Test addSubTaskTest"
                , "Test addSubTaskTest description",Status.DONE,1);
        taskManager.addSubTask(subtaskNew);
        final Subtask savedTask = (Subtask) taskManager.returnTaskById(2);
        assertNotNull(savedTask,"Задача не найдена");
        assertEquals(savedTask,subtaskNew,"Задачи разные");

        final List<Subtask> tasks = taskManager.returnAllSubtask();
        assertNotNull(tasks,"Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(savedTask, tasks.get(0), "Задачи разные");

        assertThrows(NullPointerException.class, () -> taskManager.returnTaskById(0),
                "Запрос пустой задачи вызывает NullPointerException");

        Subtask subtask = new Subtask("Test addEpicTaskTest"
                , "Test addEpicTaskTest description",Status.DONE,33);
        assertThrows(NullPointerException.class, () ->taskManager.addSubTask(subtask),
                "Добавление сабтаски с несуществующем id епика вызывает NullPointerException");
    }

    @Test
    void returnAllEpicTest() {
        T taskManager = generateTaskManager();
        ArrayList<Epic> emptyList = taskManager.returnAllEpic();
        assertTrue(emptyList.isEmpty(),"Лист не пустой");

        Epic epicTask = new Epic("Test returnAllEpicTest"
                , "Test returnAllEpicTest description");
        taskManager.addEpicTask(epicTask);
        ArrayList<Epic> epicList = taskManager.returnAllEpic();
        assertFalse(epicList.isEmpty(),"Лист пустой");
        assertNotNull(epicList,"Задачи не возвращаются");

    }

    @Test
    void returnAllCommonTaskTest() {
        T taskManager = generateTaskManager();
        ArrayList<CommonTask> emptyList = taskManager.returnAllCommonTask();
        assertTrue(emptyList.isEmpty(),"Лист не пустой");

        CommonTask commonTask = new CommonTask("Test returnAllCommonTaskTest"
                , "Test returnAllCommonTaskTest description", Status.NEW);
        taskManager.addCommonTask(commonTask);
        ArrayList<CommonTask> commonTasksList = taskManager.returnAllCommonTask();
        assertFalse(commonTasksList.isEmpty(),"Лист пустой");
        assertNotNull(commonTasksList,"Задачи не возвращаются");
    }

    @Test
    void returnAllSubtaskTest() {
        T taskManager = generateTaskManager();
        ArrayList<Subtask> emptyList = taskManager.returnAllSubtask();
        assertTrue(emptyList.isEmpty(),"Лист не пустой");

        Epic epicTask = new Epic("Test returnAllEpicTest"
                , "Test returnAllEpicTest description");
        taskManager.addEpicTask(epicTask);
        Subtask subtask = new Subtask("Test returnAllSubtaskTest"
                , "Test returnAllSubtaskTest description", Status.NEW,1);
        taskManager.addSubTask(subtask);
        ArrayList<Subtask> subtasksList = taskManager.returnAllSubtask();
        assertFalse(subtasksList.isEmpty(),"Лист пустой");
        assertNotNull(subtasksList,"Задачи не возвращаются");
    }

    @Test
    void returnAllTaskTest() {
        T taskManager = generateTaskManager();
        ArrayList<AbstractTask> emptyList = taskManager.returnAllTask();
        assertTrue(emptyList.isEmpty(),"Лист не пустой");

        Epic epicTask = new Epic("Test returnAllTaskTest"
                , "Test returnAllTaskTest description");
        taskManager.addEpicTask(epicTask);
        Subtask subtask = new Subtask("Test returnAllTaskTest"
                , "Test returnAllTaskTest description", Status.NEW,1);
        taskManager.addSubTask(subtask);
        CommonTask commonTask = new CommonTask("Test returnAllTaskTest"
                , "Test returnAllTaskTest description", Status.NEW);
        taskManager.addCommonTask(commonTask);
        ArrayList<AbstractTask> allTaskList = taskManager.returnAllTask();
        assertFalse(allTaskList.isEmpty(),"Лист пустой");
        assertNotNull(allTaskList,"Задачи не возвращаются");

    }

    @Test
    void removeEpicTest() {
        T taskManager = generateTaskManager();
        Epic epicTaskOne = new Epic("Test removeEpicTest"
                , "Test returnAllTaskTest description");
        taskManager.addEpicTask(epicTaskOne);
        Epic epicTaskTho = new Epic("Test removeEpicTest"
                , "Test returnAllTaskTest description");
        taskManager.addEpicTask(epicTaskTho);
        ArrayList<AbstractTask> a = taskManager.returnAllTask();
        taskManager.removeEpic(epicTaskOne.getIdTask());
        ArrayList<AbstractTask> b = taskManager.returnAllTask();
        taskManager.returnTaskById(epicTaskOne.getIdTask());
        ArrayList<AbstractTask> c = taskManager.returnAllTask();

        assertNull(taskManager.returnTaskById(epicTaskOne.getIdTask()), "Эпик должен быть удален.");

        assertThrows(NullPointerException.class
                , () -> taskManager.removeEpic(0)
                ,"Удаление несуществующей задачи,или задачи с неверным id");

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