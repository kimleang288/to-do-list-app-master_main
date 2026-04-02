package kh.edu.rupp.to_dolistapp.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.List;
import kh.edu.rupp.to_dolistapp.R;
import kh.edu.rupp.to_dolistapp.models.Task;
import kh.edu.rupp.to_dolistapp.models.TaskGroup;
import kh.edu.rupp.to_dolistapp.presenter.TaskPresenter;

// ✅ implements TaskPresenter.TaskView — this is MVP
public class AddActivity extends AppCompatActivity implements TaskPresenter.TaskView {

    private TextInputEditText etTaskName, etDescription, etDueDate;
    private MaterialButton btnSaveTask;
    private ImageButton backHomeBtn;
    private TaskPresenter presenter; // ← renamed to presenter

    @Override
    public void onTaskGroupsLoaded(List<TaskGroup> groups) { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_add);

        presenter = new TaskPresenter(this, this); // ← pass itself as View

        etTaskName    = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etDueDate     = findViewById(R.id.etDueDate);
        btnSaveTask   = findViewById(R.id.btnSaveTask);
        backHomeBtn   = findViewById(R.id.backHomeBtn);

        backHomeBtn.setOnClickListener(v -> finish());

        etDueDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, day) -> {
                        String selectedDate = day + "/" + (month + 1) + "/" + year;
                        etDueDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnSaveTask.setOnClickListener(v -> {
            String name        = etTaskName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String dueDate     = etDueDate.getText().toString().trim();

            presenter.insertTask(name, description, dueDate); // ← no callback needed
        });
    }

    // ✅ Presenter calls this when task is saved
    @Override
    public void onTaskSaved() {
        startActivity(new Intent(this, TasksActivity.class));
        finish();
    }

    // ✅ Not used in AddActivity but must be implemented
    @Override
    public void onTasksLoaded(List<Task> tasks) { }

    @Override
    public void onTaskDeleted() { }

    // ✅ Presenter calls this on validation error
    @Override
    public void onError(String message) {
        if (message.contains("name")) etTaskName.setError(message);
        else etDueDate.setError(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dispose(); // ← prevent memory leaks
    }
}