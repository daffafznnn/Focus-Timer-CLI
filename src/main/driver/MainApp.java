/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.driver;

import main.menu.MainMenu;

/**
 *
 * @author daffafauzan
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainMenu menuHandler = new MainMenu();
        menuHandler.display();
    }
    
}

