package ru.yandex.app.service;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String massage) {
        super(massage);
    }
}
