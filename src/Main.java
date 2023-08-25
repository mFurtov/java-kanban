import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        //Обычные таски
        System.out.println("ОБычне таски");
        CommonTask commonTask1 = new CommonTask("Купить хлеб"
                , "Отправиться в магазин и купиь свежий хлеб", "in_progress");
        manager.addCommonTask(commonTask1);
        System.out.println(manager.commonTaskMap);
        System.out.println();
        CommonTask commonTask2 = new CommonTask("Зайти на почту "
                , "получить письмо", "in_progress");
        manager.addCommonTask(commonTask2);
        System.out.println(manager.commonTaskMap);
        System.out.println();

        //Сабтаски
        System.out.println(" Сабтаски");
        Subtask subtask = new Subtask(" Оторжить денег"
                , "Положить деньги в копилку", "done", 0);
        manager.addSubTask(subtask);
        System.out.println(manager.subTaskMap);
        System.out.println();

        //Дообавил обычный таск для проверки что ид меняться
        CommonTask commonTask3 = new CommonTask("Сделать яичницу"
                ,"Пригтовить завтрак из двух яиц и бекона","done");
        manager.addCommonTask(commonTask3);
        Subtask subtask2 = new Subtask("Выбрать марку"
                ,"Почтитать отзовы","done",0);
        manager.addSubTask(subtask2);
        System.out.println(manager.subTaskMap);
        System.out.println();

        //дообоаляю эпики и лист с подзадачами
        ArrayList<Integer> numbTaskInEpbic1 = new ArrayList<>();
        numbTaskInEpbic1.add(3);
        numbTaskInEpbic1.add(5);
        Epic epic = new Epic("Купить машину"
                ,"Купить поддрежаную машину",numbTaskInEpbic1);
        manager.addEpicTask(epic);
        System.out.println("Вывожу эпик");
        System.out.println(manager.epicTaskMap);
        System.out.println();
        System.out.println("Вывожу сабтаски epicid меняться");
        System.out.println(manager.subTaskMap);
        Subtask subtask3 = new Subtask("Выбрать модель"
                , "Необходимо понять какую модель надо купить", "done", 6);
        manager.addSubTask(subtask3);
        System.out.println(manager.subTaskMap);

        System.out.println("!!!!!!!!!!!!!!!!!!");

        Subtask subtask4 = new Subtask("Тест сабсамк 1"
                ,"тест","new",0);
        manager.addSubTask(subtask4);
        Subtask subtask5 = new Subtask("Тест сабсамк 2"
                ,"текст","new",0);
        manager.addSubTask(subtask5);
        System.out.println(manager.subTaskMap);
        System.out.println();
        ArrayList<Integer> numbTaskInEpbic2 = new ArrayList<>();
        numbTaskInEpbic2.add(8);
        numbTaskInEpbic2.add(9);
        Epic epic2 = new Epic("тестовы епик"
                ,"текст",numbTaskInEpbic2);
        manager.addEpicTask(epic2);
        System.out.println(manager.subTaskMap);
        Subtask subtask6 = new Subtask("Заказать историю ДТП"
                ,"Проверить была ли машина дтп","new",6);
        manager.addSubTask(subtask6);

        Subtask subtask7 = new Subtask("Тест сабсамк 3"
                ,"текст","new",10);


        manager.addSubTask(subtask7);
        System.out.println(manager.subTaskMap);
        System.out.println(manager.epicTaskMap);
        System.out.println("____________________________________________________________________!!!!!!");


        System.out.println("Выводими все таски");
        System.out.println(manager.returnAllTask());
        System.out.println();
        System.out.println("Проверяю меняться ли статус епиков");
        System.out.println(manager.epicTaskMap);
        System.out.println();
        System.out.println("Проdерка возврата по id");
        System.out.println(manager.returnTaskById(10));
        System.out.println(manager.returnTaskById(3));
        System.out.println(manager.returnTaskById(2));
        CommonTask commonTask2new = new CommonTask(2,"Зайти в сдэк "
                , "получить письмо", "done");
        manager.updateCommonTask(commonTask2new);
        System.out.println(manager.commonTaskMap);
        System.out.println(manager.returnTaskById(2));
        System.out.println(manager.epicTaskMap);
        System.out.println("222222222222222222222222222222222!!!!!!!");
        Subtask subtask8 = new Subtask(12," NEW Тест сабсамк 3"
                ,"текст","new",10);

        System.out.println(manager.epicTaskMap);
        System.out.println("!11");
        manager.updateSubtaskTask(subtask8);
        System.out.println(manager.epicTaskMap);
        System.out.println("!!!1");
        Subtask subtask9 = new Subtask("  Тест сабсамк 4"
                ,"текст","new",10);
        System.out.println("Добавил");
        manager.addSubTask(subtask9);
        System.out.println(manager.epicTaskMap);

//        Subtask subtask9 = new Subtask(12," NEW Тест сабсамк 4"
//                ,"текст","done",10);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        Epic epic2new = new Epic(10,"NEW тестовы епик"
                ,"текст",numbTaskInEpbic2);
        System.out.println(manager.epicTaskMap);
        manager.updateEpicCommonTask(epic2new);
        System.out.println(manager.epicTaskMap);
       // System.out.println(manager.subTaskMap);
    }
}
