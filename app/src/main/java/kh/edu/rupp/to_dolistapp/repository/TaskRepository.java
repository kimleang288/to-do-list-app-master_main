package kh.edu.rupp.to_dolistapp.repository;

import android.content.Context;
import android.util.Log;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import kh.edu.rupp.to_dolistapp.database.AppDatabase;
import kh.edu.rupp.to_dolistapp.database.TaskDao;
import kh.edu.rupp.to_dolistapp.models.Task;
import kh.edu.rupp.to_dolistapp.models.TaskResponse;
import kh.edu.rupp.to_dolistapp.services.TaskService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskRepository {

    private static final String TAG = "TaskRepository";
    private TaskDao taskDao;

    public TaskRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        taskDao = database.taskDao();
    }

    // --- Local DB operations ---

    public Flowable<List<Task>> getAllTasks() {
        return taskDao.getAllTasks(); // ← returns Flowable
    }

    public Completable insertTask(Task task) {
        return taskDao.insertTask(task); // ← returns Completable
    }

    public Completable updateTask(Task task) {
        return taskDao.updateTask(task); // ← returns Completable
    }

    public Completable deleteTask(Task task) {
        return taskDao.deleteTask(task); // ← returns Completable
    }

    public Completable markAsCompleted(Task task) {
        task.setProgress(100);
        return taskDao.updateTask(task); // ← returns Completable
    }

    // --- Validation ---

    public String validate(String name, String dueDate) {
        if (name == null || name.isEmpty()) return "Task name is required";
        if (dueDate == null || dueDate.isEmpty()) return "Due date is required";
        return null;
    }

    // --- Remote API operations ---

    public void fetchRemoteTasks(OnRemoteTasksFetchedListener listener) {
        Retrofit httpsClient = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TaskService service = httpsClient.create(TaskService.class);
        Call<TaskResponse> call = service.getTasks();

        call.enqueue(new retrofit2.Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, retrofit2.Response<TaskResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Error: " + response.message());
                    listener.onError("Failed to load tasks");
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.e(TAG, "API Error: " + t.getMessage());
                listener.onError("Network Error!");
            }
        });
    }

    public interface OnRemoteTasksFetchedListener {
        void onSuccess(TaskResponse taskResponse);
        void onError(String message);
    }
}