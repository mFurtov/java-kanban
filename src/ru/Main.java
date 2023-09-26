package ru;

import ru.yandex.app.model.*;
import ru.yandex.app.service.Managers;
import ru.yandex.app.service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();
        CommonAbstractTask commonTask1 = new CommonAbstractTask("Сходить на почту"
                , "получить поссылку из деревни", Status.IN_PROGRESS);
        inMemoryTaskManager.addCommonTask(commonTask1);
        CommonAbstractTask commonTask2 = new CommonAbstractTask("Купить сыра", "пармезан для пасты"
                , Status.IN_PROGRESS);
        inMemoryTaskManager.addCommonTask(commonTask2);
        Epic epic1 = new Epic("Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        inMemoryTaskManager.addEpicTask(epic1);
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер", Status.DONE, 3);
        inMemoryTaskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW, 3);
        inMemoryTaskManager.addSubTask(subtask2);
        Subtask subtask3 = new Subtask("Купить букет жениху и невесте"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW, 3);
        inMemoryTaskManager.addSubTask(subtask3);
        Epic epic2 = new Epic("Приготовить яичницу", "Нужно позавтракать");
        inMemoryTaskManager.addEpicTask(epic2);

        System.out.println(inMemoryTaskManager.returnAllTask());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();
        inMemoryTaskManager.returnTaskById(4);
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();
        inMemoryTaskManager.returnTaskById(3);
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();
        inMemoryTaskManager.removeCommonTask(2);
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();
        inMemoryTaskManager.removeEpic(3);
        System.out.println(inMemoryTaskManager.getHistory());
    }
}
