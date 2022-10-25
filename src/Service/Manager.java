package Service;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {


    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    public static int i = 0;

    public void printHashmapsObject() {
        if (taskHashMap.isEmpty()) {
            System.out.println("Объекты TASK не найдены");
        } else {
            for (Task task : taskHashMap.values()) {
                System.out.println(task);
            }
        }

        if (epicHashMap.isEmpty()) {
            System.out.println("Объекты EPIC не найдены");
        } else {
            for (Epic epic : epicHashMap.values()) {
                System.out.println(epic);
            }
        }
        if (subtaskHashMap.isEmpty()) {
            System.out.println("Объекты SUBTASK не найдены");
        } else {
            for (Subtask subtask : subtaskHashMap.values()) {
                System.out.println(subtask);
            }
        }
        System.out.println("Конец проверки №" + i++);
    }

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
        for (Epic epic : epicHashMap.values()) {
            epic.getSubtasks().clear();
            updateStatusEpic(epic);
        }
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
        Subtask subtask = subtaskHashMap.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epicHashMap.get(subtask.getEpicId());
        epic.getSubtasks().remove(id);
        updateStatusEpic(epic);
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
        updateStatusEpic(epic);
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
        updateStatusEpic(epic);
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
