package com.javakaban.app.manager;

import com.javakaban.app.model.Status;
import com.javakaban.app.model.Task;
import com.javakaban.app.model.Subtask;
import com.javakaban.app.model.Epic;

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

    public Task updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
        return task;
    }

    public Task updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getTaskId());
        oldEpic.setNameTask(epic.getNameTask());
        oldEpic.setDescriptionTask(epic.getDescriptionTask());
        return oldEpic;
    }

    public Task updateSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Subtask oldSubtask = subtasks.get(subtask.getTaskId());
        oldSubtask.setNameTask(subtask.getNameTask());
        oldSubtask.setDescriptionTask(subtask.getDescriptionTask());
        Epic epic = epics.get(epicId);
        return subtask;
    }

    public void createTask(Task task) {
        task.setTaskId(++id);
        tasks.put(task.getTaskId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setTaskId(++id);
        epics.put(epic.getTaskId(), epic);
    }

    public void createSubtask(Epic epic, Subtask subtask) {
        subtask.setTaskId(++id);
        subtask.setEpicId(epic.getTaskId());
        subtasks.put(subtask.getTaskId(), subtask);
        epic.addSubtask(subtask);
        refreshStatuses();
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
                updateStatusForEpic(epic, Status.NEW);
            } else if (allDone) {
                updateStatusForEpic(epic, Status.DONE);
            } else {
                updateStatusForEpic(epic, Status.IN_PROGRESS);
            }
        }
    }

    private void updateStatusForEpic(Epic epic, Status status) {
        epic.setStatus(status);
        if (subtasks.containsKey(epic.getTaskId())) {
            subtasks.get(epic.getTaskId()).setStatus(status);
        }
    }

    public void updateStatusForTask(Task task, Status status) {
        task.setStatus(status);
    }

}