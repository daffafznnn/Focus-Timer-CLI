package main.menu;

import main.services.TaskService;
import main.services.TimerService;
import java.util.Scanner;

public class StartWorkSessionMenu {
  private Scanner scanner = new Scanner(System.in);
  private TaskService taskService = new TaskService();
  private TimerService timerService = new TimerService();

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
    for (String task : taskService.getTasks()) {
      System.out.println(index + ". " + task);
      index++;
    }

    System.out.print("Pilih nomor tugas untuk memulai timer: ");
    int taskNumber = scanner.nextInt();
    scanner.nextLine(); // Clear the buffer

    if (taskNumber > 0 && taskNumber <= taskService.getTasks().size()) {
      String selectedTask = taskService.getTasks().get(taskNumber - 1);
      timerService.startTimer(selectedTask);
    } else {
      System.out.println("Nomor tugas tidak valid.");
    }
  }
}
