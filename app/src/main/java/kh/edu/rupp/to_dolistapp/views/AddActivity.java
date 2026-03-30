package kh.edu.rupp.to_dolistapp.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import kh.edu.rupp.to_dolistapp.R;
import kh.edu.rupp.to_dolistapp.models.Task;
import kh.edu.rupp.to_dolistapp.repository.TaskRepository; // ✅ add import

public class AddActivity extends AppCompatActivity {

    private TextInputEditText etTaskName;
    private TextInputEditText etDescription;
    private TextInputEditText etDueDate;
    private MaterialButton btnSaveTask;
    private ImageButton backHomeBtn;
    private TaskRepository repository; // ✅ declare here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_add);

        // ✅ Initialize repository
        repository = new TaskRepository(this);

        // Find all views
        etTaskName = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        backHomeBtn = findViewById(R.id.backHomeBtn);

        // Back button
        backHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Date picker
        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(
                        AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                etDueDate.setText(selectedDate);
                            }
                        },
                        year, month, day
                );
                datePicker.show();
            }
        });

        // Save button
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        String name = etTaskName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String dueDate = etDueDate.getText().toString().trim();

        if (name.isEmpty()) {
            etTaskName.setError("Task name is required");
            return;
        }
        if (dueDate.isEmpty()) {
            etDueDate.setError("Due date is required");
            return;
        }

        // Create task object
        Task task = new Task();
        task.setTitle(name);
        task.setName(description);
        task.setDueDate(dueDate);
        task.setProgress(0);
        task.setColor("#6B3FA0");

        // Save to database
        repository.insertTask(task);

        // Go to TasksFragment
        Intent intent = new Intent(this, TasksActivity.class);
        startActivity(intent);
        finish();
    }
}