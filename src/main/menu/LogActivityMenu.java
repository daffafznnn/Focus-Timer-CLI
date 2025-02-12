package main.menu;

import main.services.TaskLogService;
import main.models.TaskLog;

/**
 * Kelas ini berisi menu untuk melihat log aktivitas.
 * Menu ini akan menampilkan daftar log aktivitas yang telah
 * dicatat sebelumnya.
 */
public class LogActivityMenu {
  private final TaskLogService taskLogService = TaskLogService.getInstance();

  /**
   * Menampilkan menu untuk melihat log aktivitas.
   * Jika belum ada aktivitas yang dicatat, maka akan
   * menampilkan pesan kesalahan.
   * Jika sudah ada aktivitas yang dicatat, maka akan
   * menampilkan daftar log aktivitas.
   */
  public void display() {
    System.out.println("=================================");
    System.out.println("         LOG AKTIVITAS");
    System.out.println("=================================");

    if (taskLogService.getAllTaskLogs().isEmpty()) {
      System.err.println("\u001B[31mBelum ada aktivitas yang dicatat.\u001B[0m");
    } else {
      System.out.println("Berikut adalah daftar log aktivitas:");

      for (TaskLog logEntry : taskLogService.getAllTaskLogs()) {
        System.err.println("\u001B[31m" + logEntry + "\u001B[0m");
      }
    }
  }
}
