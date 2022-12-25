package java.manager.implementation;

import java.model.Epic;
import java.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file = new File("testing.csv");

    @Override
    FileBackedTasksManager createTaskManager() {
        return FileBackedTasksManager.loadFromFile(file);
    }

    @AfterEach
    public void deleteFile() {
        boolean delete = file.delete();
    }

    @Test
    public void testingFile() {
        assertEquals(0, taskManager.getHistory().size());
        Task task = new Task("a", "b");
        taskManager.createTask(task);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(fileBackedTasksManager.getTask(0).getTitle(), task.getTitle());
        assertEquals(fileBackedTasksManager.getTask(0).getDescription(), task.getDescription());
        assertEquals(fileBackedTasksManager.getTask(0).getId(), task.getId());
    }

    @Test
    public void testingFileEpicWithoutSubtasks() {
        Epic epic = new Epic("a", "b");
        taskManager.createEpic(epic);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, fileBackedTasksManager.getHistory().size());
        assertEquals(fileBackedTasksManager.getEpic(0).getTitle(), epic.getTitle());
        assertEquals(fileBackedTasksManager.getEpic(0).getDescription(), epic.getDescription());
        assertEquals(fileBackedTasksManager.getEpic(0).getIdSubTasks().size(), 0);
    }
}