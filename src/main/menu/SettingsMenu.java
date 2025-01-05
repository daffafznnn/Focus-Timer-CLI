package main.menu;

import main.services.TimerService;

import java.util.Scanner;

public class SettingsMenu {
  private final Scanner scanner = new Scanner(System.in);
  private final TimerService timerService = TimerService.getInstance();

  public void display() {
    System.out.println("=================================");
    System.out.println("         PENGATURAN TIMER");
    System.out.println("=================================");

    System.out.println("Pengaturan waktu saat ini:");
    System.out.println("- Durasi kerja: " + timerService.getWorkDuration() + " menit");
    System.out.println("- Istirahat pendek: " + timerService.getShortBreak() + " menit");
    System.out.println("- Istirahat panjang: " + timerService.getLongBreak() + " menit");

    try {
      System.out.print("Masukkan durasi kerja (menit): ");
      int workDuration = scanner.nextInt();
      System.out.print("Masukkan durasi istirahat pendek (menit): ");
      int shortBreak = scanner.nextInt();
      System.out.print("Masukkan durasi istirahat panjang (menit): ");
      int longBreak = scanner.nextInt();

      timerService.setTimerSettings(workDuration, shortBreak, longBreak);
      System.out.println("Pengaturan berhasil disimpan!");
    } catch (Exception e) {
      System.out.println("Input tidak valid. Kembali ke menu utama.");
      scanner.nextLine(); // Clear invalid input
    }
  }
}
