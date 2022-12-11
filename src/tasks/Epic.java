package tasks;

import service.Status;
import service.TaskType;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(int id, String name, Status status, String description) {
        super(id, name, description, null);
        this.taskType = TaskType.EPIC;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tasks.Epic{" + "subtasks=" + subtasks +
                " name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +'}';
    }
}
