package com.javakaban.app.typeTask;

import com.javakaban.app.enumStatus.Status;

public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected int taskId;
    protected Status status;

    public Task(String nameTask, String descriptionTask, Status status) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.status = status;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{"
                + "nameTask = " + nameTask + '\''
                + ", description = " + descriptionTask
                + ", taskId = " + taskId
                + ", status = " + status
                + '}';
    }
}
