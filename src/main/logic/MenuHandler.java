/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.logic;

import java.util.Scanner;

/**
 *
 * @author daffafauzan
 */
public class MenuHandler {
    public void tampilkanMenuUtama() {
        Scanner scanner = new Scanner(System.in);
        int pilihan = 0;
        do {
            System.out.println("===== Focus Timer =====");
            System.out.println("1. Tambah Tugas");
            System.out.println("2. Lihat Daftar Tugas");
            System.out.println("3. Atur Durasi Timer");
            System.out.println("4. Mulai Timer");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1 -> System.out.println("Tambah tugas");
                case 2 -> System.out.println("Lihat daftar tugas");
                case 3 -> System.out.println("Atur durasi timer");
                case 4 -> System.out.println("Mulai timer");
                case 5 -> System.out.println("Terima kasih telah menggunakan Focus Timer!");
                default -> System.out.println("Pilihan tidak valid, coba lagi.");
            }
        } while (pilihan != 5);
        scanner.close();
    }
}

