package java.manager;

import java.manager.implementation.FileBackedTasksManager;
import java.manager.implementation.InMemoryHistoryManager;
import java.manager.interfaces.HistoryManager;
import java.manager.interfaces.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        File file = new File("testing.csv");
        return FileBackedTasksManager.loadFromFile(file);
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}