import com.javakanban.app.manager.HistoryManager;
import com.javakanban.app.manager.InMemoryTaskManager;
import com.javakanban.app.model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest {
    HistoryManager historyManager;
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);

    @Test
    void testIsOverlapNoOverlap() {
        LocalDateTime start1 = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDateTime start2 = LocalDateTime.of(2024, 1, 1, 11, 30);
        LocalDateTime end2 = LocalDateTime.of(2024, 1, 1, 12, 0);
        assertFalse(taskManager.isOverlap(start1, end1, start2, end2));
    }

    @Test
    void testIsOverlapOverlap() {
        LocalDateTime start1 = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDateTime start2 = LocalDateTime.of(2024, 1, 1, 10, 30);
        LocalDateTime end2 = LocalDateTime.of(2024, 1, 1, 11, 30);
        assertTrue(taskManager.isOverlap(start1, end1, start2, end2));
    }

    @Test
    void testIsOverlapTouching() {
        LocalDateTime start1 = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDateTime start2 = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDateTime end2 = LocalDateTime.of(2024, 1, 1, 12, 0);
        assertTrue(taskManager.isOverlap(start1, end1, start2, end2));
    }

    @Test
    void testIsOverlap_nullValues() {
        assertFalse(taskManager.isOverlap(null, null, null, null));
        assertFalse(taskManager.isOverlap(LocalDateTime.now(), LocalDateTime.now(), null, null));
        assertFalse(taskManager.isOverlap(null, null, LocalDateTime.now(), LocalDateTime.now()));
    }


    @Test
    void testHasOverlappingTasksNoOverlap() {
        Task task1 = new Task(1,"task1", "task number one", Duration.ofHours(1), LocalDateTime.of(2024, 1, 1, 10, 0));
        Task task2 = new Task(1, "Task 2", "task number two", Duration.ofHours(1), LocalDateTime.of(2024, 1, 1, 11, 30));
        taskManager.createTask(task1);
        assertFalse(taskManager.hasOverlappingTasks(task2));
    }

    @Test
    void testHasOverlappingTasksOverlap() {
        Task task1 = new Task(1,"task1", "task number one", Duration.ofHours(1), LocalDateTime.of(2024, 1, 1, 10, 0));
        Task task2 = new Task(1, "Task 2", "task number two", Duration.ofHours(1), LocalDateTime.of(2024, 1, 1, 10, 30));
        taskManager.createTask(task1);
        assertTrue(taskManager.hasOverlappingTasks(task2));
    }

}
