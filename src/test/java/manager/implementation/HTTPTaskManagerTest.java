package java.manager.implementation;

import java.manager.servers.KVServer;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.manager.servers.KVTaskClient;
import java.model.Epic;
import java.model.SubTask;
import java.model.Task;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;

import static java.manager.servers.HttpTaskServer.gson;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    KVServer kvServer;
    private KVTaskClient kvTaskClient;

    @Override
    HTTPTaskManager createTaskManager() {
        try {
            HTTPTaskManager httpTaskManager = new HTTPTaskManager(URI.create("http://localhost:8078/"));
            return httpTaskManager;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @BeforeEach
    public void startServer() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
    }

    @Test
    public void shouldCreateAllTypesOfTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("Task", "Task description", Instant.ofEpochMilli(12345678), 25);
        HttpRequest taskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        client.send(taskCreateRequest, HttpResponse.BodyHandlers.ofString());

        Epic epic = new Epic("Epic", "Epic description");
        HttpRequest epicCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        int epicId = gson.fromJson(client.send(epicCreateRequest, HttpResponse.BodyHandlers.ofString()).body(), Epic.class).getId();

        SubTask subtask = new SubTask("Subtask", "Subtask description", epicId, Instant.ofEpochMilli(87654321), 25);
        HttpRequest subtaskCreateRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        client.send(subtaskCreateRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest getTasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .GET()
                .build();
        HttpRequest getEpicsRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                .GET()
                .build();
        HttpRequest getSubtasksRequest = HttpRequest
                .newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                .GET()
                .build();

        List<Task> tasksResponse = gson.fromJson(
                client.send(getTasksRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Task>>() {}.getType()
        );

        List<Epic> epicsResponse = gson.fromJson(
                client.send(getEpicsRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<Epic>>() {}.getType()
        );

        List<SubTask> subtasksResponse = gson.fromJson(
                client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString()).body(),
                new TypeToken<List<SubTask>>() {}.getType()
        );

        assertEquals(1, tasksResponse.size());
        assertEquals(1, tasksResponse.get(0).getId());
        assertEquals("Task", tasksResponse.get(0).getTitle());

        assertEquals(1, epicsResponse.size());
        assertEquals(2, epicsResponse.get(0).getId());
        assertEquals("Epic", epicsResponse.get(0).getTitle());

        assertEquals(1, subtasksResponse.size());
        assertEquals(3, subtasksResponse.get(0).getId());
        assertEquals("Subtask", subtasksResponse.get(0).getTitle());
    }


}