package com.javakanban.app.tests;

import com.javakanban.app.manager.Managers;
import com.javakanban.app.manager.TaskManager;
import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskEqualityById() {
        Task task1 = new Task(1, "task1", "task number one");
        Task task2 = new Task(1,"task1", "task number one");
        assertEquals(task1, task2, "");
    }

    @Test
    void subtaskEqualityById() {
        Subtask subtask1 = new Subtask(3, "subtask1", "subtask number one", 2);
        Subtask subtask2 = new Subtask(3, "subtask1", "subtask number one", 2);
        assertEquals(subtask1, subtask2, "");
    }

    @Test
    void epicEqualityById() {
        Epic epic1 = new Epic(2, "epic1", "epic number one");
        Epic epic2 = new Epic(2, "epic1", "epic number one");
        assertEquals(epic1, epic2, "");
    }

    @Test
    void epicCannotAddItselfAsSubtask() {
        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic(1, "epic1", "epic number one");
        manager.createEpic(epic1);
        Subtask subtask = new Subtask(1, "epic1", "epic number one", 1);
        manager.createSubtask(epic1, subtask);
        assertFalse(manager.getAllEpics().contains(subtask), "");
    }

    @Test
    void subtaskCannotBeItsOwnEpic() {
        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic(1, "epic1", "epic number one");
        manager.createEpic(epic1);
        Subtask subtask = new Subtask(1, "epic1", "epic number one", 1);
        manager.createSubtask(epic1, subtask);
        assertFalse(manager.getAllSubtasks().contains(epic1), "");
    }

//    @Test
//    public void utilityClassReturnsInitializedManagers() {
//
//    }
//
//    @Test
//    public void inMemoryTaskManagerAddsDifferentTaskTypes() {
//
//    }
//
    @Test
    public void tasksWithSameIdDoNotConflict() {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task(1, "task1", "task number one");
        manager.createTask(task1);
        Task[] arrayOne = new Task[]{task1};
        Task task2 = new Task("task2", "task number two");
        Task[] arrayTwo = new Task[]{task2};
        assertArrayEquals(arrayOne, arrayTwo, "");
    }
//
//    @Test
//    public void taskImmutabilityUponAdditionToManager() {
//
//    }
//
//    @Test
//    public void historyManagerPreservesPreviousTaskVersions() {
//
//    }
}