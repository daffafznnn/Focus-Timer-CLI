package main.menu;

import main.services.TaskService;

public class LogActivityMenu {
  private TaskService taskService = new TaskService();

  public void display() {
    System.out.println("=================================");
    System.out.println("         LOG AKTIVITAS");
    System.out.println("=================================");

    if (taskService.getLog().isEmpty()) {
      System.out.println("Belum ada aktivitas yang dicatat.");
    } else {
      for (String logEntry : taskService.getLog()) {
        System.out.println(logEntry);
      }
    }
  }
}