package ru.yandex.app.service;

import ru.yandex.app.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public void addCommonTask(CommonTask commonTask) {
        super.addCommonTask(commonTask);
        save();
    }

    public void addEpicTask(Epic epicTask) {
        super.addEpicTask(epicTask);
        save();
    }

    public void addSubTask(Subtask subtask) {
        super.addSubTask(subtask);
        save();
    }

    public ArrayList<Epic> returnAllEpic() {
        ArrayList<Epic> allEpic = super.returnAllEpic();
        save();
        return allEpic;

    }

    public ArrayList<CommonTask> returnAllCommonTask() {
        ArrayList<CommonTask> allCommonTask = super.returnAllCommonTask();
        save();
        return allCommonTask;

    }

    public ArrayList<Subtask> returnAllSubtask() {
        ArrayList<Subtask> allSubtask = super.returnAllSubtask();
        save();
        return allSubtask;
    }

    public ArrayList<AbstractTask> returnAllTask() {
        ArrayList<AbstractTask> allTask = super.returnAllTask();
        save();
        return allTask;
    }

    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    public void removeSubtask(Integer id) {
        super.removeSubtask(id);
        save();
    }

    public void removeCommonTask(int id) {
        super.removeCommonTask(id);
        save();
    }

    public void removeAllCommonTask() {
        super.removeAllCommonTask();
        save();
    }

    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    public AbstractTask returnTaskById(Integer id) {
        AbstractTask task = super.returnTaskById(id);
        save();
        return task;
    }

    public void updateCommonTask(CommonTask commonTask) {
        super.updateCommonTask(commonTask);
        save();
    }

    public void updateSubtaskTask(Subtask subtask) {
        super.updateSubtaskTask(subtask);
        save();
    }

    public void updateEpicCommonTask(Epic epic) {
        super.updateEpicCommonTask(epic);
        save();
    }

    public ArrayList<AbstractTask> returnTaskByEpic(int id) {
        ArrayList<AbstractTask> taskByEpic = super.returnTaskByEpic(id);
        save();
        return taskByEpic;
    }

    public void save() {
        ArrayList<AbstractTask> allTask = inGeneralList();
        try (Writer writer = new FileWriter("task.csv", StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (AbstractTask abstractTask : allTask) {
                writer.write(abstractTask.toString() + "\n");
            }
            writer.write("\n");
            writer.write(historyToString(super.getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных");
        }


    }

    private ArrayList<AbstractTask> inGeneralList() {
        ArrayList<AbstractTask> generalList = new ArrayList<>();

        for (Map.Entry<Integer, Epic> entry : super.getEpicTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        for (Map.Entry<Integer, Subtask> entry : super.getSubTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        for (Map.Entry<Integer, CommonTask> entry : super.getCommonTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        return generalList;
    }

    public static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try {
            String fileLine = Files.readString(Path.of(file));
            String[] line = fileLine.split("\n");
                if (line.length >=2) {
                    for (int i = 1; i < line.length; i++) {
                        if (!line[i].trim().isEmpty()) {
                            AbstractTask task = fileBackedTasksManager.fromString(line[i]);
                            if (task instanceof Epic) {
                                HashMap<Integer, Epic> epicTaskMap = fileBackedTasksManager.getEpicTaskMap();
                                epicTaskMap.put(task.getIdTask(), (Epic) task);
                            } else if (task instanceof Subtask) {
                                HashMap<Integer, Subtask> subTaskMap = fileBackedTasksManager.getSubTaskMap();
                                subTaskMap.put(task.getIdTask(), (Subtask) task);
                            } else if (task instanceof CommonTask) {
                                HashMap<Integer, CommonTask> commonTaskMap = fileBackedTasksManager.getCommonTaskMap();
                                commonTaskMap.put(task.getIdTask(), (CommonTask) task);
                            } else {
                                throw new IllegalArgumentException("Необходимо проверить файл, у одной из указанных задач не верный тип");
                            }
                        }else{
                            break;
                        }
                    }
                }
                if (line.length > 2) {
                    for (Integer integer : historyFromString(line[line.length - 1])) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager
                                .returnHistoryTask(integer, fileBackedTasksManager));
                    }
                }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return fileBackedTasksManager;
    }

    private AbstractTask returnHistoryTask(Integer number, FileBackedTasksManager fbtm) {
        HashMap<Integer, CommonTask> commonTaskHashMap = fbtm.getCommonTaskMap();
        HashMap<Integer, Subtask> subtaskHashMap = fbtm.getSubTaskMap();
        HashMap<Integer, Epic> epicHashMap = fbtm.getEpicTaskMap();
        if (commonTaskHashMap.containsKey(number)) {
            return commonTaskHashMap.get(number);
        } else if (subtaskHashMap.containsKey(number)) {
            return subtaskHashMap.get(number);
        } else {
            return epicHashMap.get(number);
        }
    }

    private AbstractTask fromString(String value) {
        int firstComma = value.indexOf(",") + 1;
        int secondComma = value.indexOf(",", firstComma);
        TypeTask whatTheTask = TypeTask.valueOf(value.substring(firstComma, secondComma));
        String[] partString = value.split(",");
        switch (whatTheTask) {
            case TASK:
                CommonTask commonTask = new CommonTask(Integer.parseInt(partString[0]), partString[2], partString[4], Status.valueOf(partString[3]));
                return commonTask;
            case SUBTASK:
                Subtask subtask = new Subtask(Integer.parseInt(partString[0]), partString[2], partString[4], Status.valueOf(partString[3]), Integer.parseInt(partString[5]));
                return subtask;
            case EPIC:
                Epic epicTask = new Epic(Integer.parseInt(partString[0]), partString[2], partString[4]);
                epicTask.setStatusTask(Status.valueOf(partString[3]));
                return epicTask;
            default:
                return null;
        }
    }

    static List<Integer> historyFromString(String value) {
        String[] valueArray = value.split(",");
        List<Integer> list = new ArrayList<>();

        for (String s : valueArray) {
            list.add(Integer.parseInt(s));
        }
        Collections.reverse(list);
        return list;
    }

    private static String historyToString(HistoryManager manager) {
        ArrayList<String> history = new ArrayList<>();

        for (AbstractTask task : manager.getHistory()) {
            history.add(Integer.toString(task.getIdTask()));
        }
        return String.join(",", history);
    }

    public static void main(String[] args) {
        CommonTask commonTask1 = new CommonTask("Сходить на почту"
                , "получить поссылку из деревни", Status.IN_PROGRESS,"12:30","90");
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты"
                , Status.IN_PROGRESS,"15:30","80");

        Epic epic1 = new Epic("Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        Epic epic2 = new Epic(2, "Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер", Status.DONE, 3);
        Subtask subtask2 = new Subtask(66, "Купить брюки с рубашкой"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW, 3);
        Subtask subtask3 = new Subtask("Купить букет жениху и невесте"
                , "Нужно собрать костюм на свадьбу друга", Status.NEW, 3);
//
//
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();


        fileBackedTasksManager.addCommonTask(commonTask1);
        fileBackedTasksManager.addCommonTask(commonTask2);
        fileBackedTasksManager.addEpicTask(epic1);
        fileBackedTasksManager.addSubTask(subtask1);
        fileBackedTasksManager.addSubTask(subtask2);
        fileBackedTasksManager.addSubTask(subtask3);
        fileBackedTasksManager.returnAllCommonTask();
        fileBackedTasksManager.returnAllEpic();
        fileBackedTasksManager.returnTaskById(5);
        fileBackedTasksManager.returnTaskById(2);
        System.out.println(fileBackedTasksManager.getHistory());
        FileBackedTasksManager newFileBackedTasksManager = FileBackedTasksManager.loadFromFile("task.csv");
        System.out.println(newFileBackedTasksManager.getHistory());


    }
}
