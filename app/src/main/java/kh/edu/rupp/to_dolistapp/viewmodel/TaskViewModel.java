package kh.edu.rupp.to_dolistapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import kh.edu.rupp.to_dolistapp.models.Task;
import kh.edu.rupp.to_dolistapp.models.TaskGroup;
import kh.edu.rupp.to_dolistapp.models.TaskResponse;
import kh.edu.rupp.to_dolistapp.repository.TaskRepository;

public class TaskViewModel extends AndroidViewModel {

    // --- LiveData (replaces TaskView interface callbacks) ---
    private final MutableLiveData<List<Task>> tasksLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<TaskGroup>> taskGroupsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> taskSavedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> taskDeletedLiveData = new MutableLiveData<>();

    private final TaskRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public TaskViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TaskRepository(application);
    }

    // --- Exposed LiveData Getters (View observes these) ---
    public LiveData<List<Task>> getTasksLiveData() { return tasksLiveData; }
    public LiveData<List<TaskGroup>> getTaskGroupsLiveData() { return taskGroupsLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public LiveData<Boolean> getTaskSavedLiveData() { return taskSavedLiveData; }
    public LiveData<Boolean> getTaskDeletedLiveData() { return taskDeletedLiveData; }

    // --- Load all tasks ---
    public void loadTasks() {
        disposable.add(
                repository.getAllTasks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                tasks -> tasksLiveData.setValue(tasks),
                                e -> errorLiveData.setValue(e.getMessage())
                        )
        );
    }

    // --- Insert task ---
    public void insertTask(String name, String description, String dueDate) {
        String validationError = repository.validate(name, dueDate);
        if (validationError != null) {
            errorLiveData.setValue(validationError);
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
                                () -> taskSavedLiveData.setValue(true),
                                e -> errorLiveData.setValue(e.getMessage())
                        )
        );
    }

    // --- Delete task ---
    public void deleteTask(Task task) {
        disposable.add(
                repository.deleteTask(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> taskDeletedLiveData.setValue(true),
                                e -> errorLiveData.setValue(e.getMessage())
                        )
        );
    }

    // --- Update task ---
    public void updateTask(Task task) {
        disposable.add(
                repository.updateTask(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> taskSavedLiveData.setValue(true),
                                e -> errorLiveData.setValue(e.getMessage())
                        )
        );
    }

    // --- Mark task as completed ---
    public void markAsCompleted(Task task) {
        disposable.add(
                repository.markAsCompleted(task)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> taskSavedLiveData.setValue(true),
                                e -> errorLiveData.setValue(e.getMessage())
                        )
        );
    }

    // --- Remote API ---
    public void fetchRemoteTasks() {
        repository.fetchRemoteTasks(new TaskRepository.OnRemoteTasksFetchedListener() {
            @Override
            public void onSuccess(TaskResponse taskResponse) {
                if (taskResponse.getInProgress() != null) {
                    tasksLiveData.postValue(taskResponse.getInProgress());
                }
                if (taskResponse.getTaskGroups() != null) {
                    taskGroupsLiveData.postValue(taskResponse.getTaskGroups());
                }
            }

            @Override
            public void onError(String message) {
                errorLiveData.postValue(message);
            }
        });
    }

    // --- Auto cleanup when ViewModel is destroyed (replaces dispose()) ---
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}