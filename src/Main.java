public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("Epic 1", "Description for Epic 1", Task.Status.NEW);

        Subtask subtask1 = new Subtask("Subtask 1", "Description for Subtask 1", Task.Status.NEW);
        Subtask subtask2 = new Subtask("Subtask 2", "Description for Subtask 2", Task.Status.NEW);


        epic1.addSubtask(subtask1);
        epic1.addSubtask(subtask2);

        taskManager.createEpic(epic1);

        System.out.println(epic1);
        System.out.println(taskManager.getAllSubtasksOfEpic(epic1.getTaskId()));
    }
}
