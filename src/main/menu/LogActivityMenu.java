package main.menu;

import main.services.TaskLogService;
import main.models.TaskLog;

public class LogActivityMenu {
  private TaskLogService taskLogService = TaskLogService.getInstance();

  public void display() {
    System.out.println("=================================");
    System.out.println("         LOG AKTIVITAS");
    System.out.println("=================================");

    if (taskLogService.getAllTaskLogs().isEmpty()) {
      System.err.println("\u001B[31mBelum ada aktivitas yang dicatat.\u001B[0m");
    } else {
      for (TaskLog logEntry : taskLogService.getAllTaskLogs()) {
        System.err.println("\u001B[31m" + logEntry + "\u001B[0m");
      }
    }
  }
}

