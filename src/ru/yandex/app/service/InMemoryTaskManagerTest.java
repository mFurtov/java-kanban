package ru.yandex.app.service;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager generateTaskManager() {
        return new InMemoryTaskManager();
    }
}