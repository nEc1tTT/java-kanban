import service.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import service.Status;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryManager = Managers.getDefault();

        inMemoryManager.createTask(new Task("Задача №1", "Чай", Status.DONE));
        inMemoryManager.createTask(new Task("Задача №2", "Кофе", Status.NEW));
        inMemoryManager.createTask(new Task("Задача №3", "Сода", Status.NEW));
        inMemoryManager.createTask(new Task("Задача №4", "Хлебушек", Status.DONE));

        Epic epic = new Epic("Test0", "Test1", Status.NEW);
        Epic epic2 = new Epic("Test1", "test0", Status.NEW);

        int id1 = inMemoryManager.createEpic(epic);
        int id2 = inMemoryManager.createEpic(epic2);

        Subtask subtask0 = new Subtask(id1, "SubTest0", "SubTest0", Status.NEW);
        Subtask subtask1 = new Subtask(id1, "SubTest1", "SubTest1", Status.NEW);
        Subtask subtask2 = new Subtask(id1, "SubTest2", "SubTest2", Status.NEW);
        inMemoryManager.createSubtask(subtask0);
        inMemoryManager.createSubtask(subtask1);
        inMemoryManager.createSubtask(subtask2);

        System.out.println("\n Пересчет всех Эпиков : \n" + inMemoryManager.getEpics());
        System.out.println("\n Пересчет всех Задач : \n" +  inMemoryManager.getTasks());
        System.out.println("\n Пересчет всех Подзадач : \n" + inMemoryManager.getSubtasks());


        System.out.println("\n Итого Эпиков : \n" + inMemoryManager.getEpics());
        System.out.println("\n Итого Задач : \n" +  inMemoryManager.getTasks());
        System.out.println("\n Итого Подзадач : \n" + inMemoryManager.getSubtasks());

        System.out.println("\n Показать историю : \n" + inMemoryManager.getHistory());

        TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("data/data.csv"));




    }
}
