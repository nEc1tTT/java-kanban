package tasks;

import service.Status;
import service.TaskType;

public class Task {


    private String name;
    private String description;
    private int id;
    public Status status;
    public TaskType taskType;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
    }

    @Override
    public String toString() {
        return "Tasks.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, taskType, name, status, description, "");
    }
}
