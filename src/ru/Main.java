package ru;

import ru.yandex.app.model.CommonTask;
import ru.yandex.app.model.Epic;
import ru.yandex.app.service.InMemoryTaskManager;
import ru.yandex.app.model.Subtask;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        CommonTask commonTask1 = new CommonTask("Сходить на почту", "получить поссылку из деревни", "IN_PROGRESS");
        inMemoryTaskManager.addCommonTask(commonTask1);
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты", "IN_PROGRESS");
        inMemoryTaskManager.addCommonTask(commonTask2);
        Epic epic1 = new Epic("Купить костюм на свадьбу", "Нужно собрать костюм на свадьбу друга");
        inMemoryTaskManager.addEpicTask(epic1);
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер", "done", 3);
        inMemoryTaskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой", "Нужно собрать костюм на свадьбу друга", "done", 3);
        inMemoryTaskManager.addSubTask(subtask2);
        Epic epic2 = new Epic("Приготовить яичницу", "Нужно позавтракать");
        inMemoryTaskManager.addEpicTask(epic2);
        Subtask subtask3 = new Subtask("Разбить яйца", "Разбить 2 яйца", "done", 6);
        inMemoryTaskManager.addSubTask(subtask3);

        System.out.println("Список обычных задач");
        System.out.println(inMemoryTaskManager.returnAllCommonTask());
        System.out.println();
        System.out.println("Список подзадач задач");
        System.out.println(inMemoryTaskManager.returnAllSubtask());
        System.out.println();
        System.out.println("Список Эпиков");
        System.out.println(inMemoryTaskManager.returnAllEpic());
        System.out.println();
        System.out.println("До удаления задачи");
        System.out.println(inMemoryTaskManager.returnAllTask());
        System.out.println();
        //manager.removeCommonTask(2);
        System.out.println("После удаления задачи");
        System.out.println(inMemoryTaskManager.returnAllTask());
        System.out.println();
        System.out.println("До удаления эпика");
        System.out.println(inMemoryTaskManager.returnAllTask());
        System.out.println();
        System.out.println("После удаления эпика");
        //manager.removeEpic(3);
        System.out.println(inMemoryTaskManager.returnAllTask());
        System.out.println();
        System.out.println("До обновления подзадачи");
        System.out.println(inMemoryTaskManager.returnAllTask());
        Subtask subtask3new = new Subtask(7, "Разбить яйца", "Разбить 4 яйца", "new", 6);
        inMemoryTaskManager.updateSubtaskTask(subtask3new);
        System.out.println();
        System.out.println("После обновления подзадачи");
        System.out.println(inMemoryTaskManager.returnAllTask());
    }
}
