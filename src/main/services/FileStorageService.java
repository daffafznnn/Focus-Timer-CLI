package main.services;

import main.models.Task;
import main.models.TaskLog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {

  private static final String TASKS_FILE = "src/main/resources/tasks.txt";
  private static final String TASK_LOGS_FILE = "src/main/resources/task_logs.txt";

  // Menyimpan daftar tugas ke file TXT
  public void saveTasks(List<Task> tasks) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
      for (Task task : tasks) {
        writer.write(task.getId() + "|" + task.getName());
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Membaca daftar tugas dari file TXT
  public List<Task> loadTasks() {
    List<Task> tasks = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length == 2) {
          String name = parts[1];
          // Membuat task baru dengan nama dan ID otomatis
          Task task = new Task(name);
          tasks.add(task);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tasks;
  }

  // Menyimpan daftar log aktivitas ke file TXT
  public void saveTaskLogs(List<TaskLog> taskLogs) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_LOGS_FILE))) {
      for (TaskLog log : taskLogs) {
        writer.write(log.getTaskId() + "|" + log.getTaskName() + "|" +
            log.getFocusTime() + "|" + log.getBreakTime() + "|" + log.getTimestamp());
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Membaca daftar log aktivitas dari file TXT
  public List<TaskLog> loadTaskLogs() {
    List<TaskLog> taskLogs = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(TASK_LOGS_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length == 5) {
          String taskId = parts[0];
          String taskName = parts[1];
          int focusTime = Integer.parseInt(parts[2]);
          int breakTime = Integer.parseInt(parts[3]);
          String timestamp = parts[4];

          TaskLog log = new TaskLog(taskId, taskName);
          log.addFocusTime(focusTime);
          log.addBreakTime(breakTime);
          log.setTimestamp(timestamp);

          taskLogs.add(log);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return taskLogs;
  }
}