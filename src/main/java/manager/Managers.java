package java.manager;

import java.io.IOException;
import java.manager.implementation.FileBackedTasksManager;
import java.manager.implementation.HTTPTaskManager;
import java.manager.implementation.InMemoryHistoryManager;
import java.manager.interfaces.HistoryManager;
import java.manager.interfaces.TaskManager;

import java.io.File;
import java.net.URI;

public class Managers {

    public static TaskManager getDefault() {
        try {
            return new HTTPTaskManager(URI.create("http://localhost:8078/"));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}