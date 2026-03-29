package kh.edu.rupp.to_dolistapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private TextInputEditText etTaskName;
    private TextInputEditText etDescription;
    private TextInputEditText etDueDate;
    private MaterialButton btnSaveTask;
    private ImageButton backHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_add);

        // Find all views
        etTaskName = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        backHomeBtn = findViewById(R.id.backHomeBtn);

        // Back button → go back to MainActivity
        backHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Due date field → show date picker
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

        // Validate fields
        if (name.isEmpty()) {
            etTaskName.setError("Task name is required");
            return;
        }
        if (dueDate.isEmpty()) {
            etDueDate.setError("Due date is required");
            return;
        }

        // Show success message
        Toast.makeText(this, "Task '" + name + "' Saved!", Toast.LENGTH_SHORT).show();
        finish(); // go back to MainActivity
    }
}