package kh.edu.rupp.to_dolistapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import kh.edu.rupp.to_dolistapp.R;
import kh.edu.rupp.to_dolistapp.models.Task;

public class TodayTaskAdapter extends RecyclerView.Adapter<TodayTaskAdapter.TodayTaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();

    public TodayTaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TodayTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_task_view, parent, false); // ✅ your layout
        return new TodayTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayTaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TodayTaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskTitle;
        private TextView dueDate;
        private TextView taskGroup;
        private View taskPriority;

        public TodayTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            dueDate = itemView.findViewById(R.id.dueDate);
            taskGroup = itemView.findViewById(R.id.taskGroup);
            taskPriority = itemView.findViewById(R.id.taskPriority);
        }

        public void bind(Task task) {
            taskTitle.setText(task.getTitle());
            dueDate.setText(task.getDueDate());
            taskGroup.setText(task.getName());

            // ✅ Set priority color dot
            if (task.getColor() != null) {
                taskPriority.setBackgroundColor(
                        android.graphics.Color.parseColor(task.getColor())
                );
            }
        }
    }
}