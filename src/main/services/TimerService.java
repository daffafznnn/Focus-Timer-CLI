package main.services;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerService {
  private static TimerService instance;

  private int workDuration = 25; // Default work duration in minutes
  private int shortBreak = 5; // Default short break in minutes
  private int longBreak = 15; // Default long break in minutes

  private ScheduledExecutorService scheduler;
  private boolean isPaused = false;
  private int remainingTime = 0;
  private String currentTaskId;

  private TaskService taskService = TaskService.getInstance();
  private TaskLogService taskLogService = TaskLogService.getInstance();
  private int pomodoroCycleCount = 0;

  private Scanner scanner = new Scanner(System.in);

  private TimerService() {
  }

  public static TimerService getInstance() {
    if (instance == null) {
      instance = new TimerService();
    }
    return instance;
  }

  public int getWorkDuration() {
    return workDuration;
  }

  public int getShortBreak() {
    return shortBreak;
  }

  public int getLongBreak() {
    return longBreak;
  }

  public void setTimerSettings(int workDuration, int shortBreak, int longBreak) {
    this.workDuration = workDuration;
    this.shortBreak = shortBreak;
    this.longBreak = longBreak;
  }

  public void startTimer(String taskId) {
    if (scheduler != null && !scheduler.isShutdown()) {
      System.out.println("Timer sedang berjalan. Harap hentikan terlebih dahulu.");
      return;
    }

    String taskName = taskService.getTaskNameById(taskId);
    if (taskName == null) {
      System.out.println("Tugas tidak ditemukan.");
      return;
    }

    System.out.println("Memulai timer untuk tugas: " + taskName);
    remainingTime = workDuration * 60;
    isPaused = false;
    currentTaskId = taskId;

    scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(() -> {
      if (!isPaused) {
        if (remainingTime > 0) {
          int minutes = remainingTime / 60;
          int seconds = remainingTime % 60;
          System.out.printf("Waktu tersisa: %02d:%02d\r", minutes, seconds);
          remainingTime--;
        } else {
          System.out.println("\nWaktu kerja selesai!");
          taskLogService.addFocusTimeById(taskId, workDuration);
          promptBreakOrFinish(taskId);
        }
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  public void pauseTimer() {
    if (scheduler == null || scheduler.isShutdown()) {
      System.out.println("Timer tidak berjalan.");
      return;
    }
    isPaused = true;
    System.out.println("Timer dijeda.");
  }

  public void resumeTimer() {
    if (scheduler == null || scheduler.isShutdown()) {
      System.out.println("Timer tidak berjalan.");
      return;
    }
    isPaused = false;
    System.out.println("Timer dilanjutkan.");
  }

  public void stopTimer() {
    if (scheduler != null) {
      scheduler.shutdown();
      scheduler = null;

      System.out.println("Apakah tugas telah selesai? (y/n)");
      String input = scanner.nextLine();

      if (input.equalsIgnoreCase("y")) {
        System.out.println("Tugas telah selesai. Data akan dicatat.");
      } else {
        System.out.println("Tugas belum selesai. Anda dapat melanjutkan nanti.");
      }

      currentTaskId = null;
    } else {
      System.out.println("Timer tidak berjalan.");
    }
  }

  private void promptBreakOrFinish(String taskId) {
    if (pomodoroCycleCount >= 5) {
      System.out.println("Istirahat panjang dimulai...");
      remainingTime = longBreak * 60;
      try {
        runBreakTimer("Istirahat panjang selesai!");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Istirahat panjang terganggu.");
      }
      taskLogService.addBreakTimeById(taskId, longBreak);
      pomodoroCycleCount = 0;
    } else {
      System.out.println("Istirahat pendek dimulai...");
      remainingTime = shortBreak * 60;
      try {
        runBreakTimer("Istirahat pendek selesai!");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Istirahat pendek terganggu.");
      }
      taskLogService.addBreakTimeById(taskId, shortBreak);
      pomodoroCycleCount++;
    }

    System.out.println("Apakah Anda ingin melanjutkan fokus? (y/n)");
    String input = scanner.nextLine();

    if (input.equalsIgnoreCase("y")) {
      startTimer(currentTaskId);
    } else {
      stopTimer();
    }
  }

  private void runBreakTimer(String completionMessage) throws InterruptedException {
    ScheduledExecutorService breakScheduler = Executors.newScheduledThreadPool(1);
    breakScheduler.scheduleAtFixedRate(() -> {
      if (remainingTime > 0) {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        System.out.printf("Waktu istirahat tersisa: %02d:%02d\r", minutes, seconds);
        remainingTime--;
      }
    }, 0, 1, TimeUnit.SECONDS);

    while (remainingTime > 0) {
      Thread.sleep(1000);
    }

    System.out.println("\n" + completionMessage);
    breakScheduler.shutdown();
  }


  public void startPomodoro(String taskId) {
    if (scheduler != null && !scheduler.isShutdown()) {
      System.out.println("Timer sedang berjalan. Harap hentikan terlebih dahulu.");
      return;
    }
    String taskName = taskService.getTaskNameById(taskId);
    if (taskName == null) {
      System.out.println("Tugas tidak ditemukan.");

      return;
    }

    System.out.println("Memulai Teknik Pomodoro untuk tugas: " + taskName);
    isPaused = false;
    currentTaskId = taskId;

    scheduler = Executors.newScheduledThreadPool(1);
    new Thread(() -> {
      try {
        for (int i = 1; i <= 4; i++) {
          System.out.println("Sesi kerja ke-" + i + " dimulai...");
          remainingTime = workDuration * 60;
          runTimer("Sesi kerja ke-" + i + " selesai!");
          taskLogService.addFocusTimeById(taskId, workDuration);

          if (i < 4) {
            System.out.println("Istirahat pendek dimulai...");
            remainingTime = shortBreak * 60;
            runTimer("Istirahat pendek selesai!");
            taskLogService.addBreakTimeById(taskId, shortBreak);
          }
        }

        System.out.println("Istirahat panjang dimulai...");
        remainingTime = longBreak * 60;
        runTimer("Istirahat panjang selesai!");
        taskLogService.addBreakTimeById(taskId, longBreak);

        System.out.println("Teknik Pomodoro selesai!");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Pomodoro dihentikan.");
      } finally {
        scheduler.shutdown();
        currentTaskId = null;
      }
    }).start();
  }

  private void runTimer(String completionMessage) throws InterruptedException {
    scheduler.scheduleAtFixedRate(() -> {
      if (!isPaused && remainingTime > 0) {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        System.out.printf("Waktu tersisa: %02d:%02d\r", minutes, seconds);
        remainingTime--;
      }
    }, 0, 1, TimeUnit.SECONDS);

    while (remainingTime > 0) {
      Thread.sleep(1000);
    }
    System.out.println("\n" + completionMessage);
  }
}

