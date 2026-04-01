package kh.edu.rupp.to_dolistapp.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kh.edu.rupp.to_dolistapp.database.AppDatabase;
import kh.edu.rupp.to_dolistapp.database.TaskDao;
import kh.edu.rupp.to_dolistapp.models.Task;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private ExecutorService executorService;

    public TaskRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Get all tasks
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    // Save task
    public void insertTask(Task task) {
        executorService.execute(() -> taskDao.insertTask(task));
    }

    // Update task
    public void updateTask(Task task) {
        executorService.execute(() -> taskDao.updateTask(task));
    }

    // Delete task
    public void deleteTask(Task task) {
        executorService.execute(() -> taskDao.deleteTask(task));
    }
}