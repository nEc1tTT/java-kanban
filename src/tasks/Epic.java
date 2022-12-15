package tasks;

import service.Status;
import service.TaskType;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(int id, String name, Status status, String description) {
        super(id, name, description, status);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s", id, TaskType.EPIC.name(), name, status, description, "");
    }
}
