public class Subtask extends Task{
    private int epicId; // Вопрос по связи?

    public Subtask(int epicId, String name, String description, Status status) {
        super(name, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                '}';
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
