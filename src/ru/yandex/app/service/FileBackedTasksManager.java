package ru.yandex.app.service;

import com.sun.jdi.ArrayReference;
import ru.yandex.app.model.AbstractTask;
import ru.yandex.app.model.CommonAbstractTask;
import ru.yandex.app.model.Epic;
import ru.yandex.app.model.Subtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public void addCommonTask(CommonAbstractTask commonTask) {
        super.addCommonTask(commonTask);
    }

    public void addEpicTask(Epic epicTask) {
        super.addEpicTask(epicTask);
    }

        public void addSubTask(Subtask subtask) {
            super.addSubTask(subtask);
    }
    public ArrayList<Epic> returnAllEpic() {
        return super.returnAllEpic();
    }
    public ArrayList<CommonAbstractTask> returnAllCommonTask() {
        return super.returnAllCommonTask();
    }

    //    public ArrayList<Subtask> returnAllSubtask() {
//
//    }
    public ArrayList<AbstractTask> returnAllTask() {
        return super.returnAllTask();
    }
//    public void removeEpic(int id) {
//
//    }
//    public void removeSubtask(Integer id) {
//
//    }
//    public void removeCommonTask(int id) {
//
//    }
//    public void removeAllCommonTask() {
//
//    }
//    public void removeAllSubtask() {
//
//    }
//    public void removeAllEpic() {
//
//    }
//    public AbstractTask returnTaskById(Integer id) {
//
//    }
//    public void updateCommonTask(CommonAbstractTask commonTask) {
//
//    }
//    public void updateSubtaskTask(Subtask subtask) {
//
//    }
//    public void updateEpicCommonTask(Epic epic) {
//
//    }
//    public ArrayList<AbstractTask> returnTaskByEpic(int id) {
//
//    }
//    public void setEpicTaskStatus(Epic epicTask) {
//
//    }
//    public List<AbstractTask> getHistory(){
//
//    }
//    public <T extends AbstractTask> void enumerationMap(HashMap<Integer, T> taskMap) {
//
//    }
//    private int generateNextID(){
//
//    }
    public void save() {
        ArrayList<AbstractTask> allTask = inGeneralList();

        System.out.println(allTask);

    }
    private ArrayList<AbstractTask> inGeneralList(){
        ArrayList<AbstractTask> generalList = new ArrayList<>();

        for (Map.Entry<Integer, Epic> entry : super.getEpicTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        for (Map.Entry<Integer, Subtask> entry : super.getSubTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        for (Map.Entry<Integer, CommonAbstractTask> entry : super.getCommonTaskMap().entrySet()) {
            generalList.add(entry.getValue());
        }
        return generalList;
    }
    
}
