import com.javakanban.app.manager.*;
import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskEqualityById() {
        Task task1 = new Task(1, "task1", "task number one");
        Task task2 = new Task(1,"task1", "task number one");
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
    void testVersionLinkedList() {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "task1", "task number one");
        Task task2 = new Task(2,"task2", "task number two");

        historyManager.addTask(task1);
        historyManager.addTask(task2);

        assertEquals(2, historyManager.getHistory().size());

        List<Task> history = historyManager.getHistory();
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void testSubtaskIntegrity() {
        Epic epic = new Epic(1, "epic1", "epic number one");
        Subtask subtask = new Subtask(2, "subtask1", "subtask number one", 1);

        epic.addSubtask(subtask);

        assertTrue(epic.getSubtasksList().contains(subtask));

        epic.removeSubtask(subtask);

        assertFalse(epic.getSubtasksList().contains(subtask));
    }


//    @Test
//    void inMemoryTaskManagerAddsDifferentTaskTypes() {
//        TaskManager manager = Managers.getDefault();
//        Task task1 = new Task(1, "task1", "task number one");
//        Epic epic1 = new Epic(2,"epic1", "epic number one");
//        Subtask subtask1 = new Subtask(3, "subtask1", "subtask number one", 2);
//        manager.createTask(task1);
//        manager.createEpic(epic1);
//        manager.createSubtask(epic1, subtask1);
//        manager.getTaskById(1);
//        manager.getEpicById(2);
//        manager.getSubtaskById(3);
//        assertEquals(3,manager.getHistory().size());
//    }

//    @Test
//    void tasksWithSameIdDoNotConflict() {
//        TaskManager manager = Managers.getDefault();
//        Task task1 = new Task(1, "task1", "task number one");
//        manager.createTask(task1);
//        Task[] arrayOne = new Task[]{task1};
//        Task task2 = new Task("task2", "task number two");
//        Task[] arrayTwo = new Task[]{task2};
//        assertArrayEquals(arrayOne, arrayTwo, "задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера");
//    }

    @Test
    void taskImmutabilityUponAdditionToManager() {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task(1,"task1", "task number one");
        manager.createTask(task1);
        Task[] arrayOne = new Task[]{task1};
        Task task2 = manager.getTaskById(1);
        Task[] arrayTwo = new Task[]{task2};
        assertArrayEquals(arrayOne, arrayTwo, "задачи (по всем полям) при добавлении задачи в менеджер неизменны");
    }

//    @Test
//    void historyManagerPreservesPreviousTaskVersions() {
//        TaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
//        Task task1 = new Task(1, "task1", "task number one");
//        manager.createTask(task1);
//        manager.getTaskById(1);
//        List<Task> historyAfterFirstAdd = manager.getHistory();
//
//        Task task2 = new Task(1, "task1 updated", "updated description");
//        manager.updateTask(task2);
//        manager.getTaskById(1);
//        List<Task> historyAfterSecondAdd = manager.getHistory();
//
//        assertEquals(task1, historyAfterSecondAdd.get(0), "Первая версия задачи должна быть сохранена в истории");
//        assertEquals(task2, historyAfterSecondAdd.get(1), "Вторая версия задачи должна быть сохранена в истории");
//    }
//
//    @Test
//    void taskUpdateShouldReflectInHistory() {
//        TaskManager manager = Managers.getDefault();
//        Task task1 = new Task(1, "task1", "task number one");
//
//        manager.createTask(task1);
//        manager.getTaskById(1);
//
//        task1.setNameTask("task1 updated");
//        manager.updateTask(task1);
//
//        List<Task> history = manager.getHistory();
//
//        assertEquals(1, history.size());
//        assertEquals("task1 updated", history.get(0).getNameTask());
//    }

}