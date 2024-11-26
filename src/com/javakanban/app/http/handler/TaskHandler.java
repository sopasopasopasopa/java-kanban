package com.javakanban.app.http.handler;

import com.javakanban.app.manager.ManagerException;
import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Task;
import com.javakanban.app.util.GsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetTask(exchange);
                break;
            case "POST":
                handlePostTask(exchange);
                break;
            case "DELETE":
                handleDeleteTask(exchange);
                break;
            default:
                sendMethodNotAllowed(exchange);
        }
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Task task = null;
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            task = taskManager.getTaskById(id);
            if (task != null) {
                String response = GsonUtil.getGson().toJson(task);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            List<Task> tasks = taskManager.getAllTasks();
            String response = GsonUtil.getGson().toJson(tasks);
            sendText(exchange, response, 200);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(),
                StandardCharsets.UTF_8))) {
            String json = reader.lines().collect(Collectors.joining());
            Task task = GsonUtil.getGson().fromJson(json, Task.class);

            if (task.getTaskId() > 0) {
                taskManager.updateTask(task);
                sendText(exchange, GsonUtil.getGson().toJson(task), 200);
            } else {
                try {
                    int id = taskManager.createTask(task);
                    sendText(exchange, GsonUtil.getGson().toJson(task), 201);
                } catch (ManagerException e) {
                    sendHasInteractions(exchange);
                }
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                taskManager.removeTaskById(id);
                sendText(exchange, "Task with id " + id + " deleted", 200);
            } else {
                sendText(exchange, "Task not found", 404);
            }
        } else {
            sendNotFound(exchange);
        }
    }
}
