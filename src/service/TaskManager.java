package service;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    public void printHashmapsObject();

    public List<Task> getTasks();

    public List<Epic> getEpics();

    public List<Subtask> getSubtasks();

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

    public List<Subtask> getSubtaskByEpic(int id);

    public void updateStatusEpic(Epic epic);

    List<Task> getHistory();
}
