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
        loadFromFile(file);
    }

    public FileBackedTaskManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (line.startsWith("TASK:")) {
                    if (parts.length >= 3) {
                        int id = Integer.parseInt(parts[0].substring(5));
                        String name = parts[1];
                        String description = parts[2];
                        Task task = new Task(id, name, description);
                        manager.tasks.put(id, task);
                    }
                } else if (line.startsWith("SUBTASK:")) {
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0].substring(8));
                        String name = parts[1];
                        String description = parts[2];
                        int epicId = Integer.parseInt(parts[3].substring(5));
                        Subtask subtask = new Subtask(id, name, description, epicId);
                        manager.subtasks.put(id, subtask);
                    }
                } else if (line.startsWith("EPIC:")) {
                    if (parts.length >= 3) {
                        int id = Integer.parseInt(parts[0].substring(5));
                        String name = parts[1];
                        String description = parts[2];
                        Epic epic = new Epic(id, name, description);
                        manager.epics.put(id, epic);

                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Ошибка при загрузке файла!");
        }
        return manager;
    }

    public void save() throws ManagerSaveException {
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
            throw new ManagerSaveException("Ошибка при сохранении файла.");
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
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createSubtask(Epic epic, Subtask subtask) {
        subtask.setTaskId(++id);
        subtasks.put(subtask.getTaskId(), subtask);
        epic.addSubtask(subtask);
        updateStatusForEpics(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setTaskId(++id);
        epics.put(epic.getTaskId(), epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }


}
