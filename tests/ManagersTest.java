import com.javakanban.app.manager.*;
import com.javakanban.app.model.*;
import org.junit.jupiter.api.Test;

import java.io.*;


import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultShouldInitializeInMemoryTaskManager() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault(), "утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров");
    }

    @Test
    void getDefaultHistoryShouldInitializeInMemoryHistoryManager() {
        assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory(), "утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров");
    }

    @Test
    void testSaveEmptyFile() throws IOException, ManagerSaveException {
        File tempFile = File.createTempFile("file", ".txt");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);
        taskManager.save();

        try (BufferedReader reader = new BufferedReader((new FileReader(tempFile)))){
            String line;
            assertNull(reader.readLine());
        }

    }

    @Test
    void testSaveMultipleTasks() throws IOException {
        File tempFile = File.createTempFile("file", ".txt");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

        Task task1 = new Task(1, "task1", "task number one");
        Task task2 = new Task(2, "task2", "task number two");

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertEquals(2, taskManager.getAllTasks().size());

        assertEquals(task1, taskManager.getTaskById(task1.getTaskId()));
        assertEquals(task2, taskManager.getTaskById(task2.getTaskId()));

    }

    @Test
    void testLoadEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("file", ".txt");
        emptyFile.deleteOnExit();

        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(emptyFile);

        assertTrue(taskManager.getAllTasks().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
        assertTrue(taskManager.getAllEpics().isEmpty());

    }

    @Test
    void testLoadMultipleTasks() throws IOException {
        File tempFile = File.createTempFile("file", ".txt");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(tempFile);

        Task task1 = new Task(1, "task1", "task number one");
        Epic epic1 = new Epic(2,"epic1","epic number one");
        Subtask subtask1 = new Subtask(3,"suptask1", "subtask number one", 2);

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(epic1, subtask1);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getAllSubtasks().size());


    }

}