package manager.implementation;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file = new File("testing.csv");

    @Override
    @BeforeEach
    public void setUp() {
        taskManager = new FileBackedTasksManager(file);
        epicTest = new Epic("test", "test", TaskStatus.NEW);
        subTaskTest = new SubTask("test", "test", TaskStatus.NEW, 1, LocalDateTime.of(2024, 12, 21, 10, 25), Duration.ofMinutes(10));
        taskTest = new Task("test", "test", TaskStatus.NEW, LocalDateTime.of(2024, 12, 21, 10, 25), Duration.ofMinutes(10));


    }
}