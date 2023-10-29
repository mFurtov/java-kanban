package ru;

import ru.yandex.app.model.*;
import ru.yandex.app.service.Managers;
import ru.yandex.app.service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();
        CommonTask commonTask1 = new CommonTask("2022.10.23 12:30"
                , "получить поссылку из деревни", Status.IN_PROGRESS,"2022.10.23 12:30","90");
        CommonTask commonTask2 = new CommonTask("2022.10.23 14:30 сыра", "пармезан для пасты"
                , Status.IN_PROGRESS,"2022.10.23 14:30","80");
        Epic epic1 = new Epic("Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        Subtask subtask5 = new Subtask("2022.10.23 09:02 "
                , "Нужно собрать костюм на свадьбу друга", Status.NEW
                , 1,"2022.10.23 09:02","90");
        Subtask subtask1 = new Subtask("2022.09.23 17:30 ", "45 размер", Status.DONE, 1
                ,"2022.09.23 17:30","70");
        Subtask subtask2 = new Subtask(" 2022.10.23 20:20  брюки с рубашкой"
                , "2022.10.23 20:20 собрать костюм на свадьбу друга", Status.NEW
                , 1,"2022.10.23 20:20","300");
        Subtask subtask3 = new Subtask("2022.10.23 20:10"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW
                , 1,"2022.10.23 20:10","90");
        Epic epic2 = new Epic("Приготовить яичницу", "Нужно позавтракать");
        Subtask subtask4 = new Subtask("09:02 букет жениху и невесте"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW
                , 1,"2022.10.23 09:02","90");

        inMemoryTaskManager.addEpicTask(epic1);
        inMemoryTaskManager.addEpicTask(epic2);
        inMemoryTaskManager.addCommonTask(commonTask1);
        inMemoryTaskManager.addCommonTask(commonTask2);
        inMemoryTaskManager.addSubTask(subtask1);
        inMemoryTaskManager.addSubTask(subtask2);
        inMemoryTaskManager.addSubTask(subtask3);
        inMemoryTaskManager.addSubTask(subtask4);
        epic1.getEndTime();
        System.out.println(inMemoryTaskManager.getPrioritizedTasks());
    }
}
