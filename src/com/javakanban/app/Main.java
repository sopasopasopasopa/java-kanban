package com.javakanban.app;


import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Task;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Status;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 =new Task("task1", "task number one");
        Task task2 =new Task("task2", "task number two");
        Epic epic1 = new Epic("epic1", "epic number one");
        Subtask subtask1 = new Subtask("subtask1", "subtask number one", epic1.getTaskId());
        Subtask subtask2 = new Subtask("subtask2", "subtask number two", epic1.getTaskId());
        Epic epic2 = new Epic("epic2", "epic number two");
        Subtask subtask3 = new Subtask("subtask3", "subtask number three", epic2.getTaskId());


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

        task1.setStatus(Status.DONE);

        subtask1.setStatus(Status.NEW);
        taskManager.updateStatusForEpics(epic1);


        System.out.println("");
        System.out.println("task");
        System.out.println(taskManager.getAllTasks());
        System.out.println("epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("subtask");
        System.out.println(taskManager.getAllSubtasksForEpic(epic1));
        System.out.println(taskManager.getAllSubtasksForEpic(epic2));
        taskManager.updateStatusForEpics(epic1);

        taskManager.removeEpicById(epic1.getTaskId());

        System.out.println("");
        System.out.println("epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("subtask");
        System.out.println(taskManager.getAllSubtasksForEpic(epic1));
        System.out.println(taskManager.getAllSubtasksForEpic(epic2));
        taskManager.updateStatusForEpics(epic1);


        taskManager.removeTaskById(task1.getTaskId());
        taskManager.removeSubtaskById(subtask1.getTaskId());
        System.out.println("");
        System.out.println("task");
        System.out.println(taskManager.getAllTasks());
        System.out.println("subtask");
        System.out.println(taskManager.getAllSubtasksForEpic(epic1));
    }
}