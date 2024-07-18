package com.javakaban.app.manager;

import com.javakaban.app.typeTask.Task;
import com.javakaban.app.typeTask.Subtask;
import com.javakaban.app.typeTask.Epic;

import java.util.HashMap;

public class TaskManager {

    int id = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void createTask(Task task) {
        int taskId = ++id;
        task.setTaskId(taskId);
        tasks.put(taskId, task);
    }

    public void createEpic(Epic epic) {
        int epicId = ++id;
        epic.setTaskId(epicId);
        epics.put(epicId, epic);
    }

    public void createSubtask(Subtask subtask) {
        int subtaskId = ++id;
        subtask.setTaskId(subtaskId);
        subtasks.put(subtaskId, subtask);
    }


}