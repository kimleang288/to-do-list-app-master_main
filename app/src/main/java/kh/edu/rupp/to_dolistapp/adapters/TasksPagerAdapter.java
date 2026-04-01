package kh.edu.rupp.to_dolistapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import kh.edu.rupp.to_dolistapp.views.CompletedTasksFragment;
import kh.edu.rupp.to_dolistapp.views.TodayTasksFragment;
import kh.edu.rupp.to_dolistapp.views.UpcomingTasksFragment;

public class TasksPagerAdapter extends FragmentStateAdapter {

    // ✅ Constructor for Activity
    public TasksPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    // ✅ Constructor for Fragment
    public TasksPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new TodayTasksFragment();
            case 1: return new UpcomingTasksFragment();
            case 2: return new CompletedTasksFragment();
            default: return new TodayTasksFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}