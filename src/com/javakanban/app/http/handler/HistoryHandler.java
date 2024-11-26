package com.javakanban.app.http.handler;

import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Task;
import com.javakanban.app.util.GsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetHistory(exchange);
        } else {
            sendMethodNotAllowed(exchange);
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        String response;
        if (history.isEmpty()) {
            response = "История пуста!";
        } else {
            response = GsonUtil.getGson().toJson(history);
        }
        sendText(exchange, response, 200);
    }
}