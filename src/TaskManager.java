import java.util.HashMap;

public class TaskManager {

    int id;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public TaskManager() {
        this.id = 1;
    }

    public int generatedId() {
        return id++;
    }

    public void createTask(Task task) {
        int taskId = generatedId();
        task.setTaskId(taskId);
        tasks.put(taskId, task);
    }

    public HashMap<Integer, Subtask> getAllSubtasksOfEpics(int epicId) {
    }


}
