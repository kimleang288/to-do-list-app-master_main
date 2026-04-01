package kh.edu.rupp.to_dolistapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import kh.edu.rupp.to_dolistapp.models.Task;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks")
    Flowable<List<Task>> getAllTasks(); // ← LiveData replaced with Flowable

    @Insert
    Completable insertTask(Task task); // ← void replaced with Completable

    @Update
    Completable updateTask(Task task); // ← void replaced with Completable

    @Delete
    Completable deleteTask(Task task); // ← void replaced with Completable
}