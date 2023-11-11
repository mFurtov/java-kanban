package ru;

import ru.yandex.app.model.*;
import ru.yandex.app.service.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        Epic epic1 = new Epic("Купить костюм на свадьбу", "Нужно собрать костюм на свадьбу друга");
        httpTaskServer.getTaskManager().addEpicTask(epic1);
        Epic epic2 = new Epic("Купить костюм на свадьбу", "Нужно собрать костюм на свадьбу друга");
        httpTaskServer.getTaskManager().addEpicTask(epic2);
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер", Status.DONE, 1, "2021.10.23 11:30", "80");
        httpTaskServer.getTaskManager().addSubTask(subtask1);
        CommonTask commonTask1 = new CommonTask("Сходить на почту"
                , "получить поссылку из деревни"
                , Status.IN_PROGRESS, "2022.12.23 14:30", "80");
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты"
                , Status.IN_PROGRESS, "2022.10.23 13:30", "80");
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой"
                , "Нужно собрать костюм на свадьбу друга"
                , Status.NEW, 1, "2022.10.23 15:30", "80");


        httpTaskServer.getTaskManager().addSubTask(subtask2);
        httpTaskServer.getTaskManager().addCommonTask(commonTask1);
        httpTaskServer.getTaskManager().addCommonTask(commonTask2);
        httpTaskServer.getTaskManager().returnAllCommonTask();
    }
}
