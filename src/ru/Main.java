package ru;

import ru.yandex.app.model.*;
import ru.yandex.app.service.FileBackedTasksManager;
import ru.yandex.app.service.Managers;
import ru.yandex.app.service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();
        CommonTask commonTask1 = new CommonTask("Сходить на почту"
                , "получить поссылку из деревни", Status.IN_PROGRESS,"12:30","90");
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты"
                , Status.IN_PROGRESS,"14:30","80");
        Epic epic1 = new Epic("Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер", Status.DONE, 1
                ,"17:30","70");
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW, 1,"20:20","300");
        Subtask subtask3 = new Subtask("Купить букет жениху и невесте"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW, 1,"20:02","90");
        Epic epic2 = new Epic("Приготовить яичницу", "Нужно позавтракать");
        Subtask subtask4 = new Subtask("Купить букет жениху и невесте"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW, 1,"09:02","90");
//        inMemoryTaskManager.addCommonTask(commonTask1);
//        inMemoryTaskManager.addCommonTask(commonTask2);
        inMemoryTaskManager.addEpicTask(epic1);
        inMemoryTaskManager.addSubTask(subtask1);
        inMemoryTaskManager.addSubTask(subtask2);
        inMemoryTaskManager.addSubTask(subtask3);
        inMemoryTaskManager.addSubTask(subtask4);
        inMemoryTaskManager.addEpicTask(epic2);
        System.out.println(epic1.getSubtaskInEpic());
        System.out.println(commonTask1.getEndTime());
        System.out.println(subtask2.getEndTime());
        System.out.println(epic1.getEndTime());

    }
}
