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

    public ArrayList<Task> returnAllTask() {
        ArrayList<Task> allTask = super.returnAllTask();
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

    public Task returnTaskById(Integer id) {
        Task task = super.returnTaskById(id);
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

    public ArrayList<Task> returnTaskByEpic(int id) {
        ArrayList<Task> taskByEpic = super.returnTaskByEpic(id);
        save();
        return taskByEpic;
    }

    public void save() {
        ArrayList<Task> allTask = inGeneralList();
        try (Writer writer = new FileWriter("task.csv", StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic,startTime,duration\n");
            for (Task task : allTask) {
                if (task instanceof Epic) {
                    writer.write(task.toString() + "\n");
                } else {
                    writer.write(task.toString()
                            + "," + task.getStartTime() + "," + task.getDuration() + "\n");
                }
            }
            writer.write("\n");
            writer.write(historyToString(super.getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных");
        }


    }

    private ArrayList<Task> inGeneralList() {
        ArrayList<Task> generalList = new ArrayList<>();

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
            int fileContainsSomething = 2;
            if (line.length >= fileContainsSomething) {
                loadTask(fileBackedTasksManager,line);
            }
            loadHistory(fileBackedTasksManager, line);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return fileBackedTasksManager;
    }

    private static void loadTask(FileBackedTasksManager fileBackedTasksManager, String[] line) {
        for (int i = 1; i < line.length; i++) {
            if (!line[i].trim().isEmpty()) {
                Task task = fileBackedTasksManager.fromString(line[i]);
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
            } else {
                break;
            }
        }
    }


    private static void loadHistory(FileBackedTasksManager fbt, String[] line) {
        int fileContainsSomethingAndHistory = 2;
        if (line.length > fileContainsSomethingAndHistory
                && line[line.length - 2].trim().isEmpty()) {
            for (Integer integer : historyFromString(line[line.length - 1])) {
                fbt.getHistoryManager().add(fbt.returnHistoryTask(integer, fbt));
            }
        } else {
            return;
        }
    }

    private Task returnHistoryTask(Integer number, FileBackedTasksManager fbtm) {
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

    private Task fromString(String value) {
        int firstComma = value.indexOf(",") + 1;
        int secondComma = value.indexOf(",", firstComma);
        TypeTask whatTheTask = TypeTask.valueOf(value.substring(firstComma, secondComma));
        String[] partString = value.split(",");
        switch (whatTheTask) {
            case TASK:
                return new CommonTask(Integer.parseInt(partString[0])
                        , partString[2], partString[4], Status.valueOf(partString[3]), partString[5], partString[6]);
            case SUBTASK:
               return new Subtask(Integer.parseInt(partString[0]), partString[2], partString[4]
                        , Status.valueOf(partString[3]), Integer.parseInt(partString[5]), partString[6], partString[7]);

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

        for (Task task : manager.getHistory()) {
            history.add(Integer.toString(task.getIdTask()));
        }
        return String.join(",", history);
    }

    public static void main(String[] args) {
        CommonTask commonTask1 = new CommonTask("Сходить на почту"
                , "получить поссылку из деревни"
                , Status.IN_PROGRESS, "2022.10.23 14:30", "80");
        CommonTask commonTask2 = new CommonTask("Купить сыра", "пармезан для пасты"
                , Status.IN_PROGRESS, "2022.10.23 13:30", "80");

        Epic epic1 = new Epic("Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        Epic epic2 = new Epic(2, "Купить костюм на свадьбу"
                , "Нужно собрать костюм на свадьбу друга");
        Subtask subtask1 = new Subtask("Купить обувь", "45 размер"
                , Status.DONE, 1, "2022.10.23 11:30", "80");
        Subtask subtask2 = new Subtask("Купить брюки с рубашкой"
                , "Нужно собрать костюм на свадьбу друга"
                , Status.NEW, 3, "2022.10.23 15:30", "80");
        Subtask subtask3 = new Subtask("Купить букет жениху и невесте"
                , "Нужно собрать костюм на свадьбу друга"
                , Status.NEW, 3, "2022.10.23 04:30", "80");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.addEpicTask(epic1);
        fileBackedTasksManager.addSubTask(subtask1);
        System.out.println(fileBackedTasksManager.returnAllTask());

        FileBackedTasksManager newFileBackedTasksManager = fileBackedTasksManager.loadFromFile("task.csv");
        System.out.println(newFileBackedTasksManager.returnAllTask());

    }

}
