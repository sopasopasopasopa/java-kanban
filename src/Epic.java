import java.util.HashMap;

public class Epic extends Task{

    private HashMap<Integer, Task> subtasks;

    public Epic(String nameTask, String descriptionTask, int id, Status status) {
        super(nameTask, descriptionTask, status);
        this.subtasks = new HashMap<>();
    }

}
