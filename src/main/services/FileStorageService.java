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
    ensureFileExists(TASKS_FILE);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE, false))) {
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
    ensureFileExists(TASKS_FILE);
    List<Task> tasks = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length == 2) {
          String name = parts[1];
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
    ensureFileExists(TASK_LOGS_FILE);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_LOGS_FILE, false))) {
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
    ensureFileExists(TASK_LOGS_FILE);
    List<TaskLog> taskLogs = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(TASK_LOGS_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length == 5) {
          try {
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
          } catch (NumberFormatException e) {
            System.err.println("Gagal memproses data log: " + line);
          }
        } else {
          System.err.println("Data log tidak valid: " + line);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return taskLogs;
  }

  private void ensureFileExists(String filePath) {
    File file = new File(filePath);
    if (!file.exists()) {
      try {
        file.getParentFile().mkdirs();
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
