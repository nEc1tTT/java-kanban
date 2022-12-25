package java.manager.implementation;

import java.manager.exception.ManagerSaveException;
import java.manager.interfaces.HistoryManager;

import java.io.*;
import java.manager.interfaces.HistoryManager;
import java.model.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.model.TaskType.SUBTASK;
import static java.model.TaskType.TASK;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path path;

    private FileBackedTasksManager(File file) {
        this.path = file.toPath();
    }

    public void save() {
        try (Writer writer = new FileWriter(path.toString(), StandardCharsets.UTF_8, true)) {
            for (Map.Entry<Integer, Task> entry : getTasksHashMap().entrySet()) {
                writer.write(getTaskString(entry.getValue()));
                writer.write("\n");
            }
            for (Map.Entry<Integer, Epic> entry : getEpicsHashMap().entrySet()) {
                writer.write(getTaskString(entry.getValue()));
                writer.write("\n");
            }
            for (Map.Entry<Integer, SubTask> entry : getSubtasksHashMap().entrySet()) {
                writer.write(getTaskString(entry.getValue()));
                writer.write("\n");
            }
            writer.write("\n");
            writer.write(toString(getHistoryManager()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if (Files.exists(file.toPath())) {
            try (Reader reader = new FileReader(file.toPath().toString(), StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                bufferedReader.readLine();
                while (bufferedReader.ready()) {
                    String str = bufferedReader.readLine();
                    if (str != null && str.isEmpty()) {
                        String history = bufferedReader.readLine();
                        List<Integer> tasks = fromString(history);
                        Map<Integer, Task> taskMap = fileBackedTasksManager.getTasksHashMap();
                        Map<Integer, SubTask> subTaskMap = fileBackedTasksManager.getSubtasksHashMap();
                        if (tasks != null) {
                            for (Integer taskId : tasks) {
                                if (taskMap.containsKey(taskId)) {
                                    fileBackedTasksManager.getTaskSuper(taskId);
                                } else if (subTaskMap.containsKey(taskId)) {
                                    fileBackedTasksManager.getSubTaskSuper(taskId);
                                } else {
                                    fileBackedTasksManager.getEpicSuper(taskId);
                                }
                            }
                        }
                    } else {
                        fileBackedTasksManager.addTaskType(str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileBackedTasksManager;
    }

    private void getTaskSuper(int id) {
        super.getTask(id);
    }

    private void getEpicSuper(int id) {
        super.getEpic(id);
    }

    private void getSubTaskSuper(int id) {
        super.getSubTask(id);
    }

    public static String toString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        String task = tasks.stream()
                .mapToInt(Task::getId)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        return task;
    }

    public static List<Integer> fromString(String value) {
        List<Integer> tasks = new ArrayList<>();
        if (value == null) {
            return null;
        }
        String[] historyList = value.split(",");

        for (String task : historyList) {
            tasks.add(Integer.parseInt(task));
        }
        return tasks;
    }

    public void addTaskType(String str) {
        String[] taskArray = str.split(",");
        String type = taskArray[1];
        TaskType taskType = TaskType.valueOf(type);
        if (taskType == TASK) {
            super.createTask(getTaskFromString(taskArray[2], taskArray[3], taskArray[4], taskArray[5], taskArray[6]));
        } else if (taskType.equals(SUBTASK)) {
            super.createSubtask(getSubTaskFromString(taskArray[2], taskArray[3], taskArray[4], taskArray[5], taskArray[6], taskArray[7]));
        } else {
            super.createEpic(getEpicFromString(taskArray[2], taskArray[3], taskArray[4], taskArray[5], taskArray[6]));
        }
    }

    public Task getTaskFromString(String name, String taskStatus,
                                  String description, String duration, String startTime) {
        if (startTime.equals(" ")) {
            return new Task(name,
                    description,
                    TaskStatus.valueOf(taskStatus),
                    null,
                    null);
        }
        return new Task(name,
                description,
                TaskStatus.valueOf(taskStatus),
                LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME),
                Duration.ofMinutes(Integer.parseInt(duration)));
    }

    public SubTask getSubTaskFromString(String name, String taskStatus,
                                        String description, String epicId, String duration, String startTime) {
        if (startTime.equals(" ")) {
            return new SubTask(name,
                    description,
                    TaskStatus.valueOf(taskStatus),
                    Integer.parseInt(epicId),
                    null,
                    null);
        }
        return new SubTask(name,
                description,
                TaskStatus.valueOf(taskStatus),
                Integer.parseInt(epicId),
                LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME),
                Duration.ofMinutes(Integer.parseInt(duration)));
    }

    public Epic getEpicFromString(String name, String taskStatus,
                                  String description, String duration, String startTime) {
        if (startTime.equals(" ")) {
            return new Epic(name,
                    description,
                    TaskStatus.valueOf(taskStatus),
                    null,
                    null);
        }
        return new Epic(name,
                description,
                TaskStatus.valueOf(taskStatus),
                LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME),
                Duration.ofMinutes(Integer.parseInt(duration)));
    }

    public String getTaskString(Task task) {
        if (task.getStartTime() == null) {
            return String.join(",", new String[]{
                    Integer.toString(task.getId()),
                    task.getTaskType().name(),
                    task.getTitle(),
                    task.getStatus().name(),
                    task.getDescription(),
                    " ",
                    " "
            });
        }
        return String.join(",", new String[]{
                Integer.toString(task.getId()),
                task.getTaskType().name(),
                task.getTitle(),
                task.getStatus().name(),
                task.getDescription(),
                String.valueOf(task.getDuration()),
                task.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)
        });
    }

    public String getTaskString(Epic epic) {
        if (epic.getStartTime() == null) {
            return String.join(",", new String[]{
                    Integer.toString(epic.getId()),
                    epic.getTaskType().name(),
                    epic.getTitle(),
                    epic.getStatus().name(),
                    epic.getDescription(),
                    " ",
                    " "
            });
        }
        return String.join(",", new String[]{
                Integer.toString(epic.getId()),
                epic.getTaskType().name(),
                epic.getTitle(),
                epic.getStatus().name(),
                epic.getDescription(),
                String.valueOf(epic.getDuration()),
                epic.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)
        });
    }

    public String getTaskString(SubTask subTask) {
        if (subTask.getStartTime() == null) {
            return String.join(",", new String[]{
                    Integer.toString(subTask.getId()),
                    subTask.getTaskType().name(),
                    subTask.getTitle(),
                    subTask.getStatus().name(),
                    subTask.getDescription(),
                    Integer.toString(subTask.getEpicId()),
                    " ",
                    " "
            });
        }
        return String.join(",", new String[]{
                Integer.toString(subTask.getId()),
                subTask.getTaskType().name(),
                subTask.getTitle(),
                subTask.getStatus().name(),
                subTask.getDescription(),
                Integer.toString(subTask.getEpicId()),
                String.valueOf(subTask.getDuration()),
                subTask.getStartTime().format(DateTimeFormatter.ISO_DATE_TIME)
        });
    }

    @Override
    public void createSubtask(SubTask subTask) {
        super.createSubtask(subTask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }
}

