package main.services;

import main.models.TaskLog;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PomodoroTimerService {
  private static PomodoroTimerService instance;

  private final int workDuration = 1; // Fixed work duration in minutes
  private final int shortBreak = 1; // Fixed short break in minutes
  private final int longBreak = 2; // Fixed long break in minutes
  private final int cycles = 4;

  private ScheduledExecutorService scheduler;
  private boolean isPaused = false;
  private int remainingTime = 0;
  private String currentTaskId;

  private final TaskService taskService = TaskService.getInstance();
  private final TaskLogService taskLogService = TaskLogService.getInstance();
  private final NotificationService notificationService = new NotificationService();
  private int pomodoroCycleCount = 0;

  private final Scanner scanner = new Scanner(System.in);

  private PomodoroTimerService() {
  }

  public static PomodoroTimerService getInstance() {
    if (instance == null) {
      instance = new PomodoroTimerService();
    }
    return instance;
  }

  public void startPomodoro(String taskId) {
    if (isTimerRunning()) {
      System.out.println("Timer sedang berjalan. Harap hentikan terlebih dahulu.");
      return;
    }
    int taskIdInt = Integer.parseInt(taskId);
    String taskName = taskService.getTaskNameById(taskIdInt);
    if (taskName == null) {
      System.out.println("Tugas tidak ditemukan.");
      return;
    }

    System.out.println("Memulai Teknik Pomodoro untuk tugas: " + taskName);
    isPaused = false;
    currentTaskId = taskId;

    scheduler = Executors.newScheduledThreadPool(1); // Initialize scheduler here

    new Thread(() -> {
      try {
        for (int i = 1; i <= cycles; i++) {
          System.out.println("Sesi kerja ke-" + i + " dimulai...");
          initializeTimer(taskId, workDuration * 60);
          runTimer("Sesi kerja ke-" + i + " selesai!");
          taskLogService.addFocusTimeById(taskId, workDuration);
          notificationService.playSound("src/main/resources/sound.wav");
          System.out.println("Lanjutkan sesi atau selesai?");
          System.out.println("1. Lanjutkan sesi");
          System.out.println("2. Selesai");
          int choice = scanner.nextInt();
          scanner.nextLine(); // Clear buffer
          notificationService.stopSound();
          if (choice == 1) {
            if (i < cycles) {
              promptBreakOrFinish(taskId);
              taskLogService.addBreakTimeById(taskId, shortBreak);
            }
          } else if (choice == 2) {
            break;
          } else {
            System.out.println("Pilihan tidak valid.");
          }
        }

        System.out.println("Istirahat panjang dimulai...");
        initializeTimer(taskId, longBreak * 60);
        runBreakTimer("Istirahat panjang selesai!", "src/main/resources/sound.wav");
        taskLogService.addBreakTimeById(taskId, longBreak);
        pomodoroCycleCount = 0; // Reset siklus setelah istirahat panjang
        taskLogService.addBreakTimeById(taskId, longBreak);

        System.out.println("Teknik Pomodoro selesai!");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Pomodoro dihentikan.");
      } finally {
        scheduler.shutdown();
        saveTaskLog();
        currentTaskId = null;
      }
    }).start();
  }

  public void startTimer(String taskId) {
    startPomodoro(taskId);
  }

  public void pauseTimer() {
    if (!isTimerRunning()) {
      System.out.println("Timer tidak berjalan.");
      return;
    }
    isPaused = true;
    System.out.println("Timer dijeda.");
  }

  public void resumeTimer() {
    if (!isTimerRunning()) {
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
        saveTaskLog();
      } else {
        System.out.println("Tugas belum selesai. Anda dapat melanjutkan nanti.");
      }

      currentTaskId = null;
    } else {
      System.out.println("Timer tidak berjalan.");
    }
  }

  private void promptBreakOrFinish(String taskId) {
    if (pomodoroCycleCount >= cycles) {
      System.out.println("Istirahat panjang dimulai...");
      initializeTimer(taskId, longBreak * 60);
      runBreakTimer("Istirahat panjang selesai!", "src/main/resources/sound.wav");
      taskLogService.addBreakTimeById(taskId, longBreak);
      pomodoroCycleCount = 0; // Reset siklus setelah istirahat panjang
    } else {
      System.out.println("Istirahat pendek dimulai...");
      initializeTimer(taskId, shortBreak * 60);
      runBreakTimer("Istirahat pendek selesai!", "src/main/resources/sound.wav");
      taskLogService.addBreakTimeById(taskId, shortBreak);
      pomodoroCycleCount++;
    }
  }

  private void runBreakTimer(String completionMessage, String soundFile) {
    notificationService.playSound(soundFile);

    System.out.println("\u001B[31mNotifikasi suara menyala. Tekan 'enter' untuk menghentikan suara!\u001B[0m");
    scanner.nextLine(); // Tunggu input dari pengguna
    notificationService.stopSound();
    System.out.println("\u001B[31mNotifikasi suara dihentikan.\u001B[0m");

    // Mulai timer istirahat setelah suara dihentikan
    startBreakTimer(completionMessage);
  }

  private void startBreakTimer(String completionMessage) {
    scheduler = Executors.newScheduledThreadPool(1); // Initialize scheduler here

    scheduler.scheduleAtFixedRate(() -> {
      if (!isPaused) {
        if (remainingTime > 0) {
          displayRemainingTime();
          remainingTime--;
        } else {
          System.out.println("\n" + completionMessage);
          scheduler.shutdown();
          promptContinueOrFinish();
        }
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  private void displayRemainingTime() {
    int minutes = remainingTime / 60;
    int seconds = remainingTime % 60;
    System.out.printf("Sisa waktu: %02d:%02d\r", minutes, seconds);
  }

  private void promptContinueOrFinish() {
    System.out.println("Selesai istirahat?");
    System.out.println("\u001B[34m1. Lanjutkan ke siklus berikutnya\u001B[0m");
    System.out.println("\u001B[34m2. Selesaikan tugas\u001B[0m");

    int input = -1;
    while (input != 1 && input != 2) {
      System.out.print("Pilihan anda: ");
      if (scanner.hasNextInt()) {
        input = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
      } else {
        System.out.println("Input tidak valid. Masukkan angka 1 atau 2.");
        scanner.nextLine(); // Clear invalid input
      }
    }

    if (input == 1) {
      startPomodoro(currentTaskId);
    } else {
      saveTaskLog();
      System.out.println("Tugas telah selesai. Data akan dicatat.");
      currentTaskId = null;
    }
  }

  private void runTimer(String completionMessage) throws InterruptedException {
    scheduler.scheduleAtFixedRate(() -> {
      if (!isPaused && remainingTime > 0) {
        displayRemainingTime();
        remainingTime--;
      }
    }, 0, 1, TimeUnit.SECONDS);

    while (remainingTime > 0) {
      Thread.sleep(1000);
    }
    System.out.println("\n" + completionMessage);
  }

  private void initializeTimer(String taskId, int duration) {
    remainingTime = duration;
    isPaused = false;
    currentTaskId = taskId;
  }

  private boolean isTimerRunning() {
    return scheduler != null && !scheduler.isShutdown();
  }

  private void saveTaskLog() {
    int taskIdInt = Integer.parseInt(currentTaskId);
    TaskLog taskLog = taskService.getTaskLogById(taskIdInt);
    if (taskLog != null) {
      taskLogService.addTaskLog(taskLog);
    }
  }
}