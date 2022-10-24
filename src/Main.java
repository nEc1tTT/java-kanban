import Service.Manager;
import Service.Status;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Service.Status;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task = new Task("Gotomagazin", "Magazin lenta", Status.NEW);
        manager.createTask(task);
        Epic epic = new Epic("Gotobordel", "Massaj", Status.NEW);
        manager.createEpic(epic);
        Subtask subtask1 = new Subtask(epic.getId(), "KupitPivo", "KupitPivoVMagazine", Status.NEW);
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask(epic.getId(), "KupitSigareti", "KupitSigaretiVMagazine", Status.NEW);
    }
}
