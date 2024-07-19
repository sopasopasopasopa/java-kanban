package com.javakaban.app.model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask, Status status) {
        super(nameTask, descriptionTask, status);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public  void removeSubtasks() {
        subtasks.clear();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Task{"
                + "nameTask = " + nameTask + '\''
                + ", description = " + descriptionTask
                + ", epicId = " + taskId
                + ", status = " + status
                + '}';
    }

}