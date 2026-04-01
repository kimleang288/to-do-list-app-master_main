package kh.edu.rupp.to_dolistapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import kh.edu.rupp.to_dolistapp.R;
import kh.edu.rupp.to_dolistapp.adapters.TodayTaskAdapter; // ✅ new adapter
import kh.edu.rupp.to_dolistapp.repository.TaskRepository;

public class TodayTasksFragment extends Fragment {

    private TaskRepository taskRepository;
    private TodayTaskAdapter taskAdapter; // ✅ changed
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskRepository = new TaskRepository(requireContext());

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TodayTaskAdapter(new ArrayList<>()); // ✅ changed
        recyclerView.setAdapter(taskAdapter);

        // Observe database
        taskRepository.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter = new TodayTaskAdapter(tasks); // ✅ changed
            recyclerView.setAdapter(taskAdapter);
        });
    }
}
