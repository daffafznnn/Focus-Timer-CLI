package main.services;

import main.models.Task;
import main.models.TaskLog;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
  private static TaskService instance;
  private final FileStorageService fileStorageService;
  private final List<Task> tasks;

  private TaskService() {
    fileStorageService = new FileStorageService();
    tasks = fileStorageService.loadTasks();
  }

  public static TaskService getInstance() {
    if (instance == null) {
      instance = new TaskService();
    }
    return instance;
  }

  public List<Task> getTasks() {
    return new ArrayList<>(tasks);
  }

  public void addTask(String taskName) {
    Task newTask = new Task(taskName);
    tasks.add(newTask);
    fileStorageService.saveTasks(tasks);
  }

  public String getTaskNameById(int id) {
    return tasks.stream()
        .filter(task -> task.getId() == id)
        .map(Task::getName)
        .findFirst()
        .orElse(null);
  }

  public Task getTaskById(int id) {
    return tasks.stream()
        .filter(task -> task.getId() == id)
        .findFirst()
        .orElse(null);
  }

  public TaskLog getTaskLogById(int id) {
    Task task = getTaskById(id);
    return task != null ? task.getTaskLog() : null;
  }
}