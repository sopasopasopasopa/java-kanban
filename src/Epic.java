import java.util.HashMap;

public class Epic extends Task{

    HashMap<Integer, Subtask> subtasks;

    public Epic(String nameTask, String descriptionTask, Status status) {
        super(nameTask, descriptionTask, status);
        this.subtasks = new HashMap<>();
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
    }
}
