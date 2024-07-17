import java.util.HashMap;

public class Epic extends Task{

    HashMap<Integer, Subtask> subtasks;

    public Epic(String nameTask, String descriptionTask, Status status) {
        super(nameTask, descriptionTask, status);
        this.subtasks = new HashMap<>();
    }

    public void addSubtasks(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
    }
}
