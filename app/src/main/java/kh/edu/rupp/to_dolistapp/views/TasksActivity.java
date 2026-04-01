package kh.edu.rupp.to_dolistapp.views;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import kh.edu.rupp.to_dolistapp.R;
import kh.edu.rupp.to_dolistapp.adapters.TasksPagerAdapter;

public class TasksActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list); // ✅ your layout

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // ✅ Connect ViewPager with adapter
        TasksPagerAdapter pagerAdapter = new TasksPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // ✅ Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Today's Tasks");
                    break;
                case 1:
                    tab.setText("Upcoming");
                    break;
                case 2:
                    tab.setText("Completed");
                    break;
            }
        }).attach();
        // Find back button
        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish()); // ✅ goes back to previous screen
    }

}