import com.javakanban.app.manager.InMemoryTaskManager;
import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Status;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void testGetTaskById() {
        Task task1 = new Task(1, "task1", "task number one", Duration.ofHours(1), LocalDateTime.now());
        taskManager.createTask(task1);
        assertEquals(task1, taskManager.getTaskById(1));
        assertNull(taskManager.getTaskById(2));
    }

    @Test
    void testGetEpicById() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        taskManager.createEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(1));
        assertNull(taskManager.getEpicById(2));
    }

    @Test
    void testGetSubtaskById() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2, "subtask1", "subtask number one", Duration.ZERO, LocalDateTime.now(),1);
        taskManager.createSubtask(epic1, subtask1);
        assertEquals(subtask1, taskManager.getSubtaskById(2));
        assertNull(taskManager.getSubtaskById(3));
    }

    @Test
    void testUpdateTask() {
        Task task1 = new Task(1, "task1", "task number one", Duration.ofHours(1), LocalDateTime.now());
        taskManager.createTask(task1);
        Task taskUpdated = new Task(1, "updated task1", "updated task number one", Duration.ofHours(2), LocalDateTime.now().plusDays(1));
        taskManager.updateTask(taskUpdated);
        assertEquals(taskUpdated, taskManager.getTaskById(1));
    }

    @Test
    void testUpdateEpic() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        taskManager.createEpic(epic1);
        Epic epicUpdated = new Epic(1,"updated epic1", "updated epic number one", Duration.ofHours(2), LocalDateTime.now().plusDays(1));
        taskManager.updateEpic(epicUpdated);
        assertEquals(epicUpdated, taskManager.getEpicById(1));
    }

    @Test
    void testUpdatedSubtask() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2, "subtask1", "subtask number one", Duration.ZERO, LocalDateTime.now(),1);
        taskManager.createSubtask(epic1, subtask1);
        Subtask subtaskUpdated = new Subtask(2, "updated subtask1", "updated subtask number one", Duration.ofHours(2), LocalDateTime.now().plusDays(1),1);
        taskManager.updateSubtask(subtaskUpdated);
        assertEquals(subtaskUpdated, taskManager.getSubtaskById(2));
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void testCreateTask() {
        Task task1 = new Task(1, "task1", "task number one", Duration.ofHours(1), LocalDateTime.now());
        taskManager.createTask(task1);
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void testCreateEpic() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        taskManager.createEpic(epic1);
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void testCreateSubtask() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2, "subtask1", "subtask number one", Duration.ZERO, LocalDateTime.now(),1);
        taskManager.createSubtask(epic1, subtask1);
        assertEquals(1, taskManager.getAllSubtasks().size());
        assertEquals(1, taskManager.getAllSubtasksForEpic(epic1).size());
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void testRemoveTasks() {
        Task task1 = new Task(1, "task1", "task number one", Duration.ofHours(1), LocalDateTime.now());
        taskManager.createTask(task1);
        taskManager.removeTasks();
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @Test
    void testRemoveEpics() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        taskManager.createEpic(epic1);
        taskManager.removeEpics();
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @Test
    void testRemoveSubtasks() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2, "subtask1", "subtask number one", Duration.ZERO, LocalDateTime.now(),1);
        taskManager.createSubtask(epic1,subtask1);
        taskManager.removeSubtasks();
        assertEquals(0, taskManager.getAllSubtasks().size());
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task(1, "task1", "task number one", Duration.ofHours(1), LocalDateTime.now());
        taskManager.createTask(task1);
        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    void testGetAllEpics() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        taskManager.createEpic(epic1);
        assertEquals(1, taskManager.getAllEpics().size());
    }

    @Test
    void testGetAllSubtasks() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2, "subtask1", "subtask number one", Duration.ZERO, LocalDateTime.now(),1);
        taskManager.createSubtask(epic1,subtask1);
        assertEquals(1, taskManager.getAllSubtasks().size());
    }

    @Test
    void testGetAllSubtasksForEpic() {
        Epic epic1 = new Epic(1,"epic1", "epic number one", Duration.ZERO, LocalDateTime.now());
        Subtask subtask1 = new Subtask(2, "subtask1", "subtask number one", Duration.ZERO, LocalDateTime.now(),1);
        taskManager.createSubtask(epic1,subtask1);
        assertEquals(1, taskManager.getAllSubtasksForEpic(epic1).size());
    }

    @Test
    void testOverlappingTasks() {
        LocalDateTime start1 = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2024,1, 1, 11, 0);
        LocalDateTime start2 = LocalDateTime.of(2024, 1, 1, 10,30);
        LocalDateTime end2 = LocalDateTime.of(2024,1,1, 12,30);

    }

}
