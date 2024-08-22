import com.javakanban.app.manager.*;
import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Task;
import com.javakanban.app.manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private InMemoryHistoryManager historyManager;

    @Test
    void taskEqualityById() {
        Task task1 = new Task(1, "task1", "task number one");
        Task task2 = new Task(1, "task1", "task number one");
        assertEquals(task1, task2, "экземпляры класса Task равны друг другу, если равен их id");
    }

    @Test
    void subtaskEqualityById() {
        Subtask subtask1 = new Subtask(3, "subtask1", "subtask number one", 2);
        Subtask subtask2 = new Subtask(3, "subtask1", "subtask number one", 2);
        assertEquals(subtask1, subtask2, "наследники класса Task равны друг другу, если равен их id");
    }

    @Test
    void epicEqualityById() {
        Epic epic1 = new Epic(2, "epic1", "epic number one");
        Epic epic2 = new Epic(2, "epic1", "epic number one");
        assertEquals(epic1, epic2, "наследники класса Task равны друг другу, если равен их id");
    }

    @Test
    void epicCannotAddItselfAsSubtask() {
        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic(1, "epic1", "epic number one");
        manager.createEpic(epic1);
        Subtask subtask = new Subtask(1, "epic1", "epic number one", 1);
        manager.createSubtask(epic1, subtask);
        assertFalse(manager.getAllEpics().contains(subtask), "объект Epic нельзя добавить в самого себя в виде подзадачи");
    }

    @Test
    void subtaskCannotBeItsOwnEpic() {
        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic(1, "epic1", "epic number one");
        manager.createEpic(epic1);
        Subtask subtask = new Subtask(1, "epic1", "epic number one", 1);
        manager.createSubtask(epic1, subtask);
        assertFalse(manager.getAllSubtasks().contains(epic1), "объект Subtask нельзя сделать своим же эпиком;");
    }

    @Test
    public void taskImmutabilityUponAdditionToManager() {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task(1, "task1", "task number one");
        manager.createTask(task1);
        Task[] arrayOne = new Task[]{task1};
        Task task2 = manager.getTaskById(1);
        Task[] arrayTwo = new Task[]{task2};
        assertArrayEquals(arrayOne, arrayTwo, "задачи (по всем полям) при добавлении задачи в менеджер неизменны");
    }

    @Test
    void inMemoryTaskManagerAddsDifferentTaskTypes() {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task(1, "task1", "task number one");
        Epic epic1 = new Epic(2,"epic1", "epic number one");
        Subtask subtask1 = new Subtask(3, "subtask1", "subtask number one", 2);

        manager.createTask(task1);
        manager.createEpic(epic1);
        manager.createSubtask(epic1, subtask1);

        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubtaskById(3);

        List<Task> history = manager.getHistory();

        assertEquals(3, history.size());
        assertEquals(task1, history.getFirst());
        assertEquals(epic1, history.get(1));
        assertEquals(subtask1, history.get(2));
    }

    @Test
    public void tasksWithSameIdDoNotConflict() {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task(1, "task1", "task number one");
        manager.createTask(task1);

        Task task2 = new Task("task2", "task number two");
        manager.createTask(task2);

        Task retrievedTask1 = manager.getTaskById(1);
        Task retrievedTask2 = manager.getTaskById(task2.getTaskId());

        assertNotEquals(retrievedTask1.getTaskId(), retrievedTask2.getTaskId());
        assertEquals("task1", retrievedTask1.getNameTask());
        assertEquals("task number one", retrievedTask1.getDescriptionTask());

        assertEquals("task2", retrievedTask2.getNameTask());
        assertEquals("task number two", retrievedTask2.getDescriptionTask());

    }

    @Test
    public void historyManagerPreservesPreviousTaskVersions() {
        TaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = new Task(1, "task1", "task number one");
        manager.createTask(task1);
        manager.getTaskById(1);
        List<Task> historyAfterFirstAdd = manager.getHistory();

        Task task2 = new Task(2, "task1 updated", "updated description");
        manager.createTask(task2);
        manager.getTaskById(2);
        List<Task> historyAfterSecondAdd = manager.getHistory();

        assertEquals(task1, historyAfterSecondAdd.get(0), "Первая версия задачи должна быть сохранена в истории");
        assertEquals(task2, historyAfterSecondAdd.get(1), "Вторая версия задачи должна быть сохранена в истории");
    }


    @BeforeEach
    void setup() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddTask() {
        Task task1 = new Task(1, "task1", "task number one");
        Task task2 = new Task(2, "task2", "task number two");

        historyManager.addTask(task1);
        historyManager.addTask(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));

    }

    @Test
    void testAddDublicateTask() {
        Task task1 = new Task(1, "task1", "task number one");
        Task task2 = new Task(1, "task1 updated", "task number one updated");

        historyManager.addTask(task1);
        historyManager.addTask(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(1,history.size());
        assertEquals(task2, history.getFirst());
    }

    @Test
    void testRemoveTask() {
        Task task1 = new Task(1, "task1", "task number one");
        Task task2 = new Task(2, "task2", "task number two");

        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.getFirst());
    }

    @Test
    void testRemoveNonExistenTask() {
        Task task1 = new Task(1, "task1", "task number one");
        historyManager.addTask(task1);

        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(1,history.size());
    }

    @Test
    void testHistoryLimit() {
        for (int i = 0; i < 15; i++) {
            Task task1 = new Task(i, "task " + i , "task number " + i);
            historyManager.addTask(task1);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size());
        assertEquals(new Task(5, "task 5", "task number 5"), history.getFirst());
    }

}