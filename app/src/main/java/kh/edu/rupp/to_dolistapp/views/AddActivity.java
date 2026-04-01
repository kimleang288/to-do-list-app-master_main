package kh.edu.rupp.to_dolistapp.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import kh.edu.rupp.to_dolistapp.R;
import kh.edu.rupp.to_dolistapp.controller.TaskController;

public class AddActivity extends AppCompatActivity {

    private TextInputEditText etTaskName, etDescription, etDueDate;
    private MaterialButton btnSaveTask;
    private ImageButton backHomeBtn;
    private TaskController taskController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_add);

        taskController = new TaskController(this);

        etTaskName    = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etDueDate     = findViewById(R.id.etDueDate);
        btnSaveTask   = findViewById(R.id.btnSaveTask);
        backHomeBtn   = findViewById(R.id.backHomeBtn);

        backHomeBtn.setOnClickListener(v -> finish());

        // Date picker
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

        // Save button using RxJava controller
        btnSaveTask.setOnClickListener(v -> {
            String name        = etTaskName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String dueDate     = etDueDate.getText().toString().trim();

            taskController.insertTask(name, description, dueDate, new TaskController.SaveCallBack() {
                @Override
                public void onSaved() {
                    startActivity(new Intent(AddActivity.this, TasksActivity.class));
                    finish();
                }

                @Override
                public void onError(String message) {
                    if (message.contains("name")) etTaskName.setError(message);
                    else etDueDate.setError(message);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskController.dispose(); // ← prevent memory leaks
    }
}