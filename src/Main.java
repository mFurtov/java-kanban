import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();
        CommonTask commonTask1 = new CommonTask("Сходить на почту", "получить поссылку из деревни", "IN_PROGRESS");
        manager.addCommonTask(commonTask1);
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты", "IN_PROGRESS");
        manager.addCommonTask(commonTask2);
        Epic epic1 = new Epic("Купить костюм на свадьбу", "Нужно собрать костюм на свадьбу друга");
        manager.addEpicTask(epic1);
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер", "nw", 3);
        manager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой", "Нужно собрать костюм на свадьбу друга", "new", 3);
        manager.addSubTask(subtask2);
        Epic epic2 = new Epic("Приготовить яичницу", "Нужно позавтракать");
        manager.addEpicTask(epic2);
        Subtask subtask3 = new Subtask("Разбить яйца", "Разбить 2 яйца", "done", 6);
        manager.addSubTask(subtask3);

        System.out.println("Список обычных задач");
        System.out.println(manager.returnAllCommonTask());
        System.out.println();
        System.out.println("Список подзадач задач");
        System.out.println(manager.returnAllSubtask());
        System.out.println();
        System.out.println("Список Эпиков");
        System.out.println(manager.returnAllEpic());
        System.out.println();
        ;
        System.out.println("До удаления задачи");
        System.out.println(manager.returnAllTask());
        System.out.println();
        manager.removeById(2);
        System.out.println("После удаления задачи");
        System.out.println(manager.returnAllTask());
        System.out.println();
        System.out.println("До удаления эпика");
        System.out.println(manager.returnAllTask());
        System.out.println();
        System.out.println("После удаления эпика");
        manager.removeById(5);
        System.out.println(manager.returnAllTask());
        System.out.println();
        System.out.println("До обновления подзадачи");
        System.out.println(manager.returnAllTask());
        Subtask subtask3new = new Subtask(7, "Разбить яйца", "Разбить 4 яйца", "new", 6);
        manager.updateSubtaskTask(subtask3new);
        System.out.println();
        System.out.println("После обновления подзадачи");
        System.out.println(manager.returnAllTask());

    }
}
