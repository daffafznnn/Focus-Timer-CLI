package main.models;

public class Task {
  private static int idCounter = 0;
  private final int id;
  private final String name;
  private TaskLog taskLog;

  public Task(String name) {
    this.id = ++idCounter; // ID akan terisi saat konstruksi pertama kali
    this.name = name;
    this.taskLog = new TaskLog(String.valueOf(id), name); // Inisialisasi TaskLog saat membuat Task
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public TaskLog getTaskLog() {
    return taskLog;
  }

  public void setTaskLog(TaskLog taskLog) {
    this.taskLog = taskLog;
  }
}