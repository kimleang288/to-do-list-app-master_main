package kh.edu.rupp.to_dolistapp.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import kh.edu.rupp.to_dolistapp.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // ✅ Add button → open AddActivity
            if (id == R.id.navigation_add) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                return false;
            }

            // ✅ Home button → open HomeFragment
            if (id == R.id.navigation_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
                return true;
            }

            return false;
        });

        // ✅ Check if coming from AddActivity
        String openFragment = getIntent().getStringExtra("openFragment");
        if (openFragment != null && openFragment.equals("tasks")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new TasksFragment())
                    .commit();
        } else if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
}