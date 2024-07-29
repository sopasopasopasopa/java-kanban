package com.javakanban.app.tests;

import com.javakanban.app.manager.InMemoryTaskManager;
import com.javakanban.app.manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultShouldInitializeInMemoryTaskManager() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault(), "");
    }

    @Test
    void getDefaultHistoryShouldInitializeInMemoryHistoryManager() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefaultHistory(), "");
    }
}