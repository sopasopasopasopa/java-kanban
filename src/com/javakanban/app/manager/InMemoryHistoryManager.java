package com.javakanban.app.manager;

import com.javakanban.app.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private final HashMap<Integer, Node> taskMap = new HashMap<>();
    private static final int LIST_SIZE_FOR_HISTORY = 10;
    private Node head;
    private Node tail;

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        taskMap.put(task.getTaskId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        taskMap.remove(node.task.getTaskId());
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            if (taskMap.containsKey(task.getTaskId())) {
                removeNode(taskMap.get(task.getTaskId()));
            }

            if (taskMap.size() >= LIST_SIZE_FOR_HISTORY) {
                removeNode(head);
            }

            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (taskMap.containsKey(id)) {
            removeNode(taskMap.get(id));
        }
    }
}


