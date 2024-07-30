import com.javakanban.app.manager.InMemoryHistoryManager;
import com.javakanban.app.manager.InMemoryTaskManager;
import com.javakanban.app.manager.Managers;
import org.junit.jupiter.api.Test;

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
}