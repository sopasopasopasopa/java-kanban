import com.javakanban.app.http.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private static final int PORT = 8080;
    private HttpTaskServer server;

    @BeforeEach
    void startServer() throws IOException {
        server = new HttpTaskServer();
        server.start();

        createSampleTask("task1", "task number one");
        createSampleTask("task2", "task number two");
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void testGetAllTasks() throws IOException {
        int responseCode = sendRequest("GET", "/tasks");
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        String jsonResponse = getResponseContent("/tasks");

        assertTrue(jsonResponse.contains("\"taskId\":1"), "Response should contain taskId 1");
        assertTrue(jsonResponse.contains("\"taskId\":2"), "Response should contain taskId 2");
    }

    @Test
    void testGetTaskById() throws IOException {
        int taskId = createSampleTask("task1", "task number one");
        int responseCode = sendRequest("GET", "/tasks?id=" + taskId);
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        String jsonResponse = getResponseContent("/tasks?id=" + taskId);
        assertTrue(jsonResponse.contains("\"taskId\":1"), "Response should contain taskId 1");
    }

    @Test
    void testGetTaskByIdNotFound() throws IOException {
        int responseCode = sendRequest("GET", "/tasks?id=9999");
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, responseCode);
    }

    @Test
    void testAddTask() throws IOException {
        String jsonTask = "{ \"name\": \"task1\", \"description\": \"task number one\", \"status\": \"NEW\" }";
        int responseCode = sendRequest("POST", "/tasks", jsonTask);
        assertEquals(HttpURLConnection.HTTP_CREATED, responseCode);
    }

    @Test
    void testDeleteTask() throws IOException {
        int taskId = createSampleTask("task1", "task number one");
        int responseCode = sendRequest("DELETE", "/tasks?id=" + taskId);
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }

    @Test
    void testDeleteTaskNotFound() throws IOException {
        int responseCode = sendRequest("DELETE", "/tasks?id=9999");
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, responseCode);
    }

    @Test
    void testGetHistory() throws IOException {

        int taskId1 = createSampleTask("task1", "task number one");
        int taskId2 = createSampleTask("task2", "task number two");

        sendRequest("GET", "/tasks?id=" + taskId1);
        sendRequest("GET", "/tasks?id=" + taskId2);

        int responseCode = sendRequest("GET", "/history");
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        String jsonResponse = getResponseContent("/history");
        assertTrue(jsonResponse.contains("\"taskId\":" + taskId1), "Response should contain taskId " + taskId1);
        assertTrue(jsonResponse.contains("\"taskId\":" + taskId2), "Response should contain taskId " + taskId2);
    }

    @Test
    void testGetHistoryEmpty() throws IOException {

        int responseCode = sendRequest("GET", "/history");
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);

        String jsonResponse = getResponseContent("/history");
        assertEquals("История пуста!", jsonResponse);
    }

    private int sendRequest(String method, String endpoint) throws IOException {
        return sendRequest(method, endpoint, null);
    }

    private int sendRequest(String method, String endpoint, String jsonInputString) throws IOException {
        URL url = new URL("http://localhost:" + PORT + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if (jsonInputString != null) {
            connection.setDoOutput(true);
            connection.getOutputStream().write(jsonInputString.getBytes());
        }
        return connection.getResponseCode();
    }

    private String getResponseContent(String endpoint) throws IOException {
        URL url = new URL("http://localhost:" + PORT + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        }
    }

    private int createSampleTask(String name, String description) throws IOException {
        String jsonTask = String.format("{ \"name\": \"%s\", \"description\": \"%s\", \"status\": \"NEW\" }", name, description);
        sendRequest("POST", "/tasks", jsonTask);
        return 1;
    }
}
