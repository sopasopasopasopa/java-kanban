package com.javakaban.app;

import com.javakaban.app.enumStatus.Status;
import com.javakaban.app.typeTask.Task;
import com.javakaban.app.typeTask.Subtask;
import com.javakaban.app.typeTask.Epic;
import com.javakaban.app.manager.TaskManager;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 =new Task("task1", "task number one", Status.NEW);
        Task task2 =new Task("task2", "task number two", Status.NEW);
        Epic epic1 = new Epic("epic1", "epic number one", Status.NEW);
        Subtask subtask1 = new Subtask("subtask1", "subtask number one", Status.DONE, epic1.getTaskId());
        Subtask subtask2 = new Subtask("subtask2", "subtask number two", Status.NEW, epic1.getTaskId());
        Epic epic2 = new Epic("epic2", "epic number two", Status.NEW);
        Subtask subtask3 = new Subtask("subtask3", "subtask number three", Status.DONE, epic2.getTaskId());


        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(epic1, subtask1);
        taskManager.createSubtask(epic1, subtask2);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(epic2, subtask3);


        System.out.println("task");
        System.out.println(taskManager.getAllTasks());
        System.out.println("epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("subtask");
        System.out.println(taskManager.getAllSubtasksForEpic(epic1));
        System.out.println(taskManager.getAllSubtasksForEpic(epic2));

        taskManager.updateStatusForTask(task1, Status.DONE);

        taskManager.createSubtask(epic1, subtask1);


        System.out.println("");
        System.out.println("task");
        System.out.println(taskManager.getAllTasks());
        System.out.println("epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("subtask");
        System.out.println(taskManager.getAllSubtasksForEpic(epic1));
        System.out.println(taskManager.getAllSubtasksForEpic(epic2));
        taskManager.refreshStatuses();

        taskManager.removeTaskById(task1.getTaskId());
        taskManager.removeTaskById(subtask1.getTaskId());
        System.out.println("");
        System.out.println("task");
        System.out.println(taskManager.getAllTasks());
        System.out.println("subtask");
        System.out.println(taskManager.getAllSubtasksForEpic(epic1));
    }
}