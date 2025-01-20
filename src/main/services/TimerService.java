package main.services;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.models.TaskLog;

public class TimerService {
  private static TimerService instance;

  private int workDuration = 1; // Durasi kerja default dalam menit
  private int shortBreak = 1; // Durasi istirahat pendek dalam menit
  private int longBreak = 2; // Durasi istirahat panjang dalam menit
  private int cycles = 4; // Jumlah siklus fokus

  private ScheduledExecutorService scheduler;
  private boolean isPaused = false;
  private int remainingTime = 0;
  private int currentCycle = 1;
  private String currentTaskId;
  private String currentTaskName;
  private int totalFocusDuration = 0; // Total waktu fokus yang tercatat
  private int totalBreakDuration = 0; // Total waktu istirahat yang tercatat

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

    String taskName = taskService.getTaskNameById(Integer.parseInt(taskId));
    if (taskName == null) {
      System.out.println("Tugas tidak ditemukan.");
      return;
    }

    currentTaskId = taskId;
    currentTaskName = taskName;
    System.out.println("Memulai timer untuk tugas: " + taskName);

    while (true) {
      System.out.println("\nSesi fokus ke-" + currentCycle + " dimulai.");
      initializeTimer(workDuration * 60);
      runTimer("fokus");

      // Setelah sesi fokus selesai, mainkan suara notifikasi
      notificationService.playSound("src/main/resources/sound.wav");
      System.out.println("\nWaktu fokus habis! Tekan 'enter' untuk menghentikan suara.");
      scanner.nextLine(); // Tunggu pengguna menekan enter untuk menghentikan suara
      notificationService.stopSound(); // Hentikan suara setelah enter

      // Lanjutkan ke sesi istirahat setelah suara dihentikan
      if (currentCycle < cycles) {
        System.out.println("\nSesi fokus selesai. Istirahat pendek dimulai.");
        initializeTimer(shortBreak * 60);
        runTimer("istirahat pendek");
      } else {
        System.out.println("\nSemua siklus selesai. Istirahat panjang dimulai.");
        initializeTimer(longBreak * 60);
        runTimer("istirahat panjang");
        currentCycle = 1; // Reset siklus setelah istirahat panjang
      }

      // Setelah sesi istirahat selesai, tampilkan pilihan
      if (!promptContinueOrFinish()) {
        break;
      }

      currentCycle++;
    }

    System.out.println("Timer selesai. Semua data telah dicatat.");
  }

  private void runTimer(String type) {
    scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(() -> {
      if (!isPaused) {
        if (remainingTime > 0) {
          displayRemainingTime();
          remainingTime--;
        } else {
          stopTimer();
          handleTimerCompletion(type); // Menangani akhir timer
        }
      }
    }, 0, 1, TimeUnit.SECONDS);

    while (remainingTime > 0) {
      try {
        Thread.sleep(1000); // Cegah eksekusi langsung
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private void handleTimerCompletion(String type) {
    int elapsedTime = (type.equals("fokus") ? (workDuration * 60)
        : (type.equals("istirahat pendek") ? shortBreak * 60 : longBreak * 60)) - remainingTime;
    int elapsedMinutes = elapsedTime / 60;

    if (type.equals("fokus")) {
      totalFocusDuration += elapsedMinutes;
      System.out.println("Sesi fokus selesai: " + elapsedMinutes + " menit ditambahkan.");
    } else {
      totalBreakDuration += elapsedMinutes;
      System.out.println("Sesi istirahat selesai: " + elapsedMinutes + " menit ditambahkan.");
    }
  }

  private boolean promptContinueOrFinish() {
    System.out.println("\nSelesai istirahat?");
    System.out.println("\u001B[34m1. Lanjutkan ke sesi berikutnya\u001B[0m");
    System.out.println("\u001B[34m2. Selesaikan tugas dan simpan log\u001B[0m");
    System.out.println("Pilihan Anda: ");
    String input = scanner.nextLine().trim();

    if (input.equals("1")) {
      return true; // Lanjutkan ke sesi berikutnya
    } else if (input.equals("2")) {
      saveTaskLog(); // Simpan log tugas
      return false; // Selesai
    }
    return true;
  }

  private void displayRemainingTime() {
    int minutes = remainingTime / 60;
    int seconds = remainingTime % 60;
    System.out.printf("\rWaktu tersisa: %02d:%02d", minutes, seconds);
  }

  private void initializeTimer(int duration) {
    remainingTime = duration;
    isPaused = false;
  }

  private void stopTimer() {
    if (scheduler != null) {
      scheduler.shutdownNow();
      scheduler = null;
      remainingTime = 0;
    }
  }

  private boolean isTimerRunning() {
    return scheduler != null && !scheduler.isShutdown();
  }

  private void saveTaskLog() {
    if (totalFocusDuration > 0 || totalBreakDuration > 0) {
      taskLogService.updateTaskLog(currentTaskName, totalFocusDuration, totalBreakDuration);
      System.out.println("Log aktivitas disimpan: " +
          "Fokus: " + totalFocusDuration + " menit, " +
          "Istirahat: " + totalBreakDuration + " menit.");
    } else {
      System.out.println("Tidak ada aktivitas yang dicatat.");
    }

    // Reset durasi setelah log disimpan
    totalFocusDuration = 0;
    totalBreakDuration = 0;
  }

}
