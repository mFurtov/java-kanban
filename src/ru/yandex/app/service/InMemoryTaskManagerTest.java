package ru.yandex.app.service;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest  extends TaskManagerTest<InMemoryTaskManager>{

    @Override
    protected InMemoryTaskManager generateTaskManager() {
        return new InMemoryTaskManager();
    }
}