package kh.edu.rupp.to_dolistapp;

import android.content.Intent; // Needed to open a new screen
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView; // Import this

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize the Bottom Navigation View
        BottomNavigationView navView = findViewById(R.id.nav_view); // Ensure this ID matches your activity_main.xml

        // 2. Set the click listener for the menu items
        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_add) {
                // Open the AddActivity layout
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                return false; // Return false so the "Add" tab doesn't stay highlighted
            }

            if (id == R.id.navigation_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
                return true;
            }

            // Add logic for Dashboard/Notifications fragments here if you have them
            return false;
        });

        // Default fragment loading logic
        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
}