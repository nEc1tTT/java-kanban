import java.util.ArrayList;
import java.util.HashMap;

public class Manager {


    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    private static int generateId = 1;

    public static int getId() {
        return generateId;
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicHashMap.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskHashMap.values());
    }

    public void deleteTasks() {
        taskHashMap.clear();
    }

    public void deleteEpics() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    public void deleteSubtasks() {
        subtaskHashMap.clear();
    }

    public void deleteTask(int id) {
        taskHashMap.remove(id);
    }

    public void deleteEpic(int id) {
        epicHashMap.remove(id);
        HashMap<Integer, Subtask> cloneSubtaskHashMap = new HashMap<>(subtaskHashMap);
        for (Subtask subtask : cloneSubtaskHashMap.values()) {
            if (id == subtask.getEpicId()) {
                subtaskHashMap.remove(subtask.getId());
            }
        }
    }

    public void deleteSubTask(int id) {
        subtaskHashMap.remove(id);
    }

    public Task getTaskById(int id) {
        return taskHashMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicHashMap.get(id);
    }

    public Subtask getSubTaskById(int id) {
        return subtaskHashMap.get(id);
    }

    public int createTask(Task task) {
        int id = generateId++;
        task.setId(id);
        taskHashMap.put(id, task);
        return id;
    }

    public int createEpic(Epic epic) {
        int id = generateId++;
        epic.setId(id);
        epicHashMap.put(id, epic);
        return id;
    }

    public int createSubtask(Subtask subtask) {
        int id = generateId++;
        Epic epic = epicHashMap.get(subtask.getEpicId());
        epic.getSubtasks().add(subtask);
        subtask.setId(id);
        subtaskHashMap.put(id, subtask);
        return id;
    }

    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask Subtask) {
        subtaskHashMap.put(Subtask.getId(), Subtask);
    }

    public ArrayList<Subtask> getSubtaskByEpic(int id) {
        return epicHashMap.get(id).getSubtasks();
    }

    public void updateStatusEpic(Epic epic) {
        boolean statusSubtasksNew = false;
        boolean statusSubtasksInProgress = false;
        boolean statusSubtasksDone = false;
        for (Subtask subtask : epic.getSubtasks()) {
            if (subtask.getStatus() == Status.NEW) {
                statusSubtasksNew = true;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                statusSubtasksInProgress = true;
            } else if (subtask.getStatus() == Status.DONE) {
                statusSubtasksDone = true;
            }
        }

        if (epic.getSubtasks().size() != 0) {
            if (!statusSubtasksInProgress && !statusSubtasksNew && statusSubtasksDone) {
                epic.setStatus(Status.DONE);
            } else if (!statusSubtasksInProgress && !statusSubtasksDone && statusSubtasksNew) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        } else {
            epic.setStatus(Status.NEW);
        }

    }
}
