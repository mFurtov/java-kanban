package ru.yandex.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.app.model.AbstractTask;
import ru.yandex.app.model.CommonTask;
import ru.yandex.app.model.Epic;
import ru.yandex.app.model.Status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void startTest(){
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addTaskToHistoryTest() {
        Epic epicTask = new Epic("Test addTaskToHistory"
                , "Test addTaskToHistory description");
        historyManager.add(epicTask);
        assertEquals(historyManager.getHistory().size(),1,"Задача не одна или ее нет");
        assertEquals(historyManager.getHistory().get(0),epicTask,"Задачи разные");

    }
    @Test
    void addTaskToHistoryLimitTest() {
        CommonTask oldTask = new CommonTask(1,"Test addTaskToHistoryLimit"
                ,"Test addTaskToHistory description", Status.NEW);
        for(int i =2; i <= 10;i++){
            CommonTask commonTask = new CommonTask(i,"Test addTaskToHistoryLimit"
                    ,"Test addTaskToHistory description", Status.NEW);
            historyManager.add(commonTask);
        }
        CommonTask commonTaskNew = new CommonTask(11,"Test addTaskToHistoryLimit"
                ,"Test addTaskToHistory description", Status.NEW);
        historyManager.add(commonTaskNew);

        assertFalse(historyManager.getHistory().contains(oldTask),"Старая задача не удалена");
        assertEquals(10, historyManager.getHistory().size()
                ,"История привысила максимальное занчение ");
        assertTrue(historyManager.getHistory().contains(commonTaskNew)
        ,"Новая задача не добавлена");

    }
    @Test
    public void addTaskTwiceTest(){
        CommonTask commonTaskNew = new CommonTask(11,"Test addTaskTwiceTest"
                ,"Test addTaskTwiceTest description", Status.NEW);
        historyManager.add(commonTaskNew);
        historyManager.add(commonTaskNew);
        assertEquals(1, historyManager.getHistory().size(),"Задачи дублируются");
    }

    @Test
    void getHistoryEmptyTest() {
        assertTrue(historyManager.getHistory().isEmpty(),"При пустой истории список не пуст");
    }

    @Test
    void removeTest() {
        CommonTask commonTask = new CommonTask(1,"Test addTaskTwiceTest"
                ,"Test addTaskTwiceTest description", Status.NEW);
        historyManager.add(commonTask);

        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty(),"Задача не удалена из истории");

    }

    @Test
    void removeMiddleTest() {
        CommonTask commonTaskNew = new CommonTask(1,"Test removeMiddleTest"
                ,"Test removeMiddleTest description", Status.NEW);
        CommonTask commonTaskNew2 = new CommonTask(2,"Test removeMiddleTest"
                ,"Test removeMiddleTest description", Status.NEW);
        CommonTask commonTaskNew3 = new CommonTask(3,"Test removeMiddleTest"
                ,"Test removeMiddleTest description", Status.NEW);
        historyManager.add(commonTaskNew);
        historyManager.add(commonTaskNew2);
        historyManager.add(commonTaskNew3);

        historyManager.remove(2);
        assertFalse(historyManager.getHistory().contains(commonTaskNew2),"Задача не удалена из центра");

    }
    @Test
    void removeEndTest() {
        CommonTask commonTaskNew = new CommonTask(1,"Test removeEndTest"
                ,"Test removeEndTest description", Status.NEW);
        CommonTask commonTaskNew2 = new CommonTask(2,"Test removeEndTest"
                ,"Test removeEndTest description", Status.NEW);
        CommonTask commonTaskNew3 = new CommonTask(3,"Test removeEndTest"
                ,"Test removeEndTest description", Status.NEW);
        historyManager.add(commonTaskNew);
        historyManager.add(commonTaskNew2);
        historyManager.add(commonTaskNew3);
        historyManager.remove(3);

        assertEquals(2, historyManager.getHistory().size(),"Список задач не изменился ");
        assertFalse(historyManager.getHistory().contains(commonTaskNew3),"Задача не удалена из истории");
    }
    @Test
    void removeNotExistingTest() {
        CommonTask commonTaskNew = new CommonTask(1,"Test removeNotExistingTest"
                ,"Test removeNotExistingTest description", Status.NEW);
        CommonTask commonTaskNew2 = new CommonTask(2,"Test removeNotExistingTest"
                ,"Test removeNotExistingTest description", Status.NEW);
        CommonTask commonTaskNew3 = new CommonTask(3,"Test removeMiddleTest"
                ,"Test removeNotExistingTest description", Status.NEW);
        historyManager.add(commonTaskNew);
        historyManager.add(commonTaskNew2);
        historyManager.add(commonTaskNew3);

        historyManager.remove(5);

        assertEquals(3, historyManager.getHistory().size(),"что-то было удалено");

    }
}