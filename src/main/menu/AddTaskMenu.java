package main.menu;

import main.services.TaskService;
import main.models.Task;

import java.util.Scanner;

public class AddTaskMenu {
  private final Scanner scanner = new Scanner(System.in);
  private final TaskService taskService = TaskService.getInstance();

  public void display() {
    System.out.println("=================================");
    System.out.println("          TAMBAHKAN TUGAS");
    System.out.println("=================================");

    while (true) {
      System.out.println("Daftar Tugas:");
      if (taskService.getTasks().isEmpty()) {
        System.out.println("Tidak ada tugas. Tambahkan tugas baru.");
      } else {
        int index = 1;
        // Ganti 'var' dengan tipe eksplisit (misalnya Task)
        for (Task task : taskService.getTasks()) {
          System.out.println(index++ + ". " + task.getName());
        }
      }

      System.out.print("Masukkan nama tugas baru (atau ketik 'exit' untuk kembali): ");
      String taskName = scanner.nextLine();

      if (taskName.equalsIgnoreCase("exit")) {
        return;
      }

     if (taskName.trim().isEmpty()) {
        System.out.println("Nama tugas tidak boleh kosong!");
    } else {
      taskService.addTask(taskName.trim()); // Menambahkan tugas baru
      System.out.println("Tugas berhasil ditambahkan!");
    }
    }
  }
}
