package com.javakanban.app;


import com.javakanban.app.manager.Managers;
import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Task;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Status;


public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task1 =new Task("task1", "task number one");
        Task task2 =new Task("task2", "task number two");
        Epic epic1 = new Epic("epic1", "epic number one");
        Epic epic2 = new Epic("epic2", "epic number two");
        Subtask subtask1 = new Subtask("subtask1", "subtask number one", epic1.getTaskId());
        Subtask subtask2 = new Subtask("subtask2", "subtask number two", epic1.getTaskId());
        Subtask subtask3 = new Subtask("subtask3", "subtask number three", epic2.getTaskId());


        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(epic1, subtask1);
        manager.createSubtask(epic1, subtask2);
        manager.createSubtask(epic2, subtask3);


        System.out.println("task");
        System.out.println(manager.getAllTasks());
        System.out.println("epic");
        System.out.println(manager.getAllEpics());
        System.out.println("subtask");
        System.out.println(manager.getAllSubtasksForEpic(epic1));
        System.out.println(manager.getAllSubtasksForEpic(epic2));

        task1.setStatus(Status.DONE);

        subtask1.setStatus(Status.DONE);
        manager.updateStatusForEpics(epic1);


        System.out.println("");
        System.out.println("task");
        System.out.println(manager.getAllTasks());
        System.out.println("epic");
        System.out.println(manager.getAllEpics());
        System.out.println("subtask");
        System.out.println(manager.getAllSubtasksForEpic(epic1));
        System.out.println(manager.getAllSubtasksForEpic(epic2));
        manager.updateStatusForEpics(epic1);

        manager.removeEpicById(epic1.getTaskId());

        System.out.println("");
        System.out.println("epic");
        System.out.println(manager.getAllEpics());
        System.out.println("subtask");
        System.out.println(manager.getAllSubtasksForEpic(epic1));
        System.out.println(manager.getAllSubtasksForEpic(epic2));
        manager.updateStatusForEpics(epic1);


        manager.removeTaskById(task1.getTaskId());
        manager.removeSubtaskById(subtask1.getTaskId());
        System.out.println("");
        System.out.println("task");
        System.out.println(manager.getAllTasks());
        System.out.println("subtask");
        System.out.println(manager.getAllSubtasksForEpic(epic1));
    }
}