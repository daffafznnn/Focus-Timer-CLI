package main.driver;

import main.menu.MainMenu;

public class MainApp {

/**
 * Metode utama untuk menjalankan aplikasi.
 * Ini akan menampilkan menu utama aplikasi
 * menggunakan objek MainMenu.
 *
 * @param args Argumen baris perintah
 */

    public static void main(String[] args) {
        MainMenu menuHandler = new MainMenu();
        menuHandler.display();
    }
    
}

