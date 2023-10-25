package ru.yandex.app.service;

import ru.yandex.app.model.AbstractTask;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_LIST_SIZE = 10;
    private ArrayList<AbstractTask> tasksHistoryList;
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
    public void add(AbstractTask task) {
        if (tasksHistoryList.size() < MAX_LIST_SIZE) {
            tasksHistoryList.add(task);
        } else {
            tasksHistoryList.remove(0);
            tasksHistoryList.add(task);

        }
        linkLast(task);
    }

    private void linkLast(AbstractTask task) {


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

    public List<AbstractTask> getHistory() {
        ArrayList<AbstractTask> historyList = new ArrayList<>();
        Node<AbstractTask> nowNode = tail;

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
                Node tempTail = tail;
                tempTail.data = tempTail.prev.data;
                tempTail.prev=tail.prev.prev;
                tempTail.next=null;

                tail=tempTail;
            }
            else {
                Node nodeToRemoveInList = historyNode.get(id);
                tasksHistoryList.remove(nodeToRemoveInList.data);
                historyNode.remove(id);
            }
        }
    }
}

