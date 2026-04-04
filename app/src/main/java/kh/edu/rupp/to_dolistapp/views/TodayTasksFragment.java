package kh.edu.rupp.to_dolistapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import kh.edu.rupp.to_dolistapp.adapters.TodayTaskAdapter;
import kh.edu.rupp.to_dolistapp.databinding.FragmentTodayTasksBinding;
import kh.edu.rupp.to_dolistapp.viewmodel.TaskViewModel;

public class TodayTasksFragment extends Fragment {

    private TaskViewModel viewModel;
    private TodayTaskAdapter taskAdapter;
    private FragmentTodayTasksBinding binding; // replaces RecyclerView field

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate using Data Binding (replaces: inflater.inflate(...))
        binding = FragmentTodayTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get ViewModel
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Setup RecyclerView using binding (replaces: view.findViewById(...))
        taskAdapter = new TodayTaskAdapter(new ArrayList<>());
        binding.recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewTasks.setAdapter(taskAdapter);

        // Observe tasks LiveData
        viewModel.getTasksLiveData().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter = new TodayTaskAdapter(tasks);
            binding.recyclerViewTasks.setAdapter(taskAdapter);
        });

        //  Observe errors
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        // Load tasks
        viewModel.loadTasks();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}