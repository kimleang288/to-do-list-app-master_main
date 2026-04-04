package kh.edu.rupp.to_dolistapp.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.Calendar;
import kh.edu.rupp.to_dolistapp.databinding.AcitvityAddBinding;
import kh.edu.rupp.to_dolistapp.viewmodel.TaskViewModel;

// No longer implements TaskPresenter.TaskView
public class AddActivity extends AppCompatActivity {

    private AcitvityAddBinding binding; // Data Binding (matches acitvity_add.xml)
    private TaskViewModel viewModel;    // replaces TaskPresenter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate using Data Binding (replaces: setContentView(R.layout.acitvity_add))
        binding = AcitvityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get ViewModel (replaces: new TaskPresenter(this, this))
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Back button using binding (replaces: findViewById(R.id.backHomeBtn))
        binding.backHomeBtn.setOnClickListener(v -> finish());

        // Date picker using binding (replaces: findViewById(R.id.etDueDate))
        binding.etDueDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, day) -> {
                        String selectedDate = day + "/" + (month + 1) + "/" + year;
                        binding.etDueDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Save button using binding (replaces: findViewById(R.id.btnSaveTask))
        binding.btnSaveTask.setOnClickListener(v -> {
            String name        = binding.etTaskName.getText().toString().trim();
            String description = binding.etDescription.getText().toString().trim();
            String dueDate     = binding.etDueDate.getText().toString().trim();

            // Calls ViewModel (replaces: presenter.insertTask(...))
            viewModel.insertTask(name, description, dueDate);
        });

        // Observe task saved (replaces: onTaskSaved() callback)
        viewModel.getTaskSavedLiveData().observe(this, saved -> {
            if (saved != null && saved) {
                startActivity(new Intent(this, TasksActivity.class));
                finish();
            }
        });

        // Observe errors (replaces: onError() callback)
        viewModel.getErrorLiveData().observe(this, message -> {
            if (message != null) {
                if (message.contains("name")) binding.etTaskName.setError(message);
                else binding.etDueDate.setError(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Prevent memory leaks
    }
}