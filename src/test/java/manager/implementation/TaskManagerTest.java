package java.manager.implementation;

import java.manager.interfaces.TaskManager;
import java.model.Epic;
import java.model.SubTask;
import java.model.Task;
import java.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;

    abstract T createTaskManager();

    @BeforeEach
    public void updateTakManager() {
        taskManager = createTaskManager();
    }

    @Test
    public void testingForEpicAndSubTask() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице",
                TaskStatus.NEW, 0, LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(10));
        SubTask subTask1 = new SubTask("a", "b", TaskStatus.NEW, 0,
                LocalDateTime.of(2022, 12, 21, 10, 45), Duration.ofMinutes(15));
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        TaskStatus epicsStatus = taskManager.getEpic(0).getStatus();
        assertEquals(epicsStatus.toString(), TaskStatus.NEW.toString());
        int idEpic = subTask.getEpicId();
        int idEpic1 = subTask1.getEpicId();
        assertTrue(taskManager.getEpicsHashMap().containsKey(idEpic));
        assertTrue(taskManager.getEpicsHashMap().containsKey(idEpic1));
        assertEquals(LocalDateTime.of(2022, 12, 21, 10, 25), subTask.getStartTime());
        assertEquals(Duration.ofMinutes(10), subTask.getDuration());
        assertEquals(LocalDateTime.of(2022, 12, 21, 10, 45), subTask1.getStartTime());
        assertEquals(Duration.ofMinutes(15), subTask1.getDuration());
    }

    @Test
    public void testingAddTest() {
        Task task = new Task("а", "b", LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(10));
        taskManager.createTask(task);
        assertEquals(taskManager.getTask(0).getTitle(), task.getTitle());
        assertEquals(taskManager.getTask(0).getDescription(), task.getDescription());
        assertEquals(LocalDateTime.of(2022, 12, 21, 10, 25), task.getStartTime());
        assertEquals(Duration.ofMinutes(10), task.getDuration());
    }

    @Test
    public void testingAddEpic() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут",
                LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(10));
        taskManager.createEpic(epic);
        assertEquals(taskManager.getEpic(0).getTitle(), epic.getTitle());
        assertEquals(taskManager.getEpic(0).getDescription(), epic.getDescription());
        assertEquals(taskManager.getEpic(0).getStatus(), TaskStatus.NEW);
        assertEquals(LocalDateTime.of(2022, 12, 21, 10, 25), epic.getStartTime());
        assertEquals(Duration.ofMinutes(10), epic.getDuration());
    }

    @Test
    public void testingAddSubTask() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут", LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(10));
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице",
                TaskStatus.NEW, 0, LocalDateTime.of(2022, 12, 21, 11, 25), Duration.ofMinutes(20));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subTask);
        assertEquals(taskManager.getSubTask(1).getTitle(), subTask.getTitle());
        assertEquals(taskManager.getSubTask(1).getDescription(), subTask.getDescription());
        assertEquals(taskManager.getSubTask(1).getStatus(), TaskStatus.NEW);
        assertEquals(LocalDateTime.of(2022, 12, 21, 11, 25), epic.getStartTime());
        assertEquals(Duration.ofMinutes(20), epic.getDuration());
    }

    @Test
    public void testingGetTask() {
        Task task = new Task("a", "b",
                LocalDateTime.of(2022, 12, 21, 11, 25), Duration.ofMinutes(15));
        Task task1 = new Task("c", "d",
                LocalDateTime.of(2022, 12, 21, 10, 25), Duration.ofMinutes(15));
        taskManager.createTask(task);
        taskManager.createTask(task1);
        Task task2 = taskManager.getTask(0);
        Task task3 = taskManager.getTask(1);
        assertEquals(task2.getTitle(), task.getTitle());
        assertEquals(task3.getTitle(), task1.getTitle());
        assertEquals(task2.getDescription(), task.getDescription());
        assertEquals(task3.getDescription(), task1.getDescription());
        assertEquals(task2.getStartTime(), task.getStartTime());
        assertEquals(task3.getStartTime(), task1.getStartTime());
        assertEquals(task2.getDuration(), task.getDuration());
        assertEquals(task3.getDuration(), task1.getDuration());
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        assertEquals(taskManager.getHistory().get(0).getId(), list.get(0));
        assertEquals(taskManager.getHistory().get(1).getId(), list.get(1));
        assertNull(taskManager.getTask(10));
    }

    @Test
    public void testingGetEpic() {
        Epic epic = new Epic("a", "b");
        Epic epic1 = new Epic("c", "d");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);
        Epic epic2 = taskManager.getEpic(0);
        Epic epic3 = taskManager.getEpic(1);
        assertEquals(epic2.getTitle(), epic.getTitle());
        assertEquals(epic3.getTitle(), epic1.getTitle());
        assertEquals(epic2.getDescription(), epic.getDescription());
        assertEquals(epic3.getDescription(), epic1.getDescription());
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        assertEquals(taskManager.getHistory().get(0).getId(), list.get(0));
        assertEquals(taskManager.getHistory().get(1).getId(), list.get(1));
        assertNull(taskManager.getEpic(10));
    }

    @Test
    public void testingGetSubTask() {
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("c", "d", 0);
        SubTask subTask1 = new SubTask("e", "f", 0);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        SubTask subTask2 = taskManager.getSubTask(1);
        SubTask subTask3 = taskManager.getSubTask(2);
        assertEquals(subTask2.getTitle(), subTask.getTitle());
        assertEquals(subTask3.getTitle(), subTask1.getTitle());
        assertEquals(subTask2.getDescription(), subTask.getDescription());
        assertEquals(subTask3.getDescription(), subTask1.getDescription());
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        assertEquals(taskManager.getHistory().get(0).getId(), list.get(0));
        assertEquals(taskManager.getHistory().get(1).getId(), list.get(1));
        assertNull(taskManager.getSubTask(10));
    }

    @Test
    public void testingAddErrorSubTask() {
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице", TaskStatus.NEW, 0);
        assertThrows(RuntimeException.class, () -> taskManager.createSubtask(subTask));
    }

    @Test
    public void testingGetSubTaskList() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.createEpic(epic);
        assertEquals(taskManager.getSubTaskList(0), new ArrayList<Integer>());
        SubTask subTask = new SubTask("Выйти на улицу", "Будем спортсменами и спустимся по лестнице", TaskStatus.NEW, 0);
        SubTask subTask1 = new SubTask("a", "b", TaskStatus.NEW, 0);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        assertEquals(taskManager.getSubTaskList(0), list);
    }

    @Test
    public void testingGetEpicMap() {
        List<Integer> list = new ArrayList<>();
        assertEquals(0, taskManager.getEpicsHashMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        Epic epic1 = new Epic("a", "b");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);
        assertEquals(2, taskManager.getEpicsHashMap().size());
        assertEquals(epic.getTitle(), taskManager.getEpicsHashMap().get(0).getTitle());
        assertEquals(epic.getDescription(), taskManager.getEpicsHashMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsHashMap().get(0).getStatus());
        assertEquals(list, taskManager.getSubTaskList(0));
        assertEquals(epic1.getTitle(), taskManager.getEpicsHashMap().get(1).getTitle());
        assertEquals(epic1.getDescription(), taskManager.getEpicsHashMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsHashMap().get(1).getStatus());
    }

    @Test
    public void testingGetTaskMap() {
        assertEquals(0, taskManager.getTasksHashMap().size());
        Task task = new Task("Выгулять кота", "Гулять с ним 30 минут");
        Task task1 = new Task("a", "b");
        taskManager.createTask(task);
        taskManager.createTask(task1);
        assertEquals(2, taskManager.getTasksHashMap().size());
        assertEquals(task.getTitle(), taskManager.getTasksHashMap().get(0).getTitle());
        assertEquals(task.getDescription(), taskManager.getTasksHashMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getTasksHashMap().get(0).getStatus());
        assertEquals(task1.getTitle(), taskManager.getTasksHashMap().get(1).getTitle());
        assertEquals(task1.getDescription(), taskManager.getTasksHashMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getTasksHashMap().get(1).getStatus());
    }

    @Test
    public void testingGetSubTasksMap() {
        List<Integer> list = new ArrayList<>();
        assertEquals(0, taskManager.getEpicsHashMap().size());
        assertEquals(0, taskManager.getSubtasksHashMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        SubTask subTask = new SubTask("a", "b", 0);
        SubTask subTask1 = new SubTask("c", "d", 0);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        assertEquals(1, taskManager.getEpicsHashMap().size());
        assertEquals(2, taskManager.getSubtasksHashMap().size());
        assertEquals(epic.getTitle(), taskManager.getEpicsHashMap().get(0).getTitle());
        assertEquals(epic.getDescription(), taskManager.getEpicsHashMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsHashMap().get(0).getStatus());
        list.add(1);
        list.add(2);
        assertEquals(list, taskManager.getSubTaskList(0));
        assertEquals(subTask.getTitle(), taskManager.getSubtasksHashMap().get(1).getTitle());
        assertEquals(subTask.getDescription(), taskManager.getSubtasksHashMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getSubtasksHashMap().get(1).getStatus());
        assertEquals(subTask1.getTitle(), taskManager.getSubtasksHashMap().get(2).getTitle());
        assertEquals(subTask1.getDescription(), taskManager.getSubtasksHashMap().get(2).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getSubtasksHashMap().get(2).getStatus());
    }

    @Test
    public void testingClearTask() {
        assertEquals(0, taskManager.getTasksHashMap().size());
        Task task = new Task("Выгулять кота", "Гулять с ним 30 минут");
        Task task1 = new Task("a", "b");
        taskManager.createTask(task);
        taskManager.createTask(task1);
        assertEquals(2, taskManager.getTasksHashMap().size());
        taskManager.deleteTasks();
        assertEquals(0, taskManager.getTasksHashMap().size());
    }

    @Test
    public void testingClearSubTask() {
        List<Integer> list = new ArrayList<>();
        assertEquals(0, taskManager.getEpicsHashMap().size());
        assertEquals(0, taskManager.getSubtasksHashMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        SubTask subTask = new SubTask("a", "b", 0);
        SubTask subTask1 = new SubTask("c", "d", 0);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        assertEquals(1, taskManager.getEpicsHashMap().size());
        assertEquals(2, taskManager.getSubtasksHashMap().size());
        list.add(1);
        list.add(2);
        assertEquals(list, taskManager.getSubTaskList(0));
        taskManager.deleteSubtasks();
        assertEquals(0, taskManager.getSubtasksHashMap().size());
        assertNull(taskManager.getSubTaskList(0));
    }

    @Test
    public void testingClearEpic() {
        List<Integer> list = new ArrayList<>();
        assertEquals(0, taskManager.getEpicsHashMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        Epic epic1 = new Epic("a", "b");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic1);
        assertEquals(2, taskManager.getEpicsHashMap().size());
        assertEquals(list, taskManager.getSubTaskList(0));
        taskManager.deleteEpics();
        assertEquals(0, taskManager.getEpicsHashMap().size());
    }

    @Test
    public void testingRemoveTask() {
        assertEquals(0, taskManager.getTasksHashMap().size());
        Task task = new Task("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.createTask(task);
        assertEquals(1, taskManager.getTasksHashMap().size());
        taskManager.deleteTask(0);
        assertEquals(0, taskManager.getTasksHashMap().size());
    }

    @Test
    public void testingRemoveEpic() {
        assertEquals(0, taskManager.getEpicsHashMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        taskManager.createEpic(epic);
        assertEquals(1, taskManager.getEpicsHashMap().size());
        taskManager.deleteEpic(0);
        assertEquals(0, taskManager.getEpicsHashMap().size());
    }

    @Test
    public void testingRemoveSubTask() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        SubTask subTask = new SubTask("a", "b", TaskStatus.DONE, 0);
        taskManager.createEpic(epic);
        assertEquals(TaskStatus.NEW, taskManager.getEpic(0).getStatus());
        taskManager.createSubtask(subTask);
        assertEquals(TaskStatus.DONE, taskManager.getEpic(0).getStatus());
        assertEquals(1, taskManager.getSubtasksHashMap().size());
        taskManager.deleteSubtask(1);
        assertEquals(0, taskManager.getSubtasksHashMap().size());
        assertEquals(TaskStatus.NEW, taskManager.getEpic(0).getStatus());
    }

    @Test
    public void testingUpdateTask() {
        assertEquals(0, taskManager.getTasksHashMap().size());
        Task task = new Task("Выгулять кота", "Гулять с ним 30 минут");
        Task task1 = new Task(1, "a", "b", TaskStatus.NEW);
        taskManager.createTask(task);
        taskManager.createTask(task);
        assertEquals(2, taskManager.getTasksHashMap().size());
        assertEquals(task.getTitle(), taskManager.getTasksHashMap().get(0).getTitle());
        assertEquals(task.getDescription(), taskManager.getTasksHashMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getTasksHashMap().get(0).getStatus());
        taskManager.updateTask(task1);
        assertEquals(task1.getTitle(), taskManager.getTasksHashMap().get(1).getTitle());
        assertEquals(task1.getDescription(), taskManager.getTasksHashMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getTasksHashMap().get(1).getStatus());
    }

    @Test
    public void testingUpdateSubTask() {
        assertEquals(0, taskManager.getEpicsHashMap().size());
        assertEquals(0, taskManager.getSubtasksHashMap().size());
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        SubTask subTask = new SubTask("a", "b", 0);
        SubTask subTask1 = new SubTask(2, "c", "d", 0);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask);
        assertEquals(epic.getTitle(), taskManager.getEpicsHashMap().get(0).getTitle());
        assertEquals(epic.getDescription(), taskManager.getEpicsHashMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsHashMap().get(0).getStatus());
        assertEquals(subTask.getTitle(), taskManager.getSubtasksHashMap().get(1).getTitle());
        assertEquals(subTask.getDescription(), taskManager.getSubtasksHashMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getSubtasksHashMap().get(1).getStatus());
        taskManager.updateSubTask(subTask1);
        assertEquals(subTask1.getTitle(), taskManager.getSubtasksHashMap().get(2).getTitle());
        assertEquals(subTask1.getDescription(), taskManager.getSubtasksHashMap().get(2).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getSubtasksHashMap().get(2).getStatus());
    }

    @Test
    public void testingUpdateEpic() {
        Epic epic = new Epic("Выгулять кота", "Гулять с ним 30 минут");
        Epic epic1 = new Epic(1, "a", "b", TaskStatus.NEW);
        taskManager.createEpic(epic);
        taskManager.createEpic(epic);
        assertEquals(2, taskManager.getEpicsHashMap().size());
        assertEquals(epic.getTitle(), taskManager.getEpicsHashMap().get(0).getTitle());
        assertEquals(epic.getDescription(), taskManager.getEpicsHashMap().get(0).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsHashMap().get(0).getStatus());
        taskManager.updateEpic(epic1);
        assertEquals(epic1.getTitle(), taskManager.getEpicsHashMap().get(1).getTitle());
        assertEquals(epic1.getDescription(), taskManager.getEpicsHashMap().get(1).getDescription());
        assertEquals(TaskStatus.NEW, taskManager.getEpicsHashMap().get(1).getStatus());
    }

    @Test
    public void testingHistoryManagerEmptyTaskHistory() {
        Task task = new Task("a", "b");
        Task task1 = new Task("f", "g");
        Epic epic = new Epic("c", "d");
        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createEpic(epic);
        assertEquals(taskManager.getHistory().size(), 0);
        taskManager.getTask(0);
        taskManager.getTask(0);
        List<Integer> list = new ArrayList<>();
        list.add(0);
        assertEquals(list.get(0), taskManager.getHistory().get(0).getId());
        assertEquals(1, taskManager.getHistory().size());
        taskManager.getTask(1);
        taskManager.getEpic(2);
        list.add(1);
        list.add(2);
        assertEquals(3, taskManager.getHistory().size());
        assertEquals(list.get(0), taskManager.getHistory().get(0).getId());
        assertEquals(list.get(1), taskManager.getHistory().get(1).getId());
        assertEquals(list.get(2), taskManager.getHistory().get(2).getId());
        taskManager.deleteTask(1);
        assertEquals(2, taskManager.getHistory().size());
        assertEquals(list.get(0), taskManager.getHistory().get(0).getId());
        assertEquals(list.get(2), taskManager.getHistory().get(1).getId());
        taskManager.createTask(task1);
        taskManager.getTask(3);
        taskManager.deleteTask(0);
        list.add(3);
        assertEquals(2, taskManager.getHistory().size());
        assertEquals(list.get(2), taskManager.getHistory().get(0).getId());
        assertEquals(list.get(3), taskManager.getHistory().get(1).getId());
    }
}