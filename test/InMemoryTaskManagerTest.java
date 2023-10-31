import ru.yandex.app.service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager generateTaskManager() {
        return new InMemoryTaskManager();
    }
}