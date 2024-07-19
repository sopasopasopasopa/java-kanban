package com.javakaban.app.manager;

import com.javakaban.app.enumStatus.Status;
import com.javakaban.app.typeTask.Task;
import com.javakaban.app.typeTask.Subtask;
import com.javakaban.app.typeTask.Epic;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {

    int id = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public void createTask(Task task) {
        int taskId = ++id;
        task.setTaskId(taskId);
        tasks.put(taskId, task);
    }

    public void createEpic(Epic epic) {
        createTask(epic);
        epic.setTaskId(id);
        epics.put(id, epic);
        System.out.println(epics);
    }

    public void createSubtask(Epic epic, Subtask subtask) {
        createTask(subtask);
        subtask.setTaskId(id);
        subtask.setEpicId(epic.getTaskId());
        subtasks.put(id, subtask);
        refreshStatuses();
    }

    public void removeTasks() {
        tasks.clear();
        removeEpics();
    }

    public void removeEpics() {
        epics.clear();
        removeSubtasks();
    }
    public void removeSubtasks() {
        subtasks.clear();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasksForEpic(Epic epic) {
        List<Subtask> matchingSubtaskList = new ArrayList<>();

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getTaskId()) {
                matchingSubtaskList.add(subtask);
            }
        }

        return matchingSubtaskList;
    }
    public void removeTaskById(int id) {
        tasks.remove(id);

        if (epics.containsKey(id)) {
            int epicTaskId = epics.get(id).getTaskId();

            subtasks.entrySet().removeIf(entry -> entry.getValue().getEpicId() == epicTaskId);
            epics.remove(id);
        }
    }
    public void refreshStatuses() {
        for (Epic epic : epics.values()) {
            List<Subtask> subtasksForEpic = getAllSubtasksForEpic(epic);

            if (subtasksForEpic.isEmpty()) {
                epic.setStatus(Status.NEW);
                continue;
            }

            boolean allNew = true;
            boolean allDone = true;

            for (Subtask subtask : subtasksForEpic) {
                if (!subtask.getStatus().equals(Status.NEW)) {
                    allNew = false;
                }
                if (!subtask.getStatus().equals(Status.DONE)) {
                    allDone = false;
                }
            }

            if (allNew) {
                updateStatus(epic, Status.NEW);
            } else if (allDone) {
                updateStatus(epic, Status.DONE);
            } else {
                updateStatus(epic, Status.IN_PROGRESS);
            }
        }
    }

    private void updateStatus(Epic epic, Status status) {
        epic.setStatus(status);
        if (tasks.containsKey(epic.getTaskId())) {
            tasks.get(epic.getTaskId()).setStatus(status);
        }
    }

}