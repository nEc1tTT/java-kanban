package manager.implementation;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        epicTest = new Epic("test", "test", TaskStatus.NEW);
        subTaskTest = new SubTask("test", "test", TaskStatus.NEW, 1, LocalDateTime.of(2024, 12, 21, 10, 25), Duration.ofMinutes(10));
        taskTest = new Task("test", "test", TaskStatus.NEW, LocalDateTime.of(2024, 12, 21, 10, 25), Duration.ofMinutes(10));


    }
}