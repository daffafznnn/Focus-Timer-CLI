package main.services;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.models.TaskLog;

public class TimerService {
  private static TimerService instance;

  private int workDuration = 1; // Default work duration in minutes
  private int shortBreak = 1; // Default short break in minutes
  private int longBreak = 2; // Default long break in minutes
  private int cycles = 4;

  private ScheduledExecutorService scheduler;
  private boolean isPaused = false;
  private int remainingTime = 0;
  private String currentTaskId;

  private final TaskService taskService = TaskService.getInstance();
  private final TaskLogService taskLogService = TaskLogService.getInstance();
  private final NotificationService notificationService = new NotificationService();

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
    final int[] cycleCount = { 0 };

    while (cycleCount[0] < cycles) {
      initializeTimer(taskId, workDuration * 60);

      scheduler = Executors.newScheduledThreadPool(1); // Initialize scheduler here

      scheduler.scheduleAtFixedRate(() -> {
        if (!isPaused) {
          if (remainingTime > 0) {
            displayRemainingTime();
            remainingTime--;
          } else {
            notificationService.playSound("src/main/resources/sound.wav");
            System.out.println("Tekan 'enter' untuk menghentikan suara.");
            scanner.nextLine(); // Wait for user input
            notificationService.stopSound();
            System.out.println("\nWaktu Habis!");
            taskLogService.addFocusTimeById(taskId, workDuration);
            stopTimer();
            cycleCount[0]++;
          }
        }
      }, 0, 1, TimeUnit.SECONDS);

      if (cycleCount[0] < cycles) {
        System.out.println("Istirahat pendek dimulai...");
        initializeTimer(taskId, shortBreak * 60);
        startBreakTimer("Istirahat pendek selesai!", "src/main/resources/sound.wav");
        taskLogService.addBreakTimeById(taskId, shortBreak);
      }
    }

    System.out.println("Istirahat panjang dimulai...");
    initializeTimer(taskId, longBreak * 60);
    startBreakTimer("Istirahat panjang selesai!", "src/main/resources/sound.wav");
    taskLogService.addBreakTimeById(taskId, longBreak);
  }

  private void startBreakTimer(String completionMessage, String soundFile) {
    notificationService.playSound(soundFile);

    System.out.println("\u001B[31mNotifikasi suara menyala. Tekan 'enter' untuk menghentikan suara!\u001B[0m");
    scanner.nextLine(); // Tunggu input dari pengguna
    notificationService.stopSound();

    System.out.println("\n" + completionMessage);
    promptContinueOrFinish();
  }

  private void promptContinueOrFinish() {
    System.out.println("Apakah Anda ingin melanjutkan tugas ini? (y/n)");
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("y")) {
      startTimer(currentTaskId);
    } else {
      stopTimer();
    }
  }

  private void displayRemainingTime() {
    int minutes = remainingTime / 60;
    int seconds = remainingTime % 60;
    System.out.printf("\rWaktu tersisa: %02d:%02d", minutes, seconds);
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