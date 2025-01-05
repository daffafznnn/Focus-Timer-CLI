package main.models;

public class TaskLog {
  private String taskId;
  private String taskName;
  private int focusTime; // Total waktu fokus dalam menit
  private int breakTime; // Total waktu istirahat dalam menit

  public TaskLog(String taskId, String taskName) {
    this.taskId = taskId;
    this.taskName = taskName;
    this.focusTime = 0;
    this.breakTime = 0;
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

  public void addFocusTime(int minutes) {
    this.focusTime += minutes;
  }

  public void addBreakTime(int minutes) {
    this.breakTime += minutes;
  }

  @Override
  public String toString() {
    return "Tugas: " + taskName +
        "\nTotal Waktu Fokus: " + focusTime + " menit" +
        "\nTotal Waktu Istirahat: " + breakTime + " menit\n";
  }
}
