package ru.yandex.app.service;

import ru.yandex.app.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_LIST_SIZE = 10;
    private ArrayList<Task> tasksHistoryList;
    private HashMap<Integer, Node> historyNode;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        this.tasksHistoryList = new ArrayList<>();
        this.historyNode = new HashMap<>();
        this.head = null;
        this.tail = null;
    }


    @Override
    public void add(Task task) {
        if (tasksHistoryList.size() < MAX_LIST_SIZE) {
            tasksHistoryList.add(task);
        } else {
            tasksHistoryList.remove(0);
            tasksHistoryList.add(task);

        }
        linkLast(task);
    }

    private void linkLast(Task task) {


        Node lastNode = tail;
        Node newNode = new Node(task, null, lastNode);
        if (historyNode.containsKey(task.getIdTask())) {
            tail = newNode;
            Node nodeToRemove = historyNode.get(task.getIdTask());
            if (nodeToRemove.next == null) {
                removeOldNode(nodeToRemove);
                historyNode.remove(task.getIdTask());
                historyNode.put(task.getIdTask(), newNode);
            } else {
                removeOldNode(nodeToRemove);
                lastNode.next = newNode;
                newNode.prev = lastNode;
                historyNode.remove(task.getIdTask());
                historyNode.put(task.getIdTask(), newNode);
            }

        } else {
            if (tail != null) {
                lastNode.next = newNode;
                newNode.prev = lastNode;
            } else {
                head = newNode;
            }
            tail = newNode;
            historyNode.put(task.getIdTask(), tail);
        }

    }

    private void removeOldNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (nextNode == null && prevNode == null) {
            tail.next = null;
            tail.prev = null;
        } else if (prevNode == null) {
            head = node.next;
            node.next.prev = null;
        } else if (nextNode == null) {
            node.prev.next = tail;
            tail.prev = prevNode;
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }

    }

    public List<Task> getHistory() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node<Task> nowNode = tail;

        int count = 0;
        while (nowNode != null && count < MAX_LIST_SIZE) {
            historyList.add(nowNode.data);
            nowNode = nowNode.prev;
            count++;
        }

        return historyList;
    }


    @Override
    public void remove(int id) {
        Node nodeToRemove = historyNode.get(id);
        if (historyNode.containsKey(id)) {
            removeOldNode(nodeToRemove);
            if (id == -1) {
                historyNode.clear();

            }else if(tasksHistoryList.size()==1){
                head =null;
                tail=null;
                historyNode.clear();
                tasksHistoryList.clear();
            }else if(nodeToRemove==tail){
                tail = tail.prev;
                if (tail != null) {
                    tail.next = null;
                }
            }
            else {
                Node nodeToRemoveInList = historyNode.get(id);
                tasksHistoryList.remove(nodeToRemoveInList.data);
                historyNode.remove(id);
            }
        }
    }
}

