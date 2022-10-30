package Service;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    public void printHashmapsObject();

    public ArrayList<Task> getTasks();

    public ArrayList<Epic> getEpics();

    public ArrayList<Subtask> getSubtasks();

    public void deleteTasks();

    public void deleteEpics();

    public void deleteSubtasks();

    public void deleteTask(int id);

    public void deleteEpic(int id);

    public void deleteSubTask(int id);

    public Task getTaskById(int id);

    public Epic getEpicById(int id);

    public Subtask getSubTaskById(int id);

    public int createTask(Task task);

    public int createEpic(Epic epic);

    public int createSubtask(Subtask subtask);

    public void updateTask(Task task);

    public void updateEpic(Epic epic);

    public void updateSubtask(Subtask Subtask);

    public ArrayList<Subtask> getSubtaskByEpic(int id);

    public void updateStatusEpic(Epic epic);

    List<Task> getHistory();
}
