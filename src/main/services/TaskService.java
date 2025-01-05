package main.services;

import main.models.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskService {
  private static TaskService instance; // Singleton instance
  private List<Task> tasks = new ArrayList<>();

  private TaskService() {
  } // Private constructor

  public static TaskService getInstance() {
    if (instance == null) {
      instance = new TaskService();
    }
    return instance;
  }

  public List<Task> getData() {
    return tasks;
  }

  public void addTask(String name) {
        String id = UUID.randomUUID().toString(); // Membuat ID unik untuk setiap tugas
        Task task = new Task(id, name);
        tasks.add(task);
    }

  public List<String> getTasks() {
    List<String> taskNames = new ArrayList<>();
    for (Task task : tasks) {
      taskNames.add(task.getName());
    }
    return taskNames;
  }

  public String getTaskNameById(String taskId) {
    for (Task task : tasks) {
      if (task.getId().equals(taskId)) {
        return task.getName();
      }
    }
    return null;
  }

  // Mengembalikan daftar ID tugas
  public List<String> getTaskIds() {
    List<String> taskIds = new ArrayList<>();
    for (Task task : tasks) {
      taskIds.add(task.getId());
    }
    return taskIds;
  }

  // Mencari tugas berdasarkan ID
  public Task findTaskById(String id) {
    for (Task task : tasks) {
      if (task.getId().equals(id)) {
        return task;
      }
    }
    return null;
  }
}

