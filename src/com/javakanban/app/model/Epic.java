package com.javakanban.app.model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskArrayList = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    public Epic(int taskId, String nameTask, String descriptionTask) {
        super(taskId, nameTask, descriptionTask);
    }
    public void addSubtask(Subtask subtask) {
        subtaskArrayList.add(subtask);
    }

    public void removeSubtasks() {
        subtaskArrayList.clear();
    }

    public void removeSubtask(Subtask subtask) {
        subtaskArrayList.remove(subtask);
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
    }

    public int getEpicIdOfSubtask(Subtask subtask) {
        return subtask.getEpicId();
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