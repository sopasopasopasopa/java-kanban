package com.javakaban.app;

import com.javakaban.app.enumStatus.Status;
import com.javakaban.app.typeTask.Task;
import com.javakaban.app.typeTask.Subtask;
import com.javakaban.app.typeTask.Epic;
import com.javakaban.app.manager.TaskManager;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("epic1", "epic number one", Status.NEW);
        Subtask subtask1 = new Subtask("subtask1", "subtask number one", Status.NEW, epic1.getTaskId());
        Subtask subtask2 = new Subtask("subtask2", "subtask number two", Status.NEW, epic1.getTaskId());
        Epic epic2 = new Epic("epic2", "epic number two", Status.NEW);
        Subtask subtask3 = new Subtask("subtask3", "subtask number three", Status.DONE, epic2.getTaskId());
//
        taskManager.createEpic(epic1);
        taskManager.createSubtask(epic1, subtask1);
        taskManager.createSubtask(epic1, subtask2);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(epic2, subtask3);
//
//        taskManager.removeEpics();
//        System.out.println(epic1);
//        System.out.println(taskManager.getEpicById(1));

        System.out.println("task");
        System.out.println(taskManager.getAllTasks());
        System.out.println("epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("subtask");
        System.out.println(taskManager.getAllSubtasksForEpic(epic1));


//        taskManager.removeTaskById(1);
        System.out.println("");
        System.out.println("task");
        System.out.println(taskManager.getAllTasks());
        System.out.println("epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("subtask");
        System.out.println(taskManager.getAllSubtasksForEpic(epic2));
        taskManager.refreshStatuses();
    }
}