package vn.edu.tlu.group23.mybakeryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    private Context context;
    private List<Task> taskList;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }
    public TaskAdapter(Context context, List<Task> taskList, OnTaskClickListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());
        holder.assigneeName.setText(task.getAssignee());
        holder.statusTag.setText(task.getStatus());
        holder.priorityTag.setText(task.getPriority());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateData(List<Task> newTasks) {
        this.taskList = newTasks;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, assigneeName, statusTag, priorityTag;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            assigneeName = itemView.findViewById(R.id.assigneeName);
            statusTag = itemView.findViewById(R.id.statusTag);
            priorityTag = itemView.findViewById(R.id.priorityTag);
        }
    }

}

