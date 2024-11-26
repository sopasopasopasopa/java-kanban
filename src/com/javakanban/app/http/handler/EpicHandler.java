package com.javakanban.app.http.handler;

import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Epic;
import com.javakanban.app.util.GsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetEpic(exchange);
                break;
            case "POST":
                handlePostEpic(exchange);
                break;
            case "DELETE":
                handleDeleteEpic(exchange);
                break;
            default:
                sendMethodNotAllowed(exchange);
        }
    }

    private void handleGetEpic(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                String response = GsonUtil.getGson().toJson(epic);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            List<Epic> epics = taskManager.getAllEpics();
            String response = GsonUtil.getGson().toJson(epics);
            sendText(exchange, response, 200);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(),
                StandardCharsets.UTF_8))) {
            String json = reader.lines().collect(Collectors.joining());
            Epic epic = GsonUtil.getGson().fromJson(json, Epic.class);

            if (epic.getTaskId() > 0) {
                taskManager.updateEpic(epic);
                sendText(exchange, GsonUtil.getGson().toJson(epic), 200);
            } else {
                int id = taskManager.createEpic(epic);
                sendText(exchange, GsonUtil.getGson().toJson(epic), 201);
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                taskManager.removeEpicById(id);
                sendText(exchange, "Epic with id " + id + " deleted", 200);
            } else {
                sendText(exchange, "Epic not found", 404);
            }
        } else {
            sendNotFound(exchange);
        }
    }
}