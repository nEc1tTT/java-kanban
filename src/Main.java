import service.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import service.Status;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryManager = Managers.getDefault();
        Task task = new Task("Gotomagazin", "Magazin lenta", Status.NEW);
        int id1 = inMemoryManager.createTask(task);
        int id2 = inMemoryManager.createTask(task);
        int id3 = inMemoryManager.createTask(task);
        int id4 = inMemoryManager.createTask(task);
        int id5 = inMemoryManager.createTask(task);
        int id6 = inMemoryManager.createTask(task);
        int id7 = inMemoryManager.createTask(task);
        int id8 = inMemoryManager.createTask(task);
        int id9 = inMemoryManager.createTask(task);
        int id10 = inMemoryManager.createTask(task);
        inMemoryManager.getTaskById(id1);
        inMemoryManager.getTaskById(id2);
        inMemoryManager.getTaskById(id3);
        inMemoryManager.getTaskById(id4);
        inMemoryManager.getTaskById(id5);
        inMemoryManager.getTaskById(id6);
        inMemoryManager.getTaskById(id7);
        inMemoryManager.getTaskById(id8);
        inMemoryManager.getTaskById(id9);
        inMemoryManager.getTaskById(id10);
        System.out.println(inMemoryManager.getHistory());
        Epic epic = new Epic("Gotobordel", "Massaj", Status.NEW);
        int id11 = inMemoryManager.createEpic(epic);
        inMemoryManager.getEpicById(id11);
        System.out.println(inMemoryManager.getHistory());


    }
}
