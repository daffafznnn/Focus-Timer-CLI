package main.menu;

import java.util.Scanner;

/**
 * Kelas ini berisi menu utama dari aplikasi Focus Timer.
 * Menu ini akan menampilkan pilihan-pilihan yang tersedia
 * dan akan mengarahkan pengguna ke menu yang sesuai.
 */
public class MainMenu {
  private final Scanner scanner = new Scanner(System.in);

  public void display() {
    while (true) {
      System.out.println("=================================");
      System.out.println("          FOCUS TIMER");
      System.out.println("=================================");
      System.out.println("\u001B[34m1. Tambahkan Tugas\u001B[0m");
      System.out.println("\u001B[34m2. Mulai Sesi Kerja\u001B[0m");
      System.out.println("\u001B[34m3. Pengaturan Timer\u001B[0m");
      System.out.println("\u001B[34m4. Lihat Log Aktivitas\u001B[0m");
      System.out.println("\u001B[34m5. Tentang Teknik Pomodoro\u001B[0m");
      System.out.println("\u001B[34m6. Keluar\u001B[0m");
      System.out.println("=================================");
      System.out.print("Masukkan pilihan Anda: ");

      String input = scanner.nextLine();

      switch (input) {
        case "1":
          // Menu untuk menambahkan tugas baru
          new AddTaskMenu().display();
          break;
        case "2":
          // Menu untuk memulai sesi kerja
          new StartWorkSessionMenu().display();
          break;
        case "3":
          // Menu untuk mengatur timer
          new SettingsMenu().display();
          break;
        case "4":
          // Menu untuk melihat log aktivitas
          new LogActivityMenu().display();
          break;
        case "5":
          // Menu untuk membaca tentang Teknik Pomodoro
          new PomodoroMenu().display();
          break;
        case "6":
          System.out.print("Apakah anda yakin ingin keluar? (y/n): ");
          String confirmExit = scanner.nextLine();
          if (confirmExit.equalsIgnoreCase("y")) {
            System.out.println("Terima kasih telah menggunakan Focus Timer!");
            return;
          } else {
            System.out.println("Kembali ke menu utama.");
          }
          break;
        default:
          System.out.println("Pilihan tidak valid. Silakan coba lagi.");
      }
    }
  }
}

