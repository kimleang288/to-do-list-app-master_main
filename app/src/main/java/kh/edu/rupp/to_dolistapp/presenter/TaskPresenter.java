package kh.edu.rupp.to_dolistapp.presenter;

import android.content.Context;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kh.edu.rupp.to_dolistapp.models.Task;
import kh.edu.rupp.to_dolistapp.models.TaskGroup;
import kh.edu.rupp.to_dolistapp.models.TaskResponse;
import kh.edu.rupp.to_dolistapp.repository.TaskRepository;

public class TaskPresenter {

    public interface TaskView {
        void onTasksLoaded(List<Task> tasks);
        void onTaskGroupsLoaded(List<TaskGroup> taskGroups);
        void onTaskSaved();
        void onTaskDeleted();
        void onError(String message);
    }

    private TaskRepository repository;
    private TaskView view;
    private CompositeDisposable disposable = new CompositeDisposable();

    public TaskPresenter(Context context, TaskView view) {
        this.repository = new TaskRepository(context);
        this.view = view;
    }

    // --- Load all tasks ---
    public void loadTasks() {
        disposable.add(
                repository.getAllTasks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                tasks -> view.onTasksLoaded(tasks),
                                e -> view.onError(e.getMessage())
                        )
        );
    }

    // --- Insert task ---
    public void insertTask(String name, String description, String dueDate) {
        String validationError = repository.validate(name, dueDate);
        if (validationError != null) {
            view.onError(validationError);
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
                                () -> view.onTaskSaved(),
                                e -> view.onError(e.getMessage())
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
                                () -> view.onTaskDeleted(),
                                e -> view.onError(e.getMessage())
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
                                () -> view.onTaskSaved(),
                                e -> view.onError(e.getMessage())
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
                                () -> view.onTaskSaved(),
                                e -> view.onError(e.getMessage())
                        )
        );
    }

    // --- Remote API ---
    public void fetchRemoteTasks() {
        repository.fetchRemoteTasks(new TaskRepository.OnRemoteTasksFetchedListener() {
            @Override
            public void onSuccess(TaskResponse taskResponse) {
                if (taskResponse.getInProgress() != null) {
                    view.onTasksLoaded(taskResponse.getInProgress());
                }
                if (taskResponse.getTaskGroups() != null) {
                    view.onTaskGroupsLoaded(taskResponse.getTaskGroups());
                }
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    // --- Dispose ---
    public void dispose() {
        disposable.clear();
    }
}