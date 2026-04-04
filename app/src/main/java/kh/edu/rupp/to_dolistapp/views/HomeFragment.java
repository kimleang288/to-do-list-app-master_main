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
import java.util.List;
import kh.edu.rupp.to_dolistapp.adapters.TaskAdapter;
import kh.edu.rupp.to_dolistapp.adapters.TaskGroupAdapter;
import kh.edu.rupp.to_dolistapp.databinding.FragmentHomeBinding;
import kh.edu.rupp.to_dolistapp.models.Task;
import kh.edu.rupp.to_dolistapp.models.TaskGroup;
import kh.edu.rupp.to_dolistapp.viewmodel.TaskViewModel;

public class HomeFragment extends Fragment {

    private TaskAdapter taskAdapter;
    private TaskGroupAdapter taskGroupAdapter;
    private List<Task> inProgressTasks = new ArrayList<>();
    private List<TaskGroup> taskGroups = new ArrayList<>();
    private FragmentHomeBinding binding; // ✅ Data Binding
    private TaskViewModel viewModel;     // ✅ replaces TaskPresenter

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ✅ Inflate using Data Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ✅ Get ViewModel (replaces: new TaskPresenter(context, this))
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // ✅ Setup RecyclerViews using binding (replaces: view.findViewById(...))
        taskAdapter = new TaskAdapter(inProgressTasks);
        binding.recyclerViewProgress.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewProgress.setAdapter(taskAdapter);

        taskGroupAdapter = new TaskGroupAdapter(taskGroups);
        binding.recyclerViewTaskGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewTaskGroups.setAdapter(taskGroupAdapter);

        // ✅ Observe tasks LiveData (replaces: onTasksLoaded() callback)
        viewModel.getTasksLiveData().observe(getViewLifecycleOwner(), tasks -> {
            inProgressTasks.clear();
            inProgressTasks.addAll(tasks);
            taskAdapter.notifyDataSetChanged();
        });

        // ✅ Observe task groups LiveData (replaces: onTaskGroupsLoaded() callback)
        viewModel.getTaskGroupsLiveData().observe(getViewLifecycleOwner(), groups -> {
            taskGroups.clear();
            taskGroups.addAll(groups);
            taskGroupAdapter.notifyDataSetChanged();
        });

        // ✅ Observe errors (replaces: onError() callback)
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        // ✅ Fetch remote tasks (replaces: presenter.fetchRemoteTasks())
        viewModel.fetchRemoteTasks();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // ✅ Prevent memory leaks
    }
}