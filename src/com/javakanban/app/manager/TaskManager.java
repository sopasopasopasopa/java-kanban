package com.javakanban.app.manager;

import com.javakanban.app.model.Status;
import com.javakanban.app.model.Task;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Epic;

import java.util.*;

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

    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getTaskId());
        oldEpic.setNameTask(epic.getNameTask());
        oldEpic.setDescriptionTask(epic.getDescriptionTask());
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsValue(subtask)) {
            int epicId = subtask.getEpicId();
            Subtask oldSubtask = subtasks.get(subtask.getTaskId());
            oldSubtask.setNameTask(subtask.getNameTask());
            oldSubtask.setDescriptionTask(subtask.getDescriptionTask());
            Epic epic = epics.get(epicId);
            updateStatusForEpics(epic);
        }
    }

    public void createTask(Task task) {
        task.setTaskId(++id);
        tasks.put(task.getTaskId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setTaskId(++id);
        epics.put(epic.getTaskId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setTaskId(++id);
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getTaskId(), subtask);
        epic.addSubtask(subtask);
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }
    public void removeSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasksForEpic(Epic epic) {
        List<Subtask> matchingSubtaskList = new ArrayList<>();
        matchingSubtaskList = epic.getSubtasksList();
        return matchingSubtaskList;
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtasksToRemove = epic.getSubtasksList();
        subtasksToRemove.clear();
        epics.remove(id);
        System.out.println(epic.getSubtasksList());
    }

    public void removeSubtaskById(int id) {
        subtasks.remove(id);
    }


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



}
