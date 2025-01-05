package main.menu;

import java.util.Scanner;

public class MainMenu {
  private Scanner scanner = new Scanner(System.in);

  public void display() {
    while (true) {
      System.out.println("=================================");
      System.out.println("          FOCUS TIMER");
      System.out.println("=================================");
      System.out.println("1. Tambahkan Tugas");
      System.out.println("2. Mulai Sesi Kerja");
      System.out.println("3. Pengaturan Timer");
      System.out.println("4. Lihat Log Aktivitas");
      System.out.println("5. Teknik Pomodoro");
      System.out.println("6. Keluar");
      System.out.println("=================================");
      System.out.print("Masukkan pilihan Anda: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Clear the buffer

      switch (choice) {
        case 1:
          new AddTaskMenu().display();
          break;
        case 2:
          new StartWorkSessionMenu().display();
          break;
        case 3:
          new SettingsMenu().display();
          break;
        case 4:
          new LogActivityMenu().display();
          break;
        case 5:
          new PomodoroMenu().display();
          break;
        case 6:
          System.out.println("Terima kasih telah menggunakan Focus Timer!");
          return;
        default:
          System.out.println("Pilihan tidak valid, coba lagi.");
      }
    }
  }
}