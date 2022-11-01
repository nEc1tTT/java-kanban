package service;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();
    static final int MAX_HISTORY_OBJECT = 10;

    @Override
    public void add(Task task) {
        if (history.size() >= MAX_HISTORY_OBJECT) {
            history.remove(0);
        }
        history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
