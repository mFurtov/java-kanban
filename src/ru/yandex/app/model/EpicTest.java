package ru.yandex.app.model;

import org.junit.jupiter.api.Test;
import ru.yandex.app.service.InMemoryTaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void emptyTaskListTest() {
        Epic epic = new Epic(1, "Test emptyTaskListTest", "Test emptyTaskListTest");
        taskManager.addEpicTask(epic);
        final Epic checkEpic = taskManager.getEpicTaskMap().get(1);
        final ArrayList<Integer> epicSubtasksId = checkEpic.getSubtasksId();
        assertTrue(epicSubtasksId.isEmpty(), "Список подзадач не пустой");
    }

    @Test
    void allSubtasksWithNewStatusTest() {
        Epic epic = new Epic(1, "Test allSubtasksWithNewStatusTest"
                , "Test allSubtasksWithNewStatusTest description");
        taskManager.addEpicTask(epic);
        Subtask subtaskOne = new Subtask("Test allSubtasksWithNewStatusTest subtask"
                , "Test allSubtasksWithNewStatusTest subtask description", Status.NEW, 1);
        Subtask subtaskTwo = new Subtask("Test allSubtasksWithNewStatusTest subtask"
                , "Test allSubtasksWithNewStatusTest subtask description", Status.NEW, 1);
        taskManager.addSubTask(subtaskOne);
        taskManager.addSubTask(subtaskTwo);
        final Epic checkEpic = taskManager.getEpicTaskMap().get(1);
        assertEquals(checkEpic.getStatusTask(), Status.NEW, "Статус при новых сабатсках не в состоянии NEW");
    }

    @Test
    void allSubtasksWithDoneStatusTest() {
        Epic epic = new Epic(1, "Test allSubtasksWithDoneStatusTest"
                , "Test allSubtasksWithDoneStatusTest description");
        taskManager.addEpicTask(epic);
        Subtask subtaskOne = new Subtask("Test allSubtasksWithDoneStatusTest subtask"
                , "Test allSubtasksWithDoneStatusTest subtask description", Status.DONE, 1);
        Subtask subtaskTwo = new Subtask("Test allSubtasksWithDoneStatusTest subtask"
                , "Test allSubtasksWithDoneStatusTest subtask description", Status.DONE, 1);
        taskManager.addSubTask(subtaskOne);
        taskManager.addSubTask(subtaskTwo);
        final Epic checkEpic = taskManager.getEpicTaskMap().get(1);
        assertEquals(checkEpic.getStatusTask()
                , Status.DONE, "Статус при выполненных сабатсках не в состоянии DONE");
    }

    @Test
    void allSubtasksWithDoneAndNewStatusTest() {
        Epic epic = new Epic(1, "Test allSubtasksWithDoneAndNewStatusTest"
                , "Test allSubtasksWithDoneAndNewStatusTest description");
        taskManager.addEpicTask(epic);
        Subtask subtaskOne = new Subtask("Test allSubtasksWithDoneAndNewStatusTest subtask"
                , "Test allSubtasksWithDoneAndNewStatusTest subtask description", Status.DONE, 1);
        Subtask subtaskTwo = new Subtask("Test allSubtasksWithDoneAndNewStatusTest subtask"
                , "Test allSubtasksWithDoneAndNewStatusTest subtask description", Status.NEW, 1);
        taskManager.addSubTask(subtaskOne);
        taskManager.addSubTask(subtaskTwo);
        final Epic checkEpic = taskManager.getEpicTaskMap().get(1);
        assertEquals(checkEpic.getStatusTask(), Status.IN_PROGRESS
                , "Статус при новых и выполненных сабатсках не в состоянии DONE");
    }

    @Test
    void allSubtasksWithInProgressStatusTest() {
        Epic epic = new Epic(1, "Test allSubtasksWithInProgressStatusTest"
                , "Test allSubtasksWithInProgressStatusTest description");
        taskManager.addEpicTask(epic);
        Subtask subtaskOne = new Subtask("Test allSubtasksWithInProgressStatusTest subtask"
                , "Test allSubtasksWithInProgressStatusTest subtask description"
                , Status.IN_PROGRESS, 1);
        Subtask subtaskTwo = new Subtask("Test allSubtasksWithInProgressStatusTest subtask"
                , "Test allSubtasksWithInProgressStatusTest subtask description"
                , Status.IN_PROGRESS, 1);
        taskManager.addSubTask(subtaskOne);
        taskManager.addSubTask(subtaskTwo);
        final Epic checkEpic = taskManager.getEpicTaskMap().get(1);
        assertEquals(checkEpic.getStatusTask(), Status.IN_PROGRESS
                , "Статус при выполняющихся сабтасках не в состоянии IN_PROGRESS");
    }

    @Test
    void addVBasicEpcTest() {
        Epic epic = new Epic("Test addVBasicEpcTest"
                , "Test addVBasicEpcTest description");
        taskManager.addEpicTask(epic);
        assertTrue(taskManager.getEpicTaskMap().containsValue(epic), "Такого епика нет");

        final Epic checkEpic = taskManager.getEpicTaskMap().get(1);
        assertEquals(epic, checkEpic, "Эпики не совпадают");

        assertEquals(checkEpic.getStatusTask(), Status.NEW
                , "У нового епика задачи должны быть новые");

        final ArrayList<Integer> epicSubtasksId = checkEpic.getSubtasksId();
        assertTrue(epicSubtasksId.isEmpty(), "Список подзадач не пустой");

        Subtask subtaskOne = new Subtask("Test addVBasicEpcTest subtask"
                , "Test addVBasicEpcTest subtask description", Status.DONE, 1);
        taskManager.addSubTask(subtaskOne);
        assertFalse(epicSubtasksId.isEmpty(), "Список подзадач пустой");
        assertEquals(checkEpic.getStatusTask(), Status.DONE
                , "Статус при добавлении новой задачи должен измениться");

    }

}