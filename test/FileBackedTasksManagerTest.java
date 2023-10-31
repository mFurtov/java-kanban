import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.app.model.Epic;
import ru.yandex.app.service.FileBackedTasksManager;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private FileBackedTasksManager fileBackedTasksManager;

    FileBackedTasksManagerTest() {
        this.fileBackedTasksManager = new FileBackedTasksManager();
    }

    @BeforeEach
    void startTest() {
        fileBackedTasksManager = new FileBackedTasksManager();
    }

    @Override
    protected FileBackedTasksManager generateTaskManager() {
        return new FileBackedTasksManager();

    }

    @Test
    void saveEmptyTaskList() {
        fileBackedTasksManager.save();
        FileBackedTasksManager load = FileBackedTasksManager.loadFromFile("task.csv");
        assertTrue(load.returnAllCommonTask().isEmpty(), "Список обычных задач не должен содержать ничего");
        assertTrue(load.returnAllEpic().isEmpty(), "Список епиков не должен содержать ничего");
        assertTrue(load.returnAllSubtask().isEmpty(), "Список подзадач не должен содержать ничего");

    }

    @Test
    void saveOneEpicTest() {
        Epic epic = new Epic("Test Epic", "Description of Test Epic");
        fileBackedTasksManager.addEpicTask(epic);
        fileBackedTasksManager.save();
        fileBackedTasksManager.returnAllTask();
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile("task.csv");
        assertEquals(1, loadedManager.returnAllEpic().size()
                , "Список эпиков должен содержать один элемент.");
        assertEquals(0, loadedManager.returnAllSubtask().size(), "Список подзадач должен быть пустым.");
    }

    @Test
    void saveAndLoadEmptyHistoryTest() {
        fileBackedTasksManager.save();
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile("task.csv");
        assertTrue(loadedManager.getHistoryManager().getHistory().isEmpty()
                , "Список истории не должен ничего содержать");
    }

}