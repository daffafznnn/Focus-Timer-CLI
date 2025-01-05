package main.services;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService {
  private int workDuration = 25;
  private int shortBreak = 5;
  private int longBreak = 15;

  public void setTimerSettings(int workDuration, int shortBreak, int longBreak) {
    this.workDuration = workDuration;
    this.shortBreak = shortBreak;
    this.longBreak = longBreak;
  }

  public void startTimer(String taskName) {
    System.out.println("Memulai timer untuk tugas: " + taskName);
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("Waktu habis! Sesi kerja selesai untuk tugas: " + taskName);
        timer.cancel();
      }
    }, workDuration * 60 * 1000); // Convert to milliseconds
  }

  public void startPomodoro() {
    System.out.println("Memulai Teknik Pomodoro...");
    for (int i = 1; i <= 4; i++) {
      System.out.println("Sesi kerja ke-" + i);
      startWorkSession();
      if (i < 4) {
        System.out.println("Istirahat pendek...");
        startBreak(shortBreak);
      }
    }
    System.out.println("Istirahat panjang...");
    startBreak(longBreak);
  }

  private void startWorkSession() {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("Sesi kerja selesai!");
        timer.cancel();
      }
    }, workDuration * 60 * 1000);
  }

  private void startBreak(int duration) {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("Waktu istirahat selesai!");
        timer.cancel();
      }
    }, duration * 60 * 1000);
  }
}
