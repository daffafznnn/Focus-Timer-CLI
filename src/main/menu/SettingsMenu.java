package main.menu;

import main.services.TimerService;
import java.util.Scanner;

public class SettingsMenu {
  private Scanner scanner = new Scanner(System.in);
  private TimerService timerService = new TimerService();

  public void display() {
    System.out.println("=================================");
    System.out.println("         PENGATURAN TIMER");
    System.out.println("=================================");
    System.out.print("Masukkan durasi kerja (dalam menit): ");
    int workDuration = scanner.nextInt();
    System.out.print("Masukkan durasi istirahat pendek (dalam menit): ");
    int shortBreak = scanner.nextInt();
    System.out.print("Masukkan durasi istirahat panjang (dalam menit): ");
    int longBreak = scanner.nextInt();

    timerService.setTimerSettings(workDuration, shortBreak, longBreak);
    System.out.println("Pengaturan timer berhasil disimpan!");
  }
}