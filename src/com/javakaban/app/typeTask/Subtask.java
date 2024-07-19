package com.javakaban.app.typeTask;

import com.javakaban.app.enumStatus.Status;


public class Subtask extends Task {
    int epicId;

    public Subtask(String nameTask, String descriptionTask, Status status, int epicId) {
        super(nameTask, descriptionTask, status);
        this.setEpicId(epicId);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Task{"
                + "nameTask = " + nameTask + '\''
                + ", description = " + descriptionTask
                + ", taskId = " + taskId
                + ", status = " + status
                + ", epicId = " + epicId
                + '}';
    }
}