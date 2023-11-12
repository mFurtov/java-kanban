package ru.yandex.app.service;

public class RegisterClientException extends RuntimeException{

    public RegisterClientException() {
        super("Ошибка при регистрации на сервере KV");
    }
}
