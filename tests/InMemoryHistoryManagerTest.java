import com.javakanban.app.manager.InMemoryHistoryManager;
import com.javakanban.app.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    @Test
    void testGetHistoryEmpty() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void testAddTaskDuplicates() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("task1","task number one");
        historyManager.addTask(task1);
        historyManager.addTask(task1);
        assertEquals(1,historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().getFirst());
    }

    @Test
    void testAddTaskMaxSize() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        for (int i = 0; i <= 11; i++) {
            historyManager.addTask(new Task("task" + i, "task number" + i, i));
        }
        assertEquals(10,historyManager.getHistory().size());
    }

    @Test
    void testRemoveBeginning() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("task1", "task number one", 1);
        Task task2 = new Task("task2", "task number two", 2);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.remove(task1.getTaskId());
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task2,historyManager.getHistory().getFirst());
    }

    @Test
    void testRemoveMiddle() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("task1", "task number one", 1);
        Task task2 = new Task("task2", "task number two", 2);
        Task task3 = new Task("task3", "task number three", 3);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.remove(task2.getTaskId());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1,historyManager.getHistory().getFirst());
        assertEquals(task3, historyManager.getHistory().get(1));
    }

    @Test
    void testRemoveEnd() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("task1", "task number one", 1);
        Task task2 = new Task("task2", "task number two", 2);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.remove(task2.getTaskId());
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task1,historyManager.getHistory().getFirst());
    }

    @Test
    void testRemoveNonExistent() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty());
    }
}
