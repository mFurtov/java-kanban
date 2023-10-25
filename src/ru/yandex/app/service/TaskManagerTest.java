package ru.yandex.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.app.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


abstract class TaskManagerTest<T extends TaskManager> {

    protected abstract T generateTaskManager();

    @Test
    void addCommonTask() {
        T taskManager = generateTaskManager();
        CommonTask commonTask = new CommonTask("Test addNewTask"
                , "Test addNewTask description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTask);
        final CommonTask savedTask = (CommonTask) taskManager.returnTaskById(1);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(savedTask, commonTask, "Задачи разные");

        final List<CommonTask> tasks = taskManager.returnAllCommonTask();
        assertNotNull(tasks, "Задачи на возвращаются");
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
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(savedTask, epicTask, "Задачи разные");

        final List<Epic> tasks = taskManager.returnAllEpic();
        assertNotNull(tasks, "Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epicTask, tasks.get(0), "Задачи разные");

        assertThrows(NullPointerException.class, () -> taskManager.returnTaskById(0),
                "Запрос пустой задачи вызывает NullPointerException");
        assertEquals(epicTask.getStatusTask(), Status.NEW);
        Subtask subtask = new Subtask("Test addEpicTaskTest"
                , "Test addEpicTaskTest description", Status.DONE, 1);
        taskManager.addSubTask(subtask);
        assertEquals(epicTask.getStatusTask(), Status.DONE);

    }

    @Test
    void addSubTaskTest() {
        T taskManager = generateTaskManager();
        Epic epicTask = new Epic("Test addSubTaskTest"
                , "Test addSubTaskTest description");
        taskManager.addEpicTask(epicTask);
        Subtask subtaskNew = new Subtask("Test addSubTaskTest"
                , "Test addSubTaskTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskNew);
        final Subtask savedTask = (Subtask) taskManager.returnTaskById(2);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(savedTask, subtaskNew, "Задачи разные");

        final List<Subtask> tasks = taskManager.returnAllSubtask();
        assertNotNull(tasks, "Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(savedTask, tasks.get(0), "Задачи разные");

        assertThrows(NullPointerException.class, () -> taskManager.returnTaskById(0),
                "Запрос пустой задачи вызывает NullPointerException");

        Subtask subtask = new Subtask("Test addEpicTaskTest"
                , "Test addEpicTaskTest description", Status.DONE, 33);
        assertThrows(NullPointerException.class, () -> taskManager.addSubTask(subtask),
                "Добавление сабтаски с несуществующем id епика вызывает NullPointerException");
    }

    @Test
    void returnAllEpicTest() {
        T taskManager = generateTaskManager();
        ArrayList<Epic> emptyList = taskManager.returnAllEpic();
        assertTrue(emptyList.isEmpty(), "Лист не пустой");

        Epic epicTask = new Epic("Test returnAllEpicTest"
                , "Test returnAllEpicTest description");
        taskManager.addEpicTask(epicTask);

        final ArrayList<Epic> epicList = taskManager.returnAllEpic();
        assertFalse(epicList.isEmpty(), "Лист пустой");
        assertNotNull(epicList, "Задачи не возвращаются");

    }

    @Test
    void returnAllCommonTaskTest() {
        T taskManager = generateTaskManager();
        ArrayList<CommonTask> emptyList = taskManager.returnAllCommonTask();
        assertTrue(emptyList.isEmpty(), "Лист не пустой");

        CommonTask commonTask = new CommonTask("Test returnAllCommonTaskTest"
                , "Test returnAllCommonTaskTest description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTask);
        final ArrayList<CommonTask> commonTasksList = taskManager.returnAllCommonTask();
        assertFalse(commonTasksList.isEmpty(), "Лист пустой");
        assertNotNull(commonTasksList, "Задачи не возвращаются");
    }

    @Test
    void returnAllSubtaskTest() {
        T taskManager = generateTaskManager();
        ArrayList<Subtask> emptyList = taskManager.returnAllSubtask();
        assertTrue(emptyList.isEmpty(), "Лист не пустой");

        Epic epicTask = new Epic("Test returnAllEpicTest"
                , "Test returnAllEpicTest description");
        taskManager.addEpicTask(epicTask);
        Subtask subtask = new Subtask("Test returnAllSubtaskTest"
                , "Test returnAllSubtaskTest description", Status.NEW, 1);
        taskManager.addSubTask(subtask);
        final ArrayList<Subtask> subtasksList = taskManager.returnAllSubtask();
        assertFalse(subtasksList.isEmpty(), "Лист пустой");
        assertNotNull(subtasksList, "Задачи не возвращаются");
    }

    @Test
    void returnAllTaskTest() {
        T taskManager = generateTaskManager();
        ArrayList<AbstractTask> emptyList = taskManager.returnAllTask();
        assertTrue(emptyList.isEmpty(), "Лист не пустой");

        Epic epicTask = new Epic("Test returnAllTaskTest"
                , "Test returnAllTaskTest description");
        taskManager.addEpicTask(epicTask);
        Subtask subtask = new Subtask("Test returnAllTaskTest"
                , "Test returnAllTaskTest description", Status.NEW, 1);
        taskManager.addSubTask(subtask);
        CommonTask commonTask = new CommonTask("Test returnAllTaskTest"
                , "Test returnAllTaskTest description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTask);
        final ArrayList<AbstractTask> allTaskList = taskManager.returnAllTask();
        assertFalse(allTaskList.isEmpty(), "Лист пустой");
        assertNotNull(allTaskList, "Задачи не возвращаются");

    }

    @Test
    void removeEpicTest() {
        T taskManager = generateTaskManager();
        Epic epicTaskOne = new Epic("Test removeEpicTest"
                , "Test returnAllTaskTest description");
        taskManager.addEpicTask(epicTaskOne);
        Epic epicTaskTwo = new Epic("Test removeEpicTest"
                , "Test returnAllTaskTest description");
        taskManager.addEpicTask(epicTaskTwo);
        Subtask subtaskOne = new Subtask("Test removeEpicTest"
                , "Test removeEpicTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Test removeEpicTest"
                , "Test removeEpicTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskTwo);

        taskManager.removeEpic(epicTaskOne.getIdTask());

        final ArrayList<Epic> epicList = taskManager.returnAllEpic();
        assertEquals(1, epicList.size(), "Неверное количество задач");

        assertThrows(NullPointerException.class
                , () -> taskManager.returnTaskById(epicTaskOne.getIdTask())
                , "Эпик должен быть удален");
        assertThrows(NullPointerException.class
                , () -> taskManager.returnTaskById(subtaskOne.getIdTask())
                , "Сабтакска должена быть удален");
        assertThrows(NullPointerException.class
                , () -> taskManager.returnTaskById(subtaskTwo.getIdTask())
                , "Сабтакска должена быть удален");

        assertThrows(NullPointerException.class
                , () -> taskManager.removeEpic(0)
                , "Удаление несуществующей задачи,или задачи с неверным id");

    }

    @Test
    void removeSubtaskTest() {
        T taskManager = generateTaskManager();
        Epic epicTaskOne = new Epic("Test removeSubtaskTest"
                , "Test removeSubtaskTest description");
        taskManager.addEpicTask(epicTaskOne);
        Subtask subtaskOne = new Subtask("Test removeSubtaskTest"
                , "Test removeSubtaskTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Test removeSubtaskTest"
                , "Test removeSubtaskTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskTwo);
        taskManager.removeSubtask(subtaskOne.getIdTask());

        final ArrayList<Subtask> subtasksList = taskManager.returnAllSubtask();
        assertEquals(1, subtasksList.size(), "Неверное количество задач");

        assertThrows(NullPointerException.class
                , () -> taskManager.returnTaskById(subtaskOne.getIdTask())
                , "Сабтаска должна быть удалена");

        final ArrayList<Integer> subtasksId = epicTaskOne.getSubtasksId();
        assertFalse(subtasksId.contains(epicTaskOne.getIdTask()), "Епик не обновил список подзадач");

        assertThrows(NullPointerException.class
                , () -> taskManager.removeSubtask(0)
                , "Удаление несуществующей задачи,или задачи с неверным id");


    }

    @Test
    void removeCommonTaskTest() {
        T taskManager = generateTaskManager();
        CommonTask commonTaskOne = new CommonTask("Test removeCommonTaskTest"
                , "Test removeCommonTaskTest description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTaskOne);
        CommonTask commonTaskTwo = new CommonTask("Test removeCommonTaskTest"
                , "Test removeCommonTaskTest description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTaskTwo);
        taskManager.removeCommonTask(commonTaskOne.getIdTask());

        final ArrayList<CommonTask> commonTasksList = taskManager.returnAllCommonTask();
        assertEquals(commonTasksList.size(), 1, "Неверное количество задач");

        assertDoesNotThrow(() ->
                        taskManager.removeCommonTask(2)
                , "Удаление несуществующей задачи,или задачи с неверным id");

    }

    @Test
    void removeAllCommonTaskTest() {
        T taskManager = generateTaskManager();
        assertDoesNotThrow(() ->
                        taskManager.removeAllSubtask()
                , "Удаление пустого списка задач");
        CommonTask commonTaskOne = new CommonTask("Test removeAllCommonTaskTest"
                , "Test removeAllCommonTaskTest description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTaskOne);
        CommonTask commonTaskTwo = new CommonTask("Test removeAllCommonTaskTest"
                , "Test removeAllCommonTaskTest description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTaskTwo);

        taskManager.removeAllCommonTask();
        final ArrayList<CommonTask> commonTasksList = taskManager.returnAllCommonTask();
        assertEquals(0, commonTasksList.size(), "Спсиок задачь должен быть пустым");

    }

    @Test
    void removeAllSubtaskTest() {
        T taskManager = generateTaskManager();
        assertDoesNotThrow(() ->
                        taskManager.removeAllCommonTask()
                , "Удаление пустого списка задач");
        Epic epicTaskOne = new Epic("Test removeSubtaskTest"
                , "Test removeSubtaskTest description");
        taskManager.addEpicTask(epicTaskOne);
        Subtask subtaskOne = new Subtask("Test removeAllSubtaskTest"
                , "Test removeAllSubtaskTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Test removeAllSubtaskTest"
                , "Test removeAllSubtaskTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskTwo);

        taskManager.removeAllSubtask();
        final ArrayList<Subtask> subtasksList = taskManager.returnAllSubtask();
        assertEquals(0, subtasksList.size(), "Спсиок задачь должен быть пустым");

        final ArrayList<Integer> subtaskId = epicTaskOne.getSubtasksId();
        assertFalse(subtaskId.contains(subtaskOne.getIdTask()), "Подадачи в епики быть не должно");
        assertFalse(subtaskId.contains(subtaskTwo.getIdTask()), "Подадачи в епики быть не должно");

    }

    @Test
    void removeAllEpicTest() {
        T taskManager = generateTaskManager();
        assertDoesNotThrow(() ->
                        taskManager.removeAllEpic()
                , "Удаление пустого списка задач");
        Epic epicTaskOne = new Epic("Test removeAllEpicTest"
                , "Test removeAllEpicTest description");
        taskManager.addEpicTask(epicTaskOne);
        Epic epicTaskTwo = new Epic("Test removeAllEpicTest"
                , "Test removeAllEpicTest description");
        taskManager.addEpicTask(epicTaskTwo);
        Subtask subtaskOne = new Subtask("Test removeAllEpicTest"
                , "Test removeAllEpicTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Test removeAllEpicTest"
                , "Test removeAllEpicTest description", Status.DONE, 2);
        taskManager.addSubTask(subtaskTwo);
        taskManager.removeAllEpic();

        final ArrayList<Epic> epicList = taskManager.returnAllEpic();
        final ArrayList<Subtask> subtaskList = taskManager.returnAllSubtask();
        assertEquals(0, epicList.size(), "Спсиок задачь должен быть пустым");
        assertEquals(0, subtaskList.size(), "Спсиок подзадачь должен быть пустым");


    }

    @Test
    void returnTaskByIdTest() {
        T taskManager = generateTaskManager();
        assertThrows(NullPointerException.class
                , () -> taskManager.returnTaskById(2)
                , "Вызов несуществующей задачи у пустого списка");
        Epic epicTaskOne = new Epic("Test returnTaskByIdTest"
                , "Test returnTaskByIdTest description");
        taskManager.addEpicTask(epicTaskOne);
        Subtask subtaskOne = new Subtask("Test returnTaskByIdTest"
                , "Test returnTaskByIdTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskOne);
        CommonTask commonTask = new CommonTask("Test returnTaskByIdTest"
                , "Test returnTaskByIdTest description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTask);

        assertEquals(epicTaskOne, taskManager.returnTaskById(epicTaskOne.getIdTask())
                , "Задачи не соответвуют");
        assertEquals(subtaskOne, taskManager.returnTaskById(subtaskOne.getIdTask())
                , "Задачи не соответвуют");
        assertEquals(commonTask, taskManager.returnTaskById(commonTask.getIdTask())
                , "Задачи не соответвуют");

        assertThrows(NullPointerException.class
                , () -> taskManager.returnTaskById(22)
                , "Вызов несуществующей задачи,или задачи с неверным id");
    }

    @Test
    void updateCommonTaskTask() {
        T taskManager = generateTaskManager();
        CommonTask commonTask = new CommonTask("Test updateCommonTaskTask"
                , "Test updateCommonTaskTask description", Status.NEW,"12:30","90");
        taskManager.addCommonTask(commonTask);
        CommonTask commonTaskUpdate = new CommonTask(1, "Update"
                , "Update", Status.NEW);

        assertDoesNotThrow(() ->
                        taskManager.updateCommonTask(commonTaskUpdate)
                , "Изменение задачи котрой нет, просто добавляет ее");

        taskManager.updateCommonTask(commonTaskUpdate);
        final ArrayList<CommonTask> commonTaskList = taskManager.returnAllCommonTask();
        assertNotEquals(commonTaskUpdate, commonTask, "Задачи одинаковые");
        assertEquals(commonTask.getIdTask(), commonTaskUpdate.getIdTask(), "У задач разные id");
        assertEquals(1, commonTaskList.size(), "Добавилась лишняя задача");


        CommonTask commonTaskUpdate2 = new CommonTask(2, "Update"
                , "Update", Status.NEW);
        taskManager.addCommonTask(commonTask);
        final ArrayList<CommonTask> commonTaskListNew = taskManager.returnAllCommonTask();
        taskManager.updateCommonTask(commonTaskUpdate2);
        assertEquals(2, commonTaskListNew.size(), "при неверном id список не добавил задачу");


    }

    @Test
    void updateSubtaskTaskUpdateTest() {
        T taskManager = generateTaskManager();
        Epic epicTaskOne = new Epic("Test updateSubtaskTaskUpdateTest"
                , "Test updateSubtaskTaskUpdateTest description");
        Subtask subtaskOne = new Subtask("Test updateSubtaskTaskUpdateTest"
                , "Test updateSubtaskTaskUpdateTest description", Status.DONE, 1);
        Subtask subtaskOneUpdate = new Subtask(2, "Test Update"
                , "Test Update description", Status.DONE, 1);
        assertThrows(NullPointerException.class
                , () -> taskManager.updateSubtaskTask(subtaskOneUpdate)
                , "При обновлении задачи которая не добавлена в список или задача с неверным id должен быть NPE");
        taskManager.addEpicTask(epicTaskOne);
        taskManager.addSubTask(subtaskOne);
        taskManager.updateSubtaskTask(subtaskOneUpdate);
        assertNotEquals(subtaskOneUpdate, subtaskOne, "Задачи одинаковые");
        assertEquals(subtaskOneUpdate.getIdTask(), subtaskOne.getIdTask(), "У задач разные id");
        final ArrayList<Subtask> subTaskList = taskManager.returnAllSubtask();
        assertEquals(1, subTaskList.size(), "Добавилась лишняя задача");
        Subtask subtaskOneUpdateNew = new Subtask(55, "Test Update"
                , "Test Update description", Status.DONE, 1);
        final ArrayList<Subtask> epicTaskListNew = taskManager.returnAllSubtask();
        taskManager.updateSubtaskTask(subtaskOneUpdateNew);
        assertEquals(epicTaskListNew.size(), 1
                , "При обновлении задачи с неверным id, она не добавилась в список ");

    }

    @Test
    void updateEpicCommonTaskTest() {
        T taskManager = generateTaskManager();

        Epic epicTaskOne = new Epic("Test updateEpicCommonTaskTest"
                , "Test updateEpicCommonTaskTest description");
        Epic epicTaskOneUpdate = new Epic(1, "Test Update"
                , "Test Update description");

        taskManager.addEpicTask(epicTaskOne);
        assertNotEquals(epicTaskOneUpdate, epicTaskOne, "Задачи одинаковые");
        taskManager.updateEpicCommonTask(epicTaskOneUpdate);
        assertEquals(epicTaskOneUpdate.getIdTask(), epicTaskOne.getIdTask(), "У задач разные id");
        final ArrayList<Epic> epicTaskList = taskManager.returnAllEpic();
        assertEquals(1, epicTaskList.size(), "Добавилась лишняя задача");

        Epic epicTaskOneUpdateNew = new Epic(4, "Test Update2"
                , "Test Update2 description");
        assertThrows(NullPointerException.class
                , () -> taskManager.updateEpicCommonTask(epicTaskOneUpdateNew)
                , "При обновлении задачи которая не в списке или задача с неверным id должен быть NPE");


    }

    @Test
    void returnTaskByEpicTest() {
        T taskManager = generateTaskManager();
        Epic epicTaskOne = new Epic("Test returnTaskByEpicTest"
                , "Test returnTaskByEpicTest description");
        taskManager.addEpicTask(epicTaskOne);
        Subtask subtaskOne = new Subtask("Test returnTaskByEpicTest"
                , "Test returnTaskByEpicTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Test returnTaskByEpicTest"
                , "Test returnTaskByEpicTest description", Status.DONE, 1);
        taskManager.addSubTask(subtaskTwo);

        assertThrows(NullPointerException.class
                , () -> taskManager.returnTaskByEpic(44)
                , "При вызове задачи которая не в списке или задача с неверным id должен быть NPE");

        final ArrayList<AbstractTask> subtaskList = taskManager.returnTaskByEpic(1);
        final List<Subtask> subtaskListBasic = List.of(subtaskOne, subtaskTwo);
        assertEquals(subtaskListBasic.get(0), subtaskList.get(0), "Задачи не равны");
        assertEquals(subtaskListBasic.get(1), subtaskList.get(1), "Задачи не равны");
    }

}