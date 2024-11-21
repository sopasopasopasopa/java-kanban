package com.javakanban.app.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String nameTask, String descriptionTask, int epicId) {
        super(nameTask, descriptionTask);
        this.setEpicId(epicId);
    }

    public Subtask(int taskId, String nameTask, String descriptionTask, Duration duration, LocalDateTime startTime, int epicId) {
        super(taskId, nameTask, descriptionTask, duration, startTime);
        this.setEpicId(epicId);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicId);
    }

    @Override
    public String toString() {
        return "Task{"
                + "nameTask = " + nameTask + '\''
                + ", description = " + descriptionTask
                + ", taskId = " + taskId
                + ", status = " + status
                + ", epicId = " + epicId
                + ", duration=" + duration
                + ", startTime=" + startTime
                + '}';
    }
}