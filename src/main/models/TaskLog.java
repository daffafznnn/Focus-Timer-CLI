package main.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskLog {
  private String taskId;
  private String taskName;
  private int focusTime; // Total waktu fokus dalam menit
  private int breakTime; // Total waktu istirahat dalam menit
  private String timestamp; // Tanggal dan waktu log

  public TaskLog(String taskId, String taskName) {
    this.taskId = taskId;
    this.taskName = taskName;
    this.focusTime = 0;
    this.breakTime = 0;
    this.timestamp = getCurrentTimestamp();
  }

  public String getTaskId() {
    return taskId;
  }

  public String getTaskName() {
    return taskName;
  }

  public int getFocusTime() {
    return focusTime;
  }

  public int getBreakTime() {
    return breakTime;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public void addFocusTime(int minutes) {
    if (minutes > 0) {
      this.focusTime += minutes;
    }
  }

  public void addBreakTime(int minutes) {
    if (minutes > 0) {
      this.breakTime += minutes;
    }
  }

  private String getCurrentTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.now().format(formatter);
  }

  @Override
  public String toString() {
    return "Tugas: " + taskName +
        "\nTotal Waktu Fokus: " + focusTime + " menit" +
        "\nTotal Waktu Istirahat: " + breakTime + " menit" +
        "\nTimestamp: " + timestamp + "\n";
  }
}
