import com.javakanban.app.manager.FileBackedTaskManager;
import com.javakanban.app.manager.ManagerSaveException;
import com.javakanban.app.model.Epic;
import com.javakanban.app.model.Subtask;
import com.javakanban.app.model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    void testLoadFromFileIOException() {
        assertThrows(IOException.class, () -> {
            FileBackedTaskManager.loadFromFile(new File("non-existent-file.txt"));
        }, "Загрузка из несуществующего файла должна вызывать IOException");
    }

    @Test
    void testSaveIOException() {
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("test-file.txt"));
        manager.createTask(new Task(1,"task1", "task number one", null, null));
        manager.getFile().setWritable(false);
        assertThrows(ManagerSaveException.class, () -> {
            manager.save();
        }, "Сохранение в файл без разрешения на запись должно вызывать ManagerSaveException");
    }

    @Test
    void testCreateTask_SaveException() {
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("test-file.txt"));
        // Устанавливаем недействительное разрешение файла, чтобы вызвать IOException при сохранении
        manager.getFile().setWritable(false);

        assertThrows(RuntimeException.class, () -> {
            manager.createTask(new Task(1,"task1", "task number one", null, null));
        }, "Создание задачи должно выбросить RuntimeException из-за ошибки сохранения");

        manager.getFile().setWritable(true); // Восстановление разрешения
    }

    @Test
    void testCreateSubtask_SaveException() {
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("test-file.txt"));
        Epic epic = new Epic(1, "epic1", "epic number one", null, null);
        manager.createEpic(epic);

        manager.getFile().setWritable(false);

        assertThrows(RuntimeException.class, () -> {
            manager.createSubtask(epic, new Subtask(1,"subtask1", "subtask number one", null, null, epic.getTaskId()));
        }, "Создание подзадачи должно выбросить RuntimeException из-за ошибки сохранения");
        manager.getFile().setWritable(true); // Восстановление разрешения
    }

    @Test
    void testCreateEpic_SaveException() {
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("test-file.txt"));
        manager.getFile().setWritable(false);

        assertThrows(RuntimeException.class, () -> {
            manager.createEpic(new Epic(1, "epic1", "epic number one", null, null));
        }, "Создание эпика должно выбросить RuntimeException из-за ошибки сохранения");
        manager.getFile().setWritable(true); // Восстановление разрешения

    }

    @Test
    void testCreateTask_Success() {
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("test-file.txt"));
        assertDoesNotThrow(() -> {
            manager.createTask(new Task(1,"task1", "task number one", null, null));
        }, "Создание задачи должно пройти успешно");

    }
}
