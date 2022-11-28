package service;

public class Managers {
    public static TaskManager getDefault() { // Дописать
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
