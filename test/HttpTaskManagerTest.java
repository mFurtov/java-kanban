import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.app.model.*;
import ru.yandex.app.service.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private final Gson gson = Managers.getDefaultGson();
    KVServer kvServer;
    Epic epic;
    Subtask subtask;
    CommonTask commonTask;

    @BeforeEach
    void init() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        taskManager = httpTaskServer.getTaskManager();
        epic = new Epic("Купить костюм на свадьбу", "Нужно собрать костюм на свадьбу друга");
        subtask = new Subtask("Купить обувь", "45 размер", Status.NEW, 1
                , "2021.10.23 11:30", "80");
        commonTask = new CommonTask("Сходить на почту"
                , "получить поссылку из деревни"
                , Status.IN_PROGRESS, "2022.12.23 14:30", "80");
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subtask);
        taskManager.addCommonTask(commonTask);
        taskManager.returnTaskById(3);
        taskManager.returnTaskById(1);

    }

    @AfterEach
    void end() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void testGetEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());

        Type taskType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        ArrayList<Epic> epicList = gson.fromJson(response.body(), taskType);
        assertNotNull(epicList, "Задача не возвращается");
        assertEquals(1, epicList.size(), "Задачь вернулось не столько, сколько ожидалось");
        assertTrue(epicList.contains(epic));
    }

    @Test
    void testGetSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());

        Type taskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Epic> subtaskList = gson.fromJson(response.body(), taskType);
        assertNotNull(subtaskList, "Задача не возвращается");
        assertEquals(1, subtaskList.size(), "Задачь вернулось не столько, сколько ожидалось");
        assertTrue(subtaskList.contains(subtask), "Список содержит неверные задачи");
    }

    @Test
    void testGetTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());

        Type taskType = new TypeToken<ArrayList<CommonTask>>() {
        }.getType();
        ArrayList<CommonTask> taskList = gson.fromJson(response.body(), taskType);
        assertNotNull(taskList, "Задача не возвращается");
        assertEquals(1, taskList.size(), "Задачь вернулось не столько, сколько ожидалось");
    }

    @Test
    void testGetTaskPriority() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> priorityList = gson.fromJson(response.body(), taskType);
        assertNotNull(priorityList, "Задача не возвращается");
        assertEquals(2, priorityList.size(), "Задачь вернулось не столько, сколько ожидалось");

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(subtask);
        expectedList.add(commonTask);
        assertEquals(expectedList, priorityList, "Список содержит неверные задачи");


    }

    @Test
    void testGetEpicById() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> subtaskList = gson.fromJson(response.body(), taskType);
        assertNotNull(subtaskList, "Задача не возвращается");
        assertEquals(1, subtaskList.size(), "Задачь вернулось не столько, сколько ожидалось");
        assertTrue(subtaskList.contains(subtask), "Вернулась другая задача");

    }

    @Test
    void testTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task task = gson.fromJson(response.body(), taskType);
        assertNotNull(task, "Задача не возвращается");
        assertEquals(task, subtask, "Вернулась другая задача");

    }

    @Test
    void testGetHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> historyList = gson.fromJson(response.body(), taskType);
        assertNotNull(historyList, "Задача не возвращается");
        assertEquals(2, historyList.size(), "Задачь вернулось не столько, сколько ожидалось");

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(epic);
        expectedList.add(commonTask);
        assertEquals(expectedList, historyList, "Список содержит неверные задачи");


    }

    @Test
    void testPostEpicAdd() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = "{" +
                "\"nameTask\": \"testPostEpic\"," +
                "\"descriptionTask\": \"testPostEpic\"" +
                "}";
        Epic epicAdd = new Epic(4, "testPostEpic", "testPostEpic");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertEquals(2, taskManager.returnAllEpic().size()
                , "Задачь вернулось не столько, сколько ожидалось");

        assertEquals(epicAdd,taskManager.returnAllEpic().get(1),"Задачи не соответствуют");
    }

     @Test
    void testPostEpicUpdate() throws IOException, InterruptedException {
         HttpClient client = HttpClient.newHttpClient();
         URI url = URI.create("http://localhost:8080/tasks/epic/");
         String json = "{" +
                 "\"idTask\": \"1\","+
                 "\"nameTask\": \"testPostEpic\"," +
                 "\"descriptionTask\": \"testPostEpic\"" +
                 "}";

         Epic epicAdd = new Epic(1, "testPostEpic", "testPostEpic");
         HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
         HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
         assertEquals(1, taskManager.returnAllEpic().size()
                 , "Задачь вернулось не столько, сколько ожидалось");

         assertEquals(epicAdd,taskManager.returnAllEpic().get(0),"Задачи не соответствуют");
        }

@Test
void testPostTaskAdd() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create("http://localhost:8080/tasks/task/");
    String json = "{" +
            "\"nameTask\": \"testPostTaskAdd\"," +
            "\"descriptionTask\": \"testPostTaskAdd\"," +
            "\"statusTask\": \"NEW\"," +
            "\"startTime\": \"2022.10.23 13:30\"," +
            "\"duration\": \"80\"" +
            "}";
    CommonTask taskAdd = new CommonTask( 4,"testPostTaskAdd"
            , "testPostTaskAdd",Status.NEW,"2022.10.23 13:30","80");
    HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
    HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
    assertEquals(2, taskManager.returnAllCommonTask().size()
            , "Задачь вернулось не столько, сколько ожидалось");

   assertEquals(taskAdd,taskManager.returnAllCommonTask().get(1),"Задачи не соответствуют");
}

    @Test
    void testPostTaskUpdate() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = "{" +
                "\"idTask\": \"3\"," +
                "\"nameTask\": \"testPostTaskUpdate\"," +
                "\"descriptionTask\": \"testPostTaskUpdate\"," +
                "\"statusTask\": \"NEW\"," +
                "\"startTime\": \"2022.10.23 13:30\"," +
                "\"duration\": \"80\"" +
                "}";

        CommonTask taskAdd = new CommonTask( 3,"testPostTaskUpdate"
                , "testPostTaskUpdate",Status.NEW,"2022.10.23 13:30","80");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertEquals(1, taskManager.returnAllCommonTask().size()
                , "Задачь вернулось не столько, сколько ожидалось");
        assertEquals(taskAdd,taskManager.returnAllCommonTask().get(0));
    }
    @Test
    void testPostSubtaskAdd() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = "{" +
                "\"nameTask\": \"testPostTaskAdd\"," +
                "\"descriptionTask\": \"testPostTaskAdd\"," +
                "\"epicId\": \"1\"," +
                "\"statusTask\": \"DONE\"," +
                "\"startTime\": \"2022.10.23 13:30\"," +
                "\"duration\": \"80\"" +
                "}";
        Subtask taskAdd = new Subtask( 4,"testPostTaskAdd"
                , "testPostTaskAdd",Status.DONE,1,"2022.10.23 13:30","80");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertEquals(2, taskManager.returnAllSubtask().size()
                , "Задачь вернулось не столько, сколько ожидалось");

        assertEquals(taskAdd,taskManager.returnAllSubtask().get(1),"Задачи не соответствуют");
    }

    @Test
    void testPostSubtaskUpdate() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = "{" +
                "\"idTask\": \"2\"," +
                "\"nameTask\": \"testPostTaskAdd\"," +
                "\"descriptionTask\": \"testPostTaskAdd\"," +
                "\"epicId\": \"1\"," +
                "\"statusTask\": \"DONE\"," +
                "\"startTime\": \"2022.10.23 13:30\"," +
                "\"duration\": \"80\"" +
                "}";

        Subtask subtaskUpdate = new Subtask( 2,"testPostTaskAdd"
                , "testPostTaskAdd",Status.DONE,1,"2022.10.23 13:30","80");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertEquals(1, taskManager.returnAllSubtask().size()
                , "Задачь вернулось не столько, сколько ожидалось");
        System.out.println(taskManager.returnAllSubtask());
        assertEquals(subtaskUpdate,taskManager.returnAllSubtask().get(0));
        System.out.println(taskManager.returnAllCommonTask());
    }
     @Test
    void testDeleteEpic() throws IOException, InterruptedException {
         HttpClient client = HttpClient.newHttpClient();
         URI uri = URI.create("http://localhost:8080/tasks/epic/?id=1");
         HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
         assertTrue(taskManager.returnAllEpic().isEmpty(), "Список эпиков должен быть пустым");
         assertEquals(1,taskManager.returnAllTask().size(), "Задачь вернулось не столько, сколько ожидалось");
    }
    @Test
    void testTaskEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertTrue(taskManager.returnAllCommonTask().isEmpty(), "Список эпиков должен быть пустым");
        assertEquals(2,taskManager.returnAllTask().size(), "Задачь вернулось не столько, сколько ожидалось");
    }
    @Test
    void testSubtaskEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertTrue(taskManager.returnAllSubtask().isEmpty(), "Список эпиков должен быть пустым");
        assertEquals(2,taskManager.returnAllTask().size(), "Задачь вернулось не столько, сколько ожидалось");
    }
    @Test
    void testDeleteAllTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertTrue(taskManager.returnAllCommonTask().isEmpty(), "Список эпиков должен быть пустым");
        assertEquals(2,taskManager.returnAllTask().size(), "Задачь вернулось не столько, сколько ожидалось");
    }
    @Test
    void testDeleteAllEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertTrue(taskManager.returnAllEpic().isEmpty(), "Список эпиков должен быть пустым");
        assertEquals(1, taskManager.returnAllTask().size(), "Задачь вернулось не столько, сколько ожидалось");
    }
    @Test
    void testDeleteAllSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Запрос вернул ошибку " + response.statusCode());
        assertTrue(taskManager.returnAllSubtask().isEmpty(), "Список эпиков должен быть пустым");
        assertEquals(2, taskManager.returnAllTask().size(), "Задачь вернулось не столько, сколько ожидалось");
    }
    @Test
    void testLoadFromServer(){
        HttpTaskManager httpTaskManagerFromServer = new HttpTaskManager();
        httpTaskManagerFromServer.loadFromServer("http://localhost:8078");

        assertEquals(httpTaskManagerFromServer.returnTaskById(1)
                ,epic,"задача не соответствует той что должна была загрузиться");
        assertEquals(httpTaskManagerFromServer.returnTaskById(2)
                ,subtask,"задача не соответствует той что должна была загрузиться");
        assertEquals(httpTaskManagerFromServer.returnTaskById(3)
                ,commonTask,"задача не соответствует той что должна была загрузиться");
        assertTrue(httpTaskManagerFromServer.returnAllTask().size()==3,"Задач должно быть 3");

        System.out.println(httpTaskManagerFromServer.returnAllEpic());
    }

}


