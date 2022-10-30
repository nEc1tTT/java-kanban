import Service.*;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Service.Status;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryManager = Managers.getDefault();
        Task task = new Task("Gotomagazin", "Magazin lenta", Status.NEW);
        int id1 = inMemoryManager.createTask(task);
        Epic epic = new Epic("Gotobordel", "Massaj", Status.NEW);
        int id2 = inMemoryManager.createEpic(epic);
        Subtask subtask1 = new Subtask(epic.getId(), "KupitPivo", "KupitPivoVMagazine", Status.NEW);
        int id3 = inMemoryManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask(epic.getId(), "KupitSigareti", "KupitSigaretiVMagazine", Status.NEW);
        int id4 = inMemoryManager.createSubtask(subtask2);
        inMemoryManager.getTaskById(id1);
        inMemoryManager.getEpicById(id2);
        inMemoryManager.getSubTaskById(id3);
        inMemoryManager.getSubTaskById(id4);
        System.out.println(inMemoryManager.getHistory());


    }
}
