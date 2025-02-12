package main.services;

import main.models.Task;
import main.models.TaskLog;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
  private static TaskService instance;
  private final FileStorageService fileStorageService;
  private final List<Task> tasks;

  // Konstruktor privat untuk mencegah instansiasi langsung dari luar kelas
  private TaskService() {
    fileStorageService = new FileStorageService();
    tasks = fileStorageService.loadTasks();
  }

  // Mengembalikan instance tunggal dari TaskService
  public static TaskService getInstance() {
    if (instance == null) {
      instance = new TaskService();
    }
    return instance;
  }

  // Mengembalikan daftar tugas
  public List<Task> getTasks() {
    return new ArrayList<>(tasks);
  }

  // Menambahkan tugas baru ke daftar tugas dan menyimpannya ke file
  public void addTask(String taskName) {
    Task newTask = new Task(taskName);
    tasks.add(newTask);
    fileStorageService.saveTasks(tasks);
  }

  // Mengembalikan nama tugas berdasarkan ID tugas
  public String getTaskNameById(int id) {
    return tasks.stream()
        .filter(task -> task.getId() == id)
        .map(Task::getName)
        .findFirst()
        .orElse(null);
  }

  // Mengembalikan tugas berdasarkan ID tugas
  public Task getTaskById(int id) {
    return tasks.stream()
        .filter(task -> task.getId() == id)
        .findFirst()
        .orElse(null);
  }

  // Mengembalikan log tugas berdasarkan ID tugas
  public TaskLog getTaskLogById(int id) {
    Task task = getTaskById(id);
    return task != null ? task.getTaskLog() : null;
  }
}