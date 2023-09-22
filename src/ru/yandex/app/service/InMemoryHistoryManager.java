package ru.yandex.app.service;

import ru.yandex.app.model.TaskClass;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_LIST_SIZE = 10;
    private static List<TaskClass> tasksHistoryList = new LinkedList<>();
    HashMap<Integer, Node> history = new HashMap<>();
    private Node tail;


    @Override
    public void add(TaskClass task) {
        if (tasksHistoryList.size() < MAX_LIST_SIZE) {
            tasksHistoryList.add(task);
        } else {
            tasksHistoryList.remove(0);
            tasksHistoryList.add(task);
        }
        linkLast(task);

    }

    @Override
    public List<TaskClass> getHistory() {
        List<TaskClass> copyListReturn = new LinkedList<>(tasksHistoryList);
        return copyListReturn;

    }

    public void linkLast(TaskClass task) {
        Node lastNode = tail;
        Node newNode = new Node(task, null, lastNode);
        if (history.containsKey(task.getIdTask())) {
            removeOldNode(lastNode);
            history.remove(task.getIdTask());
            history.put(task.getIdTask(), newNode);
        } else {
            
            tail = newNode;
            history.put(task.getIdTask(), tail);

        }

    }

    public void removeOldNode(Node node) {
        Node prevMonde = node.prev;
        Node nextNode = node.next;
        prevMonde.next = nextNode;
        nextNode.prev = prevMonde;

    }

    public void getTasks() {

    }

    @Override
    public void remove(int id) {
        //
    }
}

