package tasks;

import service.Status;
import service.TaskType;

public class Subtask extends Task {
    private Epic epic;
    private int epicId; // Вопрос по связи?

    public Subtask(int epicId, String name, String description, Status status) {
        super(name, description, status);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, null);
        this.status = status;
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;

    }

    @Override
    public String toString() {
        return "Tasks.Subtask{" + "epicId=" + epicId +
                " name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +'}';
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
