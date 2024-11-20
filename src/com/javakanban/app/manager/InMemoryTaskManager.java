package com.javakanban.app.manager;

import com.javakanban.app.model.Status;
import com.javakanban.app.model.Task;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Epic;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager;
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime() == null ? LocalDateTime.MAX : task.getStartTime()));
    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean isOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) return false;
        return !(end1.isBefore(start2) || end2.isBefore(start1));
    }

    public boolean hasOverlappingTasks(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getDuration() == null) return false;
        LocalDateTime newTaskEnd = newTask.getStartTime().plus(newTask.getDuration());

        return prioritizedTasks.stream()
                .anyMatch(task -> isOverlap(newTask.getStartTime(), newTaskEnd, task.getStartTime(), task.getStartTime().plus(task.getDuration())));
    }


    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.addTask(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.addTask(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.addTask(subtask);
        }
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        if (hasOverlappingTasks(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующими задачами.");
        }
        tasks.put(task.getTaskId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.remove(task);
            prioritizedTasks.add(task);
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        if (hasOverlappingTasks(epic)) {
            throw new IllegalArgumentException("Эпик пересекается по времени с существующими задачами.");
        }
        epics.put(epic.getTaskId(), epic);
        prioritizedTasks.remove(epic);
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        updateStatusForEpics(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (hasOverlappingTasks(subtask)) {
            throw new IllegalArgumentException("Подзадача пересекается по времени с существующими задачами");
        }
        subtasks.put(subtask.getTaskId(), subtask);
        prioritizedTasks.remove(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateStatusForEpics(epic);
        }
    }

    @Override
    public void createTask(Task task) {
        if (hasOverlappingTasks(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующими задачами.");
        }
        task.setTaskId(++id);
        tasks.put(task.getTaskId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (hasOverlappingTasks(epic)) {
            throw new IllegalArgumentException("Эпик пересекается по времени с существующими задачами.");
        }
        epic.setTaskId(++id);
        epics.put(epic.getTaskId(), epic);
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
    }

    @Override
    public void createSubtask(Epic epic, Subtask subtask) {
        if (hasOverlappingTasks(subtask)) {
            throw new IllegalArgumentException("Подзадача пересекается по времени с существующими задачами.");
        }
        subtask.setTaskId(++id);
        subtasks.put(subtask.getTaskId(), subtask);
        epic.addSubtask(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        updateStatusForEpics(epic);
    }

    @Override
    public void removeTasks() {
        tasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public void removeEpics() {
        epics.clear();
        subtasks.clear();
        prioritizedTasks.clear();
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Subtask);
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
        return epic.getSubtasksList().stream().collect(Collectors.toList());
    }

    @Override
    public void removeTaskById(int id) {
        Task taskToRemove = tasks.remove(id);
        if (taskToRemove != null && taskToRemove.getStartTime() != null) {
            prioritizedTasks.remove(taskToRemove);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtasksToRemove = epic.getSubtasksList();
        Epic epicToRemove = epics.remove(id);
        if (epicToRemove != null && epicToRemove.getStartTime() != null) {
            prioritizedTasks.remove(epicToRemove);
        }
        if (epicToRemove != null) {
            for (Subtask subtask : subtasksToRemove) {
                subtasks.remove(subtask.getTaskId());
                if (subtask.getStartTime() != null) {
                    prioritizedTasks.remove(subtask);
                }
            }
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtaskToRemove = subtasks.remove(id);
        if (subtaskToRemove != null && subtaskToRemove.getStartTime() != null) {
            prioritizedTasks.remove(subtaskToRemove);
        }
        for (Epic epic : epics.values()) {
            if (epic.getSubtasksList().remove(subtaskToRemove)) {
                updateStatusForEpics(epic);
                break;
            }
        }
    }

    @Override
    public void updateStatusForEpics(Epic epic) {
        if (epic.getSubtasksList().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean hasNew = epic.getSubtasksList().stream().anyMatch(subtask -> subtask.getStatus() == Status.NEW);
        boolean allDone = epic.getSubtasksList().stream().allMatch(subtask -> subtask.getStatus() == Status.DONE);

        if (hasNew) {
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