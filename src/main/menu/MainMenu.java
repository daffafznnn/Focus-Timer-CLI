package main.menu;

import java.util.Scanner;

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
      System.out.println("\u001B[34m5. Teknik Pomodoro\u001B[0m");
      System.out.println("\u001B[34m6. Keluar\u001B[0m");
      System.out.println("=================================");
      System.out.print("Masukkan pilihan Anda: ");

      String input = scanner.nextLine();

      switch (input) {
        case "1":
          new AddTaskMenu().display();
          break;
        case "2":
          new StartWorkSessionMenu().display();
          break;
        case "3":
          new SettingsMenu().display();
          break;
        case "4":
          new LogActivityMenu().display();
          break;
        case "5":
          new PomodoroMenu().display();
          break;
        case "6":
          System.out.println("Terima kasih telah menggunakan Focus Timer!");
          return;
        default:
          System.out.println("Pilihan tidak valid. Silakan coba lagi.");
      }
    }
  }
}
