package main.services;

import main.models.TaskLog;
import java.util.ArrayList;
import java.util.List;

public class TaskLogService {
    private static TaskLogService instance;
    private List<TaskLog> taskLogs;

    private TaskLogService() {
        this.taskLogs = new ArrayList<>();
    }

    public static TaskLogService getInstance() {
        if (instance == null) {
            instance = new TaskLogService();
        }
        return instance;
    }

    // Menambahkan log aktivitas baru
    public void addTaskLog(TaskLog taskLog) {
        if (taskLog != null) {
            taskLogs.add(taskLog);
        }
    }

    // Mendapatkan semua log aktivitas
    public List<TaskLog> getAllTaskLogs() {
        return new ArrayList<>(taskLogs);
    }

    // Mencari log berdasarkan ID tugas
    public TaskLog findTaskLogById(String taskId) {
        for (TaskLog log : taskLogs) {
            if (log.getTaskId().equals(taskId)) {
                return log;
            }
        }
        return null;
    }

    // Menghapus log berdasarkan ID tugas
    public boolean removeTaskLogById(String taskId) {
        return taskLogs.removeIf(log -> log.getTaskId().equals(taskId));
    }

    // Menambahkan waktu fokus pada log berdasarkan ID tugas
    public void addFocusTimeById(String taskId, int minutes) {
        TaskLog log = findTaskLogById(taskId);
        if (log != null) {
            log.addFocusTime(minutes);
        }
    }

    // Menambahkan waktu istirahat pada log berdasarkan ID tugas
    public void addBreakTimeById(String taskId, int minutes) {
        TaskLog log = findTaskLogById(taskId);
        if (log != null) {
            log.addBreakTime(minutes);
        }
    }
}

