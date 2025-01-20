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
    for (Task task : taskService.getTasks()) {
      System.out.println(index + ". " + task.getId() + ": " + task.getName());
      index++;
    }

    System.out.print("Pilih nomor tugas untuk memulai timer: ");
    int taskNumber = scanner.nextInt();
    scanner.nextLine(); // Clear buffer

    if (taskNumber > 0 && taskNumber <= taskService.getTasks().size()) {
      Task selectedTask = taskService.getTasks().get(taskNumber - 1);
      timerService.startTimer(String.valueOf(selectedTask.getId()));
    } else {
      System.out.println("Nomor tugas tidak valid.");
    }
  }
}
