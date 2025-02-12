package main.menu;

import main.models.Task;
import main.services.TaskService;
import main.services.TimerService;

import java.util.Scanner;

/**
 * Menu untuk memulai sesi kerja. Menu ini akan menampilkan daftar tugas yang tersedia
 * dan meminta pengguna untuk memilih nomor tugas yang ingin dijalankan. Setelah itu,
 * menu ini akan memulai timer dengan menggunakan id tugas yang dipilih.
 */
public class StartWorkSessionMenu {
  private final Scanner scanner = new Scanner(System.in);
  private final TaskService taskService = TaskService.getInstance();
  private final TimerService timerService = TimerService.getInstance();

  /**
   * Menampilkan menu untuk memulai sesi kerja
   */
  public void display() {
    System.out.println("=================================");
    System.out.println("         MULAI SESI KERJA");
    System.out.println("=================================");

    // Jika tidak ada tugas yang tersedia, maka tampilkan pesan error
    if (taskService.getTasks().isEmpty()) {
      System.out.println("Tidak ada tugas yang tersedia. Tambahkan tugas terlebih dahulu di menu utama.");
      return;
    }

    // Tampilkan daftar tugas yang tersedia
    System.out.println("Daftar Tugas:");
    int index = 1;
    for (Task task : taskService.getTasks()) {
      System.out.println(index + ". " + task.getId() + ": " + task.getName());
      index++;
    }

    // Minta pengguna untuk memilih nomor tugas yang ingin dijalankan
    System.out.print("Pilih nomor tugas untuk memulai timer: ");
    int taskNumber = scanner.nextInt();
    scanner.nextLine(); // Clear buffer

    // Jika pengguna memilih nomor tugas yang valid, maka mulai timer dengan menggunakan id tugas yang dipilih
    if (taskNumber > 0 && taskNumber <= taskService.getTasks().size()) {
      Task selectedTask = taskService.getTasks().get(taskNumber - 1);
      timerService.startTimer(String.valueOf(selectedTask.getId()));
    } else {
      System.out.println("Nomor tugas tidak valid.");
    }
  }
}

