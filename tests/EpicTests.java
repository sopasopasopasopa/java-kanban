import com.javakanban.app.manager.InMemoryHistoryManager;
import com.javakanban.app.manager.InMemoryTaskManager;
import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Status;
import com.javakanban.app.model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTests {

    private Epic epic;
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        epic = new Epic("epic", "description");
    }

    @Test
    void testAllSubtasksNew() {
        Subtask subtask1 = new Subtask("subtask1", "subtask number one", epic.getTaskId());
        subtask1.setStatus(Status.NEW);
        Subtask subtask2 = new Subtask("subtask2", "subtask number two", epic.getTaskId());
        subtask2.setStatus(Status.NEW);

        taskManager.createSubtask(epic, subtask1);
        taskManager.createSubtask(epic, subtask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void testAllSubtasksDone() {
        Subtask subtask1 = new Subtask("subtask1", "subtask number one", epic.getTaskId());
        subtask1.setStatus(Status.DONE);
        subtask1.setDuration(Duration.ZERO);
        Subtask subtask2 = new Subtask("subtask2", "subtask number two", epic.getTaskId());
        subtask2.setStatus(Status.DONE);
        subtask2.setDuration(Duration.ZERO);

        taskManager.createSubtask(epic, subtask1);
        taskManager.createSubtask(epic, subtask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void testMixedSubtasksNewAndDone() {
        Subtask subtask1 = new Subtask("subtask1", "subtask number one", epic.getTaskId());
        subtask1.setStatus(Status.NEW);
        subtask1.setDuration(Duration.ZERO);
        Subtask subtask2 = new Subtask("subtask2", "subtask number two", epic.getTaskId());
        subtask2.setStatus(Status.DONE);
        subtask2.setDuration(Duration.ZERO);

        taskManager.createSubtask(epic, subtask1);
        taskManager.createSubtask(epic, subtask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void testAllSubtasksInProgress() {
        Subtask subtask1 = new Subtask("subtask1", "subtask number one", epic.getTaskId());
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setDuration(Duration.ZERO);
        Subtask subtask2 = new Subtask("subtask2", "subtask number two", epic.getTaskId());
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask2.setDuration(Duration.ZERO);

        taskManager.createSubtask(epic, subtask1);
        taskManager.createSubtask(epic, subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testEmptyEpic() {
        assertEquals(Status.NEW, epic.getStatus());
    }
}
