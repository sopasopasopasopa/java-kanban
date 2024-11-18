package com.javakanban.app.model;

import java.time.*;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    public Epic(int taskId, String nameTask, String descriptionTask) {
        super(taskId, nameTask, descriptionTask);
    }
    public void addSubtask(Subtask subtask) {
        subtaskArrayList.add(subtask);
        updateEpicDetails();
    }

    public void removeSubtasks() {
        subtaskArrayList.clear();
        updateEpicDetails();
    }

    public void removeSubtask(Subtask subtask) {
        subtaskArrayList.remove(subtask);
        updateEpicDetails();
    }

    public ArrayList<Subtask> getSubtasksList() {
        return subtaskArrayList;
    }

    public ArrayList<Integer> getSubtasksIds() {
        ArrayList<Integer> subtasksIds = new ArrayList<>();
        for (Subtask subtask : subtaskArrayList) {
            subtasksIds.add(subtask.getTaskId());
        }
        return subtasksIds;
    }

    public void setSubtaskArrayList(ArrayList<Subtask> subtaskArrayList) {
        this.subtaskArrayList = subtaskArrayList;
        updateEpicDetails();
    }

    public int getEpicIdOfSubtask(Subtask subtask) {
        return subtask.getEpicId();
    }

    private void updateEpicDetails() {
        if (subtaskArrayList.isEmpty()) {
            this.startTime = null;
            this.duration = Duration.ZERO;
            return;
        }

        LocalDateTime earliestStart = LocalDateTime.MAX;
        LocalDateTime latestEnd = LocalDateTime.MIN;

        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : subtaskArrayList) {
            if (subtask.getStartTime() != null) {
                earliestStart = earliestStart.isBefore(subtask.getStartTime()) ? earliestStart : subtask.getStartTime();
            }
            if (subtask.getEndTime() != null) {
                latestEnd = latestEnd.isAfter(subtask.getEndTime()) ? latestEnd : subtask.getEndTime();
            }
            totalDuration = totalDuration.plus(subtask.getDuration());
        }

        this.startTime = earliestStart;
        this.duration = totalDuration;

        if (startTime != null && duration != null) {
            this.endTime = startTime.plus(duration);
        } else {
            this.endTime = null;
        }
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