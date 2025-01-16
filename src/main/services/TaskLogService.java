package main.services;

import main.models.TaskLog;
import java.util.ArrayList;
import java.util.List;

public class TaskLogService {
    private static TaskLogService instance;
    private final List<TaskLog> taskLogs;
    private final FileStorageService fileStorageService;

    private TaskLogService() {
        fileStorageService = new FileStorageService();
        taskLogs = fileStorageService.loadTaskLogs();
    }

    public static TaskLogService getInstance() {
        if (instance == null) {
            instance = new TaskLogService();
        }
        return instance;
    }

    public void addTaskLog(TaskLog taskLog) {
        if (taskLog != null) {
            taskLogs.add(taskLog);
            fileStorageService.saveTaskLogs(taskLogs);
        }
    }

    public List<TaskLog> getAllTaskLogs() {
        return new ArrayList<>(taskLogs);
    }

    public TaskLog findTaskLogById(String taskId) {
        return taskLogs.stream()
                .filter(log -> log.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
    }

    public boolean removeTaskLogById(String taskId) {
        boolean removed = taskLogs.removeIf(log -> log.getTaskId().equals(taskId));
        if (removed) {
            fileStorageService.saveTaskLogs(taskLogs);
        }
        return removed;
    }

    public void addFocusTimeById(String taskId, int minutes) {
        TaskLog log = findTaskLogById(taskId);
        if (log != null) {
            log.addFocusTime(minutes);
            fileStorageService.saveTaskLogs(taskLogs);
        }
    }

    public void addBreakTimeById(String taskId, int minutes) {
        TaskLog log = findTaskLogById(taskId);
        if (log != null) {
            log.addBreakTime(minutes);
            fileStorageService.saveTaskLogs(taskLogs);
        }
    }
}