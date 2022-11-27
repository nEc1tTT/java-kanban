import service.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import service.Status;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryManager = Managers.getDefault();
        Epic epic = new Epic("Test0", "Test1", Status.NEW);
        Epic epic2 = new Epic("Test1", "test0", Status.NEW);
        Epic epic3 = new Epic("Test1", "test0", Status.NEW);
        int id1 = inMemoryManager.createEpic(epic);
        inMemoryManager.getEpicById(id1);
        System.out.println(inMemoryManager.getHistory());
        int id2 = inMemoryManager.createEpic(epic2);
        Subtask subtask0 = new Subtask(id1, "SubTest0", "SubTest0", Status.NEW);
        Subtask subtask1 = new Subtask(id1, "SubTest1", "SubTest1", Status.NEW);
        Subtask subtask2 = new Subtask(id1, "SubTest2", "SubTest2", Status.NEW);
        inMemoryManager.createSubtask(subtask0);
        inMemoryManager.createSubtask(subtask1);
        inMemoryManager.createSubtask(subtask2);
        int id3 = inMemoryManager.createEpic(epic3);
        inMemoryManager.getEpicById(id1);
        inMemoryManager.getEpicById(id2);
        inMemoryManager.getEpicById(id3);
        System.out.println(inMemoryManager.getHistory());
        inMemoryManager.deleteEpic(id2);
        System.out.println(inMemoryManager.getHistory());
        inMemoryManager.deleteEpic(id1);
        System.out.println(inMemoryManager.getHistory());
        inMemoryManager.deleteEpic(id3);
        System.out.println("Все удалено");
        System.out.println(inMemoryManager.getHistory());
        inMemoryManager.printHashmapsObject();




    }
}
