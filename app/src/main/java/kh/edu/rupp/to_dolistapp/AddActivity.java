package kh.edu.rupp.to_dolistapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView; // Added for the back button
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private EditText etProjectName;
    private EditText etDescription;
    private Button btnAddProject;
    private ImageView btnBack; // Added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // CRITICAL: Ensure your XML filename is exactly 'acitvity_add'
        // (Note the spelling 'acitvity' matches your previous prompt)
        setContentView(R.layout.acitvity_add);

        // Initialize views
        etProjectName = findViewById(R.id.projectName);
        etDescription = findViewById(R.id.description);
        btnAddProject = findViewById(R.id.btnAddProject);
        btnBack = findViewById(R.id.btnBack); // Initialize back button

        // Handle Back Button Click
        btnBack.setOnClickListener(v -> {
            finish(); // Closes this activity and goes back to the previous screen
        });

        // Handle Add Project Click
        btnAddProject.setOnClickListener(v -> {
            saveProject();
        });
    }

    private void saveProject() {
        String name = etProjectName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (name.isEmpty()) {
            etProjectName.setError("Project name is required");
            return;
        }

        // For now, we just show a toast.
        // Later, you can send this data back to MainActivity or a Database.
        Toast.makeText(this, "Project '" + name + "' Added!", Toast.LENGTH_SHORT).show();

        finish();
    }
}