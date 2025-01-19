package main.menu;

import main.models.Task;
import main.services.PomodoroTimerService;
import main.services.TaskService;

import java.util.Scanner;

public class PomodoroMenu {
  private final Scanner scanner = new Scanner(System.in);
  private final PomodoroTimerService pomodoroTimerService = PomodoroTimerService.getInstance();
  private final TaskService taskService = TaskService.getInstance();

  public void display() {
    boolean isRunning = true;
    while (isRunning) {
      System.out.println("=================================");
      System.out.println("         TEKNIK POMODORO");
      System.out.println("=================================");
      System.out.println("Teknik Pomodoro membantu Anda fokus dengan interval waktu tertentu.");
      System.out.println("\u001B[34m1. Penjelasan tentang Teknik Pomodoro\u001B[0m");
      System.out.println("\u001B[34m2. Mulai Teknik Pomodoro\u001B[0m");
      System.out.println("\u001B[34m3. Kembali ke Halaman Utama\u001B[0m");
      System.out.println("=================================");
      System.out.print("Pilihan Anda: ");
      int choice = scanner.nextInt();
      scanner.nextLine(); // Clear the buffer

      switch (choice) {
        case 1:
          showExplanation();
          break;
        case 2:
          startPomodoroSession();
          break;
        case 3:
          isRunning = false;
          break;
        default:
          System.out.println("Pilihan tidak valid.");
      }
    }
  }

  private void showExplanation() {
    System.out.println("=================================================");
    System.out.println("      TENTANG DAN LANGKAH TEKNIK POMODORO");
    System.out.println("=================================================");
    System.out.println("Teknik Pomodoro adalah metode manajemen waktu yang dikembangkan oleh Francesco Cirillo pada akhir tahun 1980-an.");
    System.out.println("Teknik ini menggunakan timer untuk membagi waktu kerja menjadi interval, biasanya 25 menit, yang disebut 'Pomodoro', diikuti oleh istirahat singkat, biasanya 5 menit.");
    System.out.println("Setelah empat interval kerja (empat sesi), Anda mengambil istirahat lebih lama, biasanya 15-30 menit.");
    System.out.println("Langkah-langkah Teknik Pomodoro:");
    System.out.println("\u001B[34m1. Pilih tugas yang akan dikerjakan.\u001B[0m");
    System.out.println("\u001B[34m2. Atur timer selama 25 menit dan mulai mengerjakan tugas tersebut.\u001B[0m");
    System.out.println("\u001B[34m3. Bekerja sampai timer berbunyi, lalu beri tanda centang pada selembar kertas.\u001B[0m");
    System.out.println("\u001B[34m4. Istirahat singkat selama 5 menit.\u001B[0m");
    System.out.println("\u001B[34m5. Setelah empat Pomodoro, ambil istirahat lebih lama selama 15-30 menit.\u001B[0m");
    System.out.println("=================================================");
    System.out.println("Kembali ke menu sebelumnya? (Y/N):");

    String input = scanner.nextLine().trim();
    while (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")) {
      System.out.println("Pilihan tidak valid. Kembali ke menu sebelumnya? (Y/N):");
      input = scanner.nextLine().trim();
    }
  }

  private void startPomodoroSession() {
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

    System.out.print("Masukkan nomor tugas: ");
    int taskNumber = scanner.nextInt();
    scanner.nextLine(); // Clear the buffer

    if (taskNumber > 0 && taskNumber <= taskService.getTasks().size()) {
      Task selectedTask = taskService.getTasks().get(taskNumber - 1);
      String selectedTaskId = String.valueOf(selectedTask.getId());
      pomodoroTimerService.startPomodoro(selectedTaskId);

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
          pomodoroTimerService.pauseTimer();
          break;
        case 2:
          pomodoroTimerService.resumeTimer();
          break;
        case 3:
          pomodoroTimerService.stopTimer();
          isRunning = false;
          break;
        case 0:
          pomodoroTimerService.stopTimer();
          isRunning = false;
          break;
        default:
          System.out.println("Pilihan tidak valid.");
      }
    }
  }
}

