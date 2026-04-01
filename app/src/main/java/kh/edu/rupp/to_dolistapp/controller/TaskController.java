package kh.edu.rupp.to_dolistapp.controller;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kh.edu.rupp.to_dolistapp.models.Task;
import kh.edu.rupp.to_dolistapp.repository.TaskRepository;

public class TaskController {

    private TaskRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public TaskController(Context context) {
        repository = new TaskRepository(context);
    }

    // --- Callbacks ---

    public interface TaskCallBack {
        void onTaskLoaded(List<Task> tasks);
        void onError(String message);
    }

    public interface SaveCallBack {
        void onSaved();
        void onError(String message);
    }

    // --- Local DB operations ---

    public void loadAllTasks(TaskCallBack callback) {
        disposable.add(
                repository.getAllTasks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                callback::onTaskLoaded,
                                error -> callback.onError(error.getMessage())
                        )
        );
    }

    public void insertTask(String name, String description,
                           String dueDate, SaveCallBack callback) {
        // Validation delegated to Repository
        String error = repository.validate(name, dueDate);
        if (error != null) {
            callback.onError(error);
            return;
        }

        Task task = new Task();
        task.setName(name);
        task.setTitle(name);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setProgress(0);

        disposable.add(
                repository.insertTask(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                callback::onSaved,
                                err -> callback.onError(err.getMessage())
                        )
        );
    }

    public void deleteTask(Task task, SaveCallBack callback) {
        disposable.add(
                repository.deleteTask(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                callback::onSaved,
                                err -> callback.onError(err.getMessage())
                        )
        );
    }

    public void updateTask(Task task, SaveCallBack callback) {
        disposable.add(
                repository.updateTask(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                callback::onSaved,
                                err -> callback.onError(err.getMessage())
                        )
        );
    }

    public void markAsCompleted(Task task, SaveCallBack callback) {
        disposable.add(
                repository.markAsCompleted(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                callback::onSaved,
                                err -> callback.onError(err.getMessage())
                        )
        );
    }

    // --- Remote API operations ---

    public void fetchRemoteTasks(TaskRepository.OnRemoteTasksFetchedListener listener) {
        repository.fetchRemoteTasks(listener); // ← just delegates
    }

    // --- Dispose to prevent memory leaks ---

    public void dispose() {
        disposable.clear();
    }
}