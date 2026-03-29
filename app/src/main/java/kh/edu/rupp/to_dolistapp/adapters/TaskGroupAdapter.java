package kh.edu.rupp.to_dolistapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.to_dolistapp.R;
import kh.edu.rupp.to_dolistapp.models.TaskGroup;

public class TaskGroupAdapter extends RecyclerView.Adapter<TaskGroupAdapter.TaskGroupViewHolder> {
    private List<TaskGroup> taskGroups = new ArrayList<>();

    public TaskGroupAdapter(List<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    @NonNull
    @Override
    public TaskGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_group_view, parent, false);
        return new TaskGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskGroupViewHolder holder, int position) {
        TaskGroup taskGroup = taskGroups.get(position);
        holder.bind(taskGroup);
    }

    @Override
    public int getItemCount() {
        return taskGroups.size();
    }

    public static class TaskGroupViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private TextView taskNumber;
        private TextView taskProgress;
        private ImageView taskIcon;
        private androidx.cardview.widget.CardView iconBackground; // ✅ Add this

        public TaskGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskNumber = itemView.findViewById(R.id.taskNumber);
            taskProgress = itemView.findViewById(R.id.taskProgress);
            taskIcon = itemView.findViewById(R.id.taskIcon);
            iconBackground = itemView.findViewById(R.id.iconBackground); // ✅ Add this
        }

        public void bind(TaskGroup taskGroup) {
            taskName.setText(taskGroup.getName());
            taskNumber.setText(taskGroup.getTasks() + " Tasks");
            taskProgress.setText(taskGroup.getProgress() + "%");

            Picasso.get()
                    .load(taskGroup.getIcon())
                    .placeholder(R.drawable.briefcase)
                    .error(R.drawable.briefcase)
                    .into(taskIcon);

            // ✅ Apply color to icon background only
            iconBackground.setCardBackgroundColor(
                    android.graphics.Color.parseColor(taskGroup.getColor())
            );
        }
    }
}
