package service;

import exceptions.ManagerCreateException;
import exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Никита\\dev\\java-kanban\\resources\\database.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Epic epic = new Epic("Test0", "Test1", Status.NEW);
        fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.getEpicById(epic.getId());
        Subtask subtask0 = new Subtask(epic.getId(), "SubTest0", "SubTest0", Status.NEW);
        fileBackedTasksManager.createSubtask(subtask0);
        fileBackedTasksManager.getSubTaskById(subtask0.getId());
        Task task = new Task("Test0", "Test1", Status.NEW);
        fileBackedTasksManager.createTask(task);
        fileBackedTasksManager.getTaskById(task.getId());
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if (!file.exists()) {
            return null;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String input = bufferedReader.readLine();
                if (!input.isBlank()) {
                    Task task = fromString(input);
                    if (task instanceof Epic) {
                        fileBackedTasksManager.createEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        fileBackedTasksManager.createSubtask((Subtask) task);
                    } else if (task != null) {
                        fileBackedTasksManager.createTask(task);
                    } else {
                        List<Integer> historyResult = historyFromString(input);
                        for (Integer ints : historyResult) {
                            if (fileBackedTasksManager.taskHashMap.containsKey(ints)) {
                                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getTaskById(ints));
                            } else if (fileBackedTasksManager.epicHashMap.containsKey(ints)) {
                                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getEpicById(ints));
                            } else if (fileBackedTasksManager.subtaskHashMap.containsKey(ints)) {
                                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getSubTaskById(ints));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Что-то пошло не так.");
        }
        return fileBackedTasksManager;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : taskHashMap.values()) {
                writer.write(task.toString() + "\n");
            }
            for (Epic epic : epicHashMap.values()) {
                writer.write(epic.toString() + "\n");
            }
            for (Subtask subtask : subtaskHashMap.values()) {
                writer.write(subtask.toString() + "\n");
            }
            writer.write("\n");
            writer.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла.");
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : manager.getHistory()) {
            stringBuilder.append(task.getId()).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    private static List<Integer> historyFromString(String value) {
        try {
            String[] result = value.trim().split(",");       // Нужна ли проверка на пустоту?
            List<Integer> backResult = new ArrayList<>();
            for (String string : result) {
                backResult.add(Integer.parseInt(string));          //
            }
            return backResult;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ошибка при парсинге.");
        }
    }

    //return String.format("%s,%s,%s,%s,%s,%s", id, TaskType.TASK.name(), name, status, description, "");
    public static Task fromString(String value) {
        String[] result = value.trim().split(",");
        if (result.length > 4 && result[1].equals(TaskType.TASK.name())) {
            Task task = new Task(result[2], result[4], Status.valueOf(result[3]));
            task.setId(Integer.parseInt(result[0]));
            return task;
        } else if (result.length > 4 && result[1].equals(TaskType.EPIC.name())) {
            Epic epic = new Epic(result[2], result[4], Status.valueOf(result[3]));
            epic.setId(Integer.parseInt(result[0]));
            return epic;
        } else if (result.length > 4 && result[1].equals(TaskType.SUBTASK.name())) {
            Subtask subtask = new Subtask(Integer.parseInt(result[5]), result[2], result[4], Status.valueOf(result[3]));
            subtask.setId(Integer.parseInt(result[0]));
            return subtask;
        }
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }


    @Override
    public int createTask(Task task) {
        int x = super.createTask(task);
        save();
        return x;
    }

    @Override
    public int createEpic(Epic epic) {
        int x = super.createEpic(epic);
        save();
        return x;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int x = super.createSubtask(subtask);
        save();
        return x;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = super.getSubTaskById(id);
        save();
        return subtask;
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
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
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


}
