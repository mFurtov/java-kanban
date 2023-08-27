import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();
        CommonTask commonTask1 = new CommonTask("Сходить на почту", "получить поссылку из деревни", "IN_PROGRESS");
        manager.addCommonTask(commonTask1);
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты", "IN_PROGRESS");
        manager.addCommonTask(commonTask2);
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер", "done", 0);
        manager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой", "Нужно собрать костюм на свадьбу друга", "done", 0);
        manager.addSubTask(subtask2);
        ArrayList<Integer> epicSubtask1 = new ArrayList<>();
        epicSubtask1.add(3);
        epicSubtask1.add(4);
        Epic epic1 = new Epic("Купить костюм на свадьбу", "Нужно собрать костюм на свадьбу друга", epicSubtask1);
        manager.addEpicTask(epic1);
        Subtask subtask3 = new Subtask("Разбить яйца", "Разбить 2 яйца", "done", 0);
        manager.addSubTask(subtask3);
        ArrayList<Integer> epicSubtask2 = new ArrayList<>();
        epicSubtask2.add(6);
        Epic epic2 = new Epic("Приготовить яичницу", "Нужно позавтракать", epicSubtask2);
        manager.addEpicTask(epic2);
        System.out.println("Список обычных задач");
        System.out.println(manager.getCommonTaskMap());
        System.out.println();
        System.out.println("Список подзадач задач");
        System.out.println(manager.getSubTaskMap());
        System.out.println();
        System.out.println("Список Эпиков");
        System.out.println(manager.getEpicTaskMap());
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
        Subtask subtask3new = new Subtask(6, "Разбить яйца", "Разбить 4 яйца", "IN_PROGRESS", 7);
        manager.updateSubtaskTask(subtask3new);
        System.out.println();
        System.out.println("После обновления подзадачи");
        System.out.println(manager.returnAllTask());
    }
}
