package kh.edu.rupp.to_dolistapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.List;
import kh.edu.rupp.to_dolistapp.adapters.TaskAdapter;
import kh.edu.rupp.to_dolistapp.adapters.TaskGroupAdapter;
import kh.edu.rupp.to_dolistapp.databinding.FragmentHomeBinding;
import kh.edu.rupp.to_dolistapp.models.Task;
import kh.edu.rupp.to_dolistapp.models.TaskGroup;
import kh.edu.rupp.to_dolistapp.presenter.TaskPresenter;

public class HomeFragment extends Fragment implements TaskPresenter.TaskView {

    private TaskAdapter taskAdapter;
    private TaskGroupAdapter taskGroupAdapter;
    private List<Task> inProgressTasks = new ArrayList<>();
    private List<TaskGroup> taskGroups = new ArrayList<>();
    private FragmentHomeBinding binding;
    private TaskPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new TaskPresenter(requireContext(), this);

        taskAdapter = new TaskAdapter(inProgressTasks);
        binding.recyclerViewProgress.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewProgress.setAdapter(taskAdapter);

        taskGroupAdapter = new TaskGroupAdapter(taskGroups);
        binding.recyclerViewTaskGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewTaskGroups.setAdapter(taskGroupAdapter);

        presenter.fetchRemoteTasks();
    }

    // ✅ Presenter calls this for in progress tasks
    @Override
    public void onTasksLoaded(List<Task> tasks) {
        inProgressTasks.clear();
        inProgressTasks.addAll(tasks);
        taskAdapter.notifyDataSetChanged();
    }

    // ✅ Presenter calls this for task groups
    @Override
    public void onTaskGroupsLoaded(List<TaskGroup> groups) {
        taskGroups.clear();
        taskGroups.addAll(groups);
        taskGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskSaved() { }

    @Override
    public void onTaskDeleted() { }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.dispose();
    }
}