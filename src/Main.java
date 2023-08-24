import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager =new Manager();
        CommonTask commonTask1 = new CommonTask("Купить хлеб"
                ,"Отправиться в магазин и купиь свежий хлеб","in_progress");
        manager.addCommonTask(commonTask1);
        System.out.println(manager.commonTaskMap);
        System.out.println();
        Subtask subtask = new Subtask(" Оторжить денег"
                ,"Положить деньги в копилку","new",0);
        manager.addSubTask(subtask);
        System.out.println(manager.subTaskMap);
        System.out.println();
        CommonTask commonTask2 = new CommonTask("Сделать яичницу"
                ,"Пригтовить завтрак из двух яиц и бекона","done");
        manager.addCommonTask(commonTask2);
        Subtask subtask2 = new Subtask("Выбрать марку"
                ,"Почтитать отзовы","new",0);
        manager.addSubTask(subtask2);
        System.out.println(manager.subTaskMap);
        System.out.println();
        ArrayList<Integer> numbTaskInEpbic = new ArrayList<>();
        numbTaskInEpbic.add(2);
        numbTaskInEpbic.add(4);
        Epic epic = new Epic("Купить машину"
                ,"Купить поддрежаную машину",numbTaskInEpbic);
        manager.addEpicTask(epic);
        System.out.println(manager.epicTaskMap);
        System.out.println();
        System.out.println(manager.subTaskMap);
        System.out.println("______");
        Subtask subtask3 = new Subtask("Выбрать модель"
                ,"Необходимо понять какую модель надо купить","new",5);
        manager.addSubTask(subtask3);
        System.out.println(manager.subTaskMap);
        System.out.println();
        System.out.println(manager.epicTaskMap);
        System.out.println();

    }
}
