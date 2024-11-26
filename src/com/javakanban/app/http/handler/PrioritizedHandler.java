package com.javakanban.app.http.handler;

import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Task;
import com.javakanban.app.util.GsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler{
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetPrioritizedTasks(exchange);
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        String response = GsonUtil.getGson().toJson(prioritizedTasks);
        sendText(exchange, response, 200);
    }
}

