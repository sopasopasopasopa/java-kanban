package com.javakaban.app.typeTask;

import com.javakaban.app.enumStatus.Status;

import java.util.HashMap;

public class Epic extends Task{

    public Epic(String nameTask, String descriptionTask, Status status) {
        super(nameTask, descriptionTask, status);
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