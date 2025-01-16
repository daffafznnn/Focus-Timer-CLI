package main.menu;

import main.models.Task;
import main.services.TaskService;
import main.services.TimerService;

import java.util.Scanner;

public class StartWorkSessionMenu {
  private final Scanner scanner = new Scanner(System.in);
  private final TaskService taskService = TaskService.getInstance();
  private final TimerService timerService = TimerService.getInstance();

  public void display() {
    System.out.println("=================================");
    System.out.println("         MULAI SESI KERJA");
    System.out.println("=================================");

    if (taskService.getTasks().isEmpty()) {
      System.out.println("Tidak ada tugas yang tersedia. Tambahkan tugas terlebih dahulu di menu utama.");
      return;
    }

    System.out.println("Daftar Tugas:");
    int index = 1;
    // Menampilkan tugas beserta ID
    for (Task task : taskService.getTasks()) {
      System.out.println(index + ". " + task.getId() + ": " + task.getName());
      index++;
    }

    System.out.print("Pilih nomor tugas untuk memulai timer: ");
    int taskNumber = scanner.nextInt();
    scanner.nextLine(); // Clear the buffer

    if (taskNumber > 0 && taskNumber <= taskService.getTasks().size()) {
      Task selectedTask = taskService.getTasks().get(taskNumber - 1);
      String selectedTaskId = String.valueOf(selectedTask.getId());
      timerService.startTimer(selectedTaskId);

      handleTimerControls(selectedTaskId);
    } else {
      System.out.println("Nomor tugas tidak valid.");
    }
  }

  private void handleTimerControls(String taskId) {
    boolean isRunning = true;
    while (isRunning) {
      System.out.println("=================================");
      System.out.println("1. Pause Timer");
      System.out.println("2. Resume Timer");
      System.out.println("3. Stop Timer");
      System.out.println("0. Kembali ke Menu Utama");
      System.out.println("=================================");
      System.out.print("Pilihan Anda: ");
      int choice = scanner.nextInt();
      scanner.nextLine(); // Clear the buffer

      switch (choice) {
        case 1:
          timerService.pauseTimer();
          break;
        case 2:
          timerService.resumeTimer();
          break;
        case 3:
          timerService.stopTimer();
          isRunning = false;
          break;
        case 0:
          timerService.stopTimer();
          isRunning = false;
          break;
        default:
          System.out.println("Pilihan tidak valid.");
      }
    }
  }
}