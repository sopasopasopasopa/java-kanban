package com.javakanban.app.manager;

import com.javakanban.app.model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void addTask(Task task);
}
