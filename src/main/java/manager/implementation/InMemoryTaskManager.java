package manager.implementation;

import manager.Managers;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static model.TaskStatus.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final Map<Integer, Task> tasksHashMap;
    private final Map<Integer, Epic> epicsHashMap;
    private final Map<Integer, SubTask> subtasksHashMap;
    private final HistoryManager history;
    private final TreeSet<Task> listOfTasksSortedByTime;
    private final Set<Task> listOfTaskWithoutStartTime;

    public InMemoryTaskManager() {
        this.tasksHashMap = new HashMap<>();
        this.epicsHashMap = new HashMap<>();
        this.subtasksHashMap = new HashMap<>();
        this.history = Managers.getDefaultHistory();
        this.listOfTasksSortedByTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        this.listOfTaskWithoutStartTime = new HashSet<>() {
        };
    }

    @Override
    public void createTask(Task task) {
        int[] name = new int[10];
        task.setId(id++);
        tasksHashMap.put(task.getId(), task);
        if (task.getStartTime() == null) {
            listOfTaskWithoutStartTime.add(task);
        } else {
            comparisonOfTasksOverTime(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(id++);
        epicsHashMap.put(epic.getId(), epic);
        if (epic.getStartTime() == null) {
            listOfTaskWithoutStartTime.add(epic);
        } else {
            comparisonOfTasksOverTime(epic);
        }
    }

    @Override
    public void createSubtask(SubTask subTask) {
        if (!epicsHashMap.containsKey(subTask.getEpicId())) {
            throw new RuntimeException("Не существует указанного Эпика");
        }
        subTask.setId(id++);
        subtasksHashMap.put(subTask.getId(), subTask);
        getSubTaskList(subTask.getEpicId()).add(subTask.getId());
        updateStatusEpic(subTask.getEpicId());
        startTimeForEpic(subTask.getEpicId());
        getEndTime(subTask.getEpicId());
        sumOfDuration(subTask.getEpicId());
        if (subTask.getStartTime() == null) {
            listOfTaskWithoutStartTime.add(subTask);
        } else {
            comparisonOfTasksOverTime(subTask);
        }
    }

    @Override
    public Task getTask(int id) {    //Получение задач всех видов
        if (tasksHashMap.get(id) != null) {
            history.addHistory(tasksHashMap.get(id));
            return tasksHashMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int id) {
        if (epicsHashMap.get(id) != null) {
            history.addHistory(epicsHashMap.get(id));
            return epicsHashMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTask(int id) {
        if (subtasksHashMap.get(id) != null) {
            history.addHistory(subtasksHashMap.get(id));
            return subtasksHashMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Integer> getSubTaskList(int epicId) {
        if (epicsHashMap.get(epicId) != null) {
            return epicsHashMap.get(epicId).getIdSubTasks();
        } else {
            return null;
        }
    }

    @Override
    public HashMap<Integer, Task> getTasksHashMap() {    //Получение HashMap'ов всех типов задач
        return new HashMap<>(tasksHashMap);
    }

    @Override
    public HashMap<Integer, Epic> getEpicsHashMap() {
        return new HashMap<>(epicsHashMap);
    }

    @Override
    public HashMap<Integer, SubTask> getSubtasksHashMap() {
        return new HashMap<>(subtasksHashMap);
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasksHashMap.values()) {
            listOfTasksSortedByTime.remove(task);
            listOfTaskWithoutStartTime.remove(task);
        }
        clearMemoryTask();
        tasksHashMap.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epicsHashMap.values()) {
            listOfTasksSortedByTime.remove(epic);
            listOfTaskWithoutStartTime.remove(epic);
        }
        clearMemoryEpic();
        epicsHashMap.clear();
        subtasksHashMap.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (SubTask subTask : subtasksHashMap.values()) {
            listOfTasksSortedByTime.remove(subTask);
            listOfTaskWithoutStartTime.remove(subTask);
        }
        clearMemorySubTask();
        subtasksHashMap.clear();
        for (Integer id : epicsHashMap.keySet()) {
            epicsHashMap.get(id).setStatus(NEW);
            epicsHashMap.get(id).setIdSubTasks(null);
            epicsHashMap.get(id).setStartTime(null);
            epicsHashMap.get(id).setDuration(null);
            epicsHashMap.get(id).setEndTime(null);
        }
    }

    private void clearMemoryTask() {
        for (Map.Entry<Integer, Task> entry : tasksHashMap.entrySet()) {
            history.remove(entry.getKey());
        }
    }

    private void clearMemorySubTask() {
        for (Map.Entry<Integer, SubTask> entry : subtasksHashMap.entrySet()) {
            history.remove(entry.getKey());
        }
    }

    private void clearMemoryEpic() {
        for (Map.Entry<Integer, Epic> entry : epicsHashMap.entrySet()) {
            history.remove(entry.getKey());
        }
        clearMemorySubTask();
    }

    @Override
    public void updateTask(Task task) {    //Обновление всех типов задач
        if (tasksHashMap.containsKey(task.getId())) {
            listOfTasksSortedByTime.remove(task);
            tasksHashMap.put(task.getId(), task);
            comparisonOfTasksOverTime(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicsHashMap.containsKey(epic.getId())) {
            epicsHashMap.put(epic.getId(), epic);
            listOfTasksSortedByTime.remove(epic);
            comparisonOfTasksOverTime(epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subtasksHashMap.containsKey(subTask.getId())) {
            subtasksHashMap.put(subTask.getId(), subTask);
            updateStatusEpic(subTask.getEpicId());
            listOfTasksSortedByTime.remove(subTask);
            comparisonOfTasksOverTime(subTask);
            startTimeForEpic(subTask.getEpicId());
            sumOfDuration(subTask.getEpicId());
            getEndTime(subTask.getEpicId());
        }
    }

    @Override
    public void deleteTask(int id) {    //Удаление одной конкретной задачи
        if (tasksHashMap.get(id) != null) {
            listOfTasksSortedByTime.remove(tasksHashMap.get(id));//может выкидывать исключения, возможно потребуется проверка
            listOfTaskWithoutStartTime.remove(tasksHashMap.get(id));
            tasksHashMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {    //При удалении Epic'а, нужно удалить и все принадлежащие ему подзадачи
        if (getSubTaskList(id) != null) {
            listOfTasksSortedByTime.remove(epicsHashMap.get(id));//может выкидывать исключения, возможно потребуется проверка
            listOfTaskWithoutStartTime.remove(epicsHashMap.get(id));
            for (int i = 0; i < getSubTaskList(id).size(); i++) {
                listOfTasksSortedByTime.remove(subtasksHashMap.get(id));//может выкидывать исключения, возможно потребуется проверка
                listOfTaskWithoutStartTime.remove(subtasksHashMap.get(id));
                subtasksHashMap.remove(getSubTaskList(id).get(i));
                history.remove(getSubTaskList(id).get(i));
            }
            epicsHashMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        if (getSubTaskList(subtasksHashMap.get(id).getEpicId()) != null) {
            listOfTasksSortedByTime.remove(subtasksHashMap.get(id));
            listOfTaskWithoutStartTime.remove(subtasksHashMap.get(id));
            getSubTaskList(subtasksHashMap.get(id).getEpicId()).remove((Integer) id);
            updateStatusEpic(subtasksHashMap.get(id).getEpicId());
            subtasksHashMap.remove(id);
            history.remove(id);
        }
    }

    private void updateStatusEpic(int id) {
        int statusInProgress = 0;
        int statusDone = 0;
        int statusNew = 0;
        if (getSubTaskList(id) != null) {
            for (int i = 0; i < getSubTaskList(id).size(); i++) {
                if (subtasksHashMap.get(getSubTaskList(id).get(i)) != null) {
                    if (subtasksHashMap.get((getSubTaskList(id).get(i))).getStatus() == IN_PROGRESS) {
                        statusInProgress++;
                    } else if (subtasksHashMap.get(getSubTaskList(id).get(i)).getStatus() == DONE) {
                        statusDone++;
                    } else {
                        statusNew++;
                    }
                }
            }
            if (statusInProgress < 1 && statusDone < 1) {
                epicsHashMap.get(id).setStatus(NEW);
            } else if (statusInProgress > 0 || (statusDone > 0 && statusNew > 0)) {
                epicsHashMap.get(id).setStatus(IN_PROGRESS);
            } else {
                epicsHashMap.get(id).setStatus(DONE);
            }
        }
    }

    private void startTimeForEpic(int epicId) {
        getEpicForMetod(epicId).getIdSubTasks()
                .stream()
                .map(this::getSubtaskForMetod)
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .ifPresent(getEpicForMetod(epicId)::setStartTime);
    }

    private void sumOfDuration(int epicId) {
        List<Integer> subTasks = getSubTaskList(epicId);
        Duration duration = Duration.ZERO;
        for (Integer subTask : subTasks) {
            Duration durationSubTask;
            if (getSubTask(subTask).getDuration() == null) {
                continue;
            }
            durationSubTask = getSubTask(subTask).getDuration();

            duration = duration.plus(durationSubTask);
        }
        epicsHashMap.get(epicId).setDuration(duration);
    }

    private void getEndTime(int epicId) {
        getEpicForMetod(epicId).getIdSubTasks()
                .stream()
                .map(this::getSubtaskForMetod)
                .map(x -> x.getEndTime().orElse(x.getStartTime()))
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(getEpicForMetod(epicId)::setEndTime);
    }

    private void comparisonOfTasksOverTime(Task myTask) {
        if (myTask.getStartTime() == null) {
            return;
        }
        listOfTasksSortedByTime.add(myTask);
        LocalDateTime prev = LocalDateTime.MIN;
        for (Task prioritizedTask : listOfTasksSortedByTime) {
            if (prev.isAfter(prioritizedTask.getStartTime())) {
                listOfTasksSortedByTime.remove(myTask);
                throw new RuntimeException("Произошло наложение задач, введенная задача будет удалена");
            }
            prev = prioritizedTask.getEndTime().orElse(prioritizedTask.getStartTime());
        }
    }

    private Epic getEpicForMetod(int id) {
        return epicsHashMap.get(id);
    }

    private SubTask getSubtaskForMetod(int id) {
        return subtasksHashMap.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return history;
    }
}