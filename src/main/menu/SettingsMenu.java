package main.menu;

import main.services.TimerService;

import java.util.Scanner;

/**
 * Kelas SettingsMenu menyediakan antarmuka bagi pengguna untuk mengubah 
 * pengaturan timer, termasuk durasi fokus, durasi istirahat, dan jumlah siklus.
 */
public class SettingsMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final TimerService timerService = TimerService.getInstance();

    /**
     * Menampilkan menu pengaturan kepada pengguna dan menangani input pengguna 
     * untuk mengubah pengaturan timer.
     */
    public void display() {
        while (true) {
            System.out.println("=================================");
            System.out.println("         PENGATURAN TIMER");
            System.out.println("=================================");

            // Menampilkan pengaturan waktu saat ini
            System.out.println("Pengaturan waktu saat ini:");
            System.out.println("- Durasi fokus saat ini: " + timerService.getWorkDuration() + " menit");
            System.out.println("- Durasi istirahat singkat saat ini: " + timerService.getShortBreak() + " menit");
            System.out.println("- Durasi istirahat panjang saat ini: " + timerService.getLongBreak() + " menit");
            System.out.println("- Jumlah siklus: " + timerService.getCycles() + " siklus");

            // Menampilkan pilihan tindakan kepada pengguna
            System.out.println("Pilih tindakan:");
            System.out.println("\u001B[34m1. Ubah durasi fokus\u001B[0m");
            System.out.println("\u001B[34m2. Ubah durasi istirahat singkat\u001B[0m");
            System.out.println("\u001B[34m3. Ubah durasi istirahat panjang\u001B[0m");
            System.out.println("\u001B[34m4. Ubah jumlah siklus\u001B[0m");
            System.out.println("\u001B[34m5. Kembali ke menu utama\u001B[0m");
            System.out.print("Pilihan: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Mengosongkan buffer input

            // Menangani pilihan pengguna
            switch (choice) {
                case 1:
                    System.out.print("Masukkan durasi fokus (menit): ");
                    int workDuration = scanner.nextInt();
                    timerService.setTimerSettings(workDuration, timerService.getShortBreak(), 
                        timerService.getLongBreak(), timerService.getCycles());
                    System.out.println("Pengaturan berhasil disimpan!");
                    break;
                case 2:
                    System.out.print("Masukkan durasi istirahat singkat (menit): ");
                    int shortBreak = scanner.nextInt();
                    timerService.setTimerSettings(timerService.getWorkDuration(), shortBreak, 
                        timerService.getLongBreak(), timerService.getCycles());
                    System.out.println("Pengaturan berhasil disimpan!");
                    break;
                case 3:
                    System.out.print("Masukkan durasi istirahat panjang (menit): ");
                    int longBreak = scanner.nextInt();
                    timerService.setTimerSettings(timerService.getWorkDuration(), timerService.getShortBreak(), 
                        longBreak, timerService.getCycles());
                    System.out.println("Pengaturan berhasil disimpan!");
                    break;
                case 4:
                    System.out.print("Masukkan jumlah siklus: ");
                    int cycles = scanner.nextInt();
                    timerService.setTimerSettings(timerService.getWorkDuration(), timerService.getShortBreak(), 
                        timerService.getLongBreak(), cycles);
                    System.out.println("Pengaturan berhasil disimpan!");
                    break;
                case 5:
                    System.out.println("Kembali ke menu utama");
                    return;
                default:
                    System.out.println("Pilihan tidak valid");
                    break;
            }
        }
    }
}
