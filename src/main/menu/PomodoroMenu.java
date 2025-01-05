package main.menu;

import main.services.TimerService;

public class PomodoroMenu {
  private TimerService timerService = new TimerService();

  public void display() {
    System.out.println("=================================");
    System.out.println("         TEKNIK POMODORO");
    System.out.println("=================================");
    timerService.startPomodoro();
  }
}