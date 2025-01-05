  package main.menu;

  import main.services.TaskService;
  import java.util.Scanner;

  public class AddTaskMenu {
    private Scanner scanner = new Scanner(System.in);
    private TaskService taskService = new TaskService();

    public void display() {
      System.out.println("=================================");
      System.out.println("          TAMBAHKAN TUGAS");
      System.out.println("=================================");
      System.out.print("Masukkan nama tugas baru: ");
      String taskName = scanner.nextLine();
      taskService.addTask(taskName);
      System.out.println("Tugas berhasil ditambahkan!");
    }
  }