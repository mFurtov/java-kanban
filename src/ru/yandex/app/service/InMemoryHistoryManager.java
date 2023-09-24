package ru.yandex.app.service;

import ru.yandex.app.model.TaskClass;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_LIST_SIZE = 10;
    private static List<TaskClass> tasksHistoryList = new LinkedList<>();
    private HashMap<Integer, Node> history = new HashMap<>();
    private Node head;
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

    //@Override
//    public List<TaskClass> getHistory() {
//        List<TaskClass> copyListReturn = new LinkedList<>(tasksHistoryList);
//        return copyListReturn;
//
//    }

    private void linkLast(TaskClass task) {
        Node lastNode = tail;
        Node newNode = new Node(task, null, lastNode);
        if (history.containsKey(task.getIdTask())) {
            tail = newNode;
            Node nodeToRemove = history.get(task.getIdTask());
            if (nodeToRemove.next == null) {
                removeOldNode(nodeToRemove, newNode);
                history.remove(task.getIdTask());
                history.put(task.getIdTask(), newNode);
            } else {
                removeOldNode(nodeToRemove, newNode);
                lastNode.next = newNode;
                newNode.prev = lastNode;
                history.remove(task.getIdTask());
                history.put(task.getIdTask(), newNode);
            }

        } else {
            if (tail != null) {
                lastNode.next = newNode;
                newNode.prev = lastNode;
            }else{
                head = newNode;
            }
            tail = newNode;
            history.put(task.getIdTask(), tail);
        }

    }

    private void removeOldNode(Node node, Node newNod) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (nextNode == null && prevNode == null) {
            tail.next = null;
            tail.prev = null;
            return;
        }else if(prevNode==null){
            head =node.next;
            node.next.prev=null;
        }else if (nextNode == null) {
            node.prev.next = tail;
            tail.prev = prevNode;
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }

    }
    public List<TaskClass> getHistory(){
        ArrayList<TaskClass> historyList = new ArrayList<>();
        for (int i = 0; i < history.size(); i++) {
            
        }
    return new ArrayList<>();
    }

//    public void getTasks() {
//
//    }

    @Override
    public void remove(int id) {
        //
    }
}

