package main.services;

import main.models.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
  private List<Task> tasks = new ArrayList<>();
  private List<String> log = new ArrayList<>();

  public void addTask(String taskName) {
    tasks.add(new Task(taskName));
  }

  public List<String> getTasks() {
    List<String> taskNames = new ArrayList<>();
    for (Task task : tasks) {
      taskNames.add(task.getName());
    }
    return taskNames;
  }

  public void logActivity(String activity) {
    log.add(activity);
  }

  public List<String> getLog() {
    return log;
  }
}
