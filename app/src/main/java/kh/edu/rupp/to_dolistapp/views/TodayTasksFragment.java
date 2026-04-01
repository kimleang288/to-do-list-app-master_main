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
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import kh.edu.rupp.to_dolistapp.R;
import kh.edu.rupp.to_dolistapp.adapters.TodayTaskAdapter;
import kh.edu.rupp.to_dolistapp.controller.TaskController;
import kh.edu.rupp.to_dolistapp.models.Task;

public class TodayTasksFragment extends Fragment {

    private TaskController taskController;
    private TodayTaskAdapter taskAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskController = new TaskController(requireContext());

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TodayTaskAdapter(new ArrayList<>());
        recyclerView.setAdapter(taskAdapter);

        // RxJava instead of LiveData
        taskController.loadAllTasks(new TaskController.TaskCallBack() {
            @Override
            public void onTaskLoaded(List<Task> tasks) {
                taskAdapter = new TodayTaskAdapter(tasks);
                recyclerView.setAdapter(taskAdapter);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        taskController.dispose(); // ← prevent memory leaks
    }
}