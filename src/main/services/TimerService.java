package main.services;

import main.models.TaskLog;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerService {
  private static TimerService instance;

  private int workDuration = 25; // Default work duration in minutes
  private int shortBreak = 5; // Default short break in minutes
  private int longBreak = 15; // Default long break in minutes
  private int cycles = 4;

  private ScheduledExecutorService scheduler;
  private boolean isPaused = false;
  private int remainingTime = 0;
  private String currentTaskId;

  private final TaskService taskService = TaskService.getInstance();
  private final TaskLogService taskLogService = TaskLogService.getInstance();
  private final NotificationService notificationService = new NotificationService();
  private int pomodoroCycleCount = 0;

  private final Scanner scanner = new Scanner(System.in);

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

  public int getCycles() {
    return cycles;
  }

  public void setTimerSettings(int workDuration, int shortBreak, int longBreak, int cycles) {
    this.workDuration = workDuration;
    this.shortBreak = shortBreak;
    this.longBreak = longBreak;
    this.cycles = cycles;
  }

  public void startTimer(String taskId) {
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

    System.out.println("Memulai timer untuk tugas: " + taskName);
    initializeTimer(taskId, workDuration * 60);

    scheduler = Executors.newScheduledThreadPool(1); // Initialize scheduler here

    scheduler.scheduleAtFixedRate(() -> {
      if (!isPaused) {
        if (remainingTime > 0) {
          displayRemainingTime();
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

          if (i < cycles) {
            System.out.println("Istirahat pendek dimulai...");
            initializeTimer(taskId, shortBreak * 60);
            runBreakTimer("Istirahat pendek selesai!", "src/main/resources/sound.wav");
            taskLogService.addBreakTimeById(taskId, shortBreak);
          }
        }

        System.out.println("Istirahat panjang dimulai...");
        initializeTimer(taskId, longBreak * 60);
        runBreakTimer("Istirahat panjang selesai!", "src/main/resources/sound.wav");
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

  private void promptBreakOrFinish(String taskId) {
    if (pomodoroCycleCount >= cycles) {
      System.out.println("Istirahat panjang dimulai...");
      initializeTimer(taskId, longBreak * 60);
      runBreakTimer("Istirahat panjang selesai!", "src/main/resources/sound.wav");
      taskLogService.addBreakTimeById(taskId, longBreak);
      pomodoroCycleCount = 0;
    } else {
      System.out.println("Istirahat pendek dimulai...");
      initializeTimer(taskId, shortBreak * 60);
      runBreakTimer("Istirahat pendek selesai!", "src/main/resources/sound.wav");
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

  private void runBreakTimer(String completionMessage, String soundFile) {
    notificationService.playSound(soundFile);

    System.out.println("Notifikasi suara menyala. Masukkan 's' untuk mematikan suara.");
    String input = scanner.nextLine();

    if (input.equalsIgnoreCase("s")) {
      notificationService.stopSound();
    }

    initializeTimer(currentTaskId, remainingTime); // Initialize break timer

    scheduler = Executors.newScheduledThreadPool(1);
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

  private void promptContinueOrFinish() {
    System.out.println("Apakah Anda ingin melanjutkan fokus? (y/n)");
    String input = scanner.nextLine();

    if (input.equalsIgnoreCase("y")) {
      startTimer(currentTaskId);
    } else {
      stopTimer();
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

  private void displayRemainingTime() {
    int minutes = remainingTime / 60;
    int seconds = remainingTime % 60;
    System.out.printf("Waktu tersisa: %02d:%02d\r", minutes, seconds);
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