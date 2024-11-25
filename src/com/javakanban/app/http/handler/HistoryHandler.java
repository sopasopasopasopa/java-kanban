package com.javakanban.app.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javakanban.app.adapter.DurationAdapter;
import com.javakanban.app.adapter.LocalDateTimeAdapter;
import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetHistory(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        String response;
        if (history.isEmpty()) {
            response = "История пуста!";
        } else {
            response = gson.toJson(history);
        }
        sendText(exchange, response, 200);
    }
}