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

        public void updateTaskLog(String taskName, int totalFocusTime, int totalBreakTime) {
            TaskLog log = taskLogs.stream()
                    .filter(l -> l.getTaskName().equals(taskName))
                    .findFirst()
                    .orElse(null);

            if (log != null) {
                if (totalFocusTime > 0) {
                    log.addFocusTime(totalFocusTime);
                }
                if (totalBreakTime > 0) {
                    log.addBreakTime(totalBreakTime);
                }
                fileStorageService.saveTaskLogs(taskLogs);
            } else {
                // Jika log tidak ditemukan, buat log baru
                TaskLog newLog = new TaskLog(taskName, taskName); // Gunakan taskName sebagai taskId untuk sementara
                newLog.addFocusTime(totalFocusTime);
                newLog.addBreakTime(totalBreakTime);
                addTaskLog(newLog);
            }
        }
    }

