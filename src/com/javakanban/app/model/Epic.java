package com.javakanban.app.model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskArrayList = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    public void addSubtask(Subtask subtask) {
        subtaskArrayList.add(subtask);
    }

    public void removeSubtasks() {
        subtaskArrayList.clear();
    }

    public ArrayList<Subtask> getSubtasksList() {
        return subtaskArrayList;
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