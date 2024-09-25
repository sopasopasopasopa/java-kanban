package com.javakanban.app.manager;

import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Task;

import java.io.*;


public class FileBackedTaskManager extends InMemoryTaskManager{

    private final File file;

    public FileBackedTaskManager(File file, HistoryManager historyManager) throws IOException {
        super(historyManager);
        this.file = file;
    }

    void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            for (Task task : getAllTasks()) {
                writer.write(taskToString(task));
                writer.newLine();
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(subtaskToString(subtask));
                writer.newLine();
            }

            for (Epic epic : getAllEpics()) {
                writer.write(epicToString(epic));
                writer.newLine();
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private String taskToString(Task task) {
        return "TASK: Task" + task.getTaskId() + "," + task.getNameTask() + "," + task.getDescriptionTask();
    }

    private String subtaskToString(Subtask subtask) {
        return "SUBTASK:Sub Task" + subtask.getTaskId() + "," + subtask.getNameTask() + "," + subtask.getDescriptionTask() + "," + subtask.getEpicId();
    }

    private String epicToString(Epic epic) {
        return "EPIC,Epic" + epic.getTaskId() + "," + epic.getNameTask() + "," + epic.getDescriptionTask();
    }

    @Override
    public void createTask(Task task) {
        task.setTaskId(++id);
        tasks.put(task.getTaskId(), task);
        save();
    }

    @Override
    public void createSubtask(Epic epic, Subtask subtask) {
        subtask.setTaskId(++id);
        subtasks.put(subtask.getTaskId(), subtask);
        epic.addSubtask(subtask);
        updateStatusForEpics(epic);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setTaskId(++id);
        epics.put(epic.getTaskId(), epic);
        save();
    }


}
