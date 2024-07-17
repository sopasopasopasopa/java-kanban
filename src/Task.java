
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

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{"
                + "nameTask = " + nameTask + '\''
                + ", description = " + descriptionTask
                + ", taskId = " + taskId
                + ", status = " + status
                + '}';
    }

    enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }
}
