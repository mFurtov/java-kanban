package ru.yandex.app.service;

public class IntersectionTimeException extends RuntimeException{

    public IntersectionTimeException(String massage) {
        super(massage);
    }
}
