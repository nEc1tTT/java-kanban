package service;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    public static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task) {
            this.task = task;
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        Node temp = new Node(task);
        if (first != null) {
            for (int id : nodeMap.keySet()) {
                if (task.getId() == id) {
                    remove(id);
                }
            }
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        if (first == null) {
            System.out.println("Список истории просмотра задач пуст.");// подсказка
        } else {
            for (Node node : nodeMap.values()) {
                tasks.add(node.task);
            }
        }
        return tasks;
    }

    private void linkLast(Task task) {
        Node temp = new Node(task);
        if (first == null) {
            first = temp;
        } else {
            last.next = temp;
        }
        temp.prev = last;
        last = temp;
        nodeMap.put(task.getId(), temp);

    }

    private void removeNode(int id) {
        Node node = nodeMap.remove(id);
        if (first == null || node == null) {
            return;
        }

        Node next = node.next;
        Node prev = node.prev;

        if (prev == null) { // удаляется first
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) { // удаляется last
            last = prev;
        } else {
            next.prev = prev;
        }
    }
}
