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
        final int taskId1 = task1.getTaskId();
        task1 = new Task(taskId1, task1.getNameTask(), task1.getDescriptionTask());

        Task task2 = new Task("task2", "task number two");
        final int taskId2 = task1.getTaskId();
        task2 = new Task(taskId2, task2.getNameTask(), task2.getDescriptionTask());

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Epic epic1 = new Epic("epic1", "epic number one");
        final int epicId1 = epic1.getTaskId();
        epic1 = new Epic(epicId1, epic1.getNameTask(), epic1.getDescriptionTask());

        Epic epic2 = new Epic("epic2", "epic number two");
        final int epicId2 = epic1.getTaskId();
        epic2 = new Epic(epicId2, epic2.getNameTask(), epic2.getDescriptionTask());

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Subtask subtask1 = new Subtask("subtask1", "subtask number one", epic1.getTaskId());
        final int subtaskId1 = subtask1.getTaskId();
        subtask1 = new Subtask(subtaskId1, subtask1.getNameTask(), subtask1.getDescriptionTask(), epic1.getTaskId());

        Subtask subtask2 = new Subtask("subtask2", "subtask number two", epic1.getTaskId());
        final int subtaskId2 = subtask2.getTaskId();
        subtask2 = new Subtask(subtaskId2, subtask2.getNameTask(), subtask2.getDescriptionTask(), epic1.getTaskId());

        Subtask subtask3 = new Subtask("subtask3", "subtask number three", epic2.getTaskId());
        final int subtaskId3 =subtask3.getTaskId();
        subtask3 = new Subtask(subtaskId3, subtask3.getNameTask(), subtask3.getDescriptionTask(), epic2.getTaskId());


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