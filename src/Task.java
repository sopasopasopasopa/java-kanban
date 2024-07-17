
public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected int taskId;
    protected Status status;

    public Task(String nameTask, String descriptionTask, Status status) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.status = status;
    }

    enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }
}
