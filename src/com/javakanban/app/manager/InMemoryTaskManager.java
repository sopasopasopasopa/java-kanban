package com.javakanban.app.manager;

import com.javakanban.app.model.Status;
import com.javakanban.app.model.Task;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Epic;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    int id = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsValue(task.getTaskId())) {
            tasks.put(task.getTaskId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsValue(epic.getTaskId())) {
            epics.put(epic.getTaskId(), epic);
            updateStatusForEpics(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsValue(subtask.getTaskId())) {
            Epic epic = epics.get(subtask.getEpicId());
            subtasks.put(subtask.getTaskId(), subtask);
            updateStatusForEpics(epic);
        }
    }

    @Override
    public void createTask(Task task) {
        task.setTaskId(++id);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setTaskId(++id);
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void createSubtask(Epic epic, Subtask subtask) {
        subtask.setTaskId(++id);
        subtasks.put(subtask.getTaskId(), subtask);
        epic.addSubtask(subtask);
        updateStatusForEpics(epic);
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasksForEpic(Epic epic) {
        List<Subtask> matchingSubtaskList = new ArrayList<>();
        matchingSubtaskList = epic.getSubtasksList();
        return matchingSubtaskList;
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtasksToRemove = epic.getSubtasksList();
        subtasksToRemove.clear();
        epics.remove(id);
        System.out.println(epic.getSubtasksList());
    }

    @Override
    public void removeSubtaskById(int id) {
        subtasks.remove(id);
    }

    @Override
    public void updateStatusForEpics(Epic epic) {
        if (epic.getSubtasksList().isEmpty()) {
            epic.setStatus(Status.NEW);
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : epic.getSubtasksList()) {
            if (!subtask.getStatus().equals(Status.NEW)) {
                allNew = false;
            }
            if (!subtask.getStatus().equals(Status.DONE)) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}