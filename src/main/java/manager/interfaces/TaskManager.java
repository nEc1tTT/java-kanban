package manager.interfaces;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(SubTask subTask);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    List<Integer> getSubTaskList(int epicId);

    Map<Integer, Task> getTasksHashMap();

    Map<Integer, Epic> getEpicsHashMap();

    Map<Integer, SubTask> getSubtasksHashMap();

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    List<Task> getHistory();

    HistoryManager getHistoryManager();
}