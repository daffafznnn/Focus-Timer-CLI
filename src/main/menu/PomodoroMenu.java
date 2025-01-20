package main.menu;

import java.util.Scanner;

public class PomodoroMenu {
  private final Scanner scanner = new Scanner(System.in);

  public void display() {
    System.out.println("=================================================");
    System.out.println("      TENTANG DAN LANGKAH TEKNIK POMODORO");
    System.out.println("=================================================");
    System.out.println("Teknik Pomodoro adalah metode manajemen waktu yang dikembangkan oleh Francesco Cirillo pada akhir tahun 1980-an.");
    System.out.println("Teknik ini menggunakan timer untuk membagi waktu kerja menjadi interval, biasanya 25 menit, yang disebut 'Pomodoro', diikuti oleh istirahat singkat, biasanya 5 menit.");
    System.out.println("Setelah empat interval kerja (empat sesi), Anda mengambil istirahat lebih lama, biasanya 15-30 menit.");
    System.out.println("Langkah-langkah Teknik Pomodoro:");
    System.out.println("\u001B[34m1. Pilih tugas yang akan dikerjakan.\u001B[0m");
    System.out.println("\u001B[34m2. Atur timer selama 25 menit dan mulai mengerjakan tugas tersebut.\u001B[0m");
    System.out.println("\u001B[34m3. Bekerja sampai timer berbunyi, lalu beri tanda centang pada selembar kertas.\u001B[0m");
    System.out.println("\u001B[34m4. Istirahat singkat selama 5 menit.\u001B[0m");
    System.out.println("\u001B[34m5. Setelah empat Pomodoro, ambil istirahat lebih lama selama 15-30 menit.\u001B[0m");
    System.out.println("=================================================");

    System.out.print("Kembali ke menu utama? (y/n): ");
    String input = scanner.nextLine().trim().toLowerCase();
    if (input.equalsIgnoreCase("y")) {
      new MainMenu().display();
    }
  }
}


