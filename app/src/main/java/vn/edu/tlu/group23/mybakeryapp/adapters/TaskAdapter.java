package vn.edu.tlu.group23.mybakeryapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.activities.DeleteConfirmDialog;
import vn.edu.tlu.group23.mybakeryapp.database.TaskDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    private Context context;
    private List<Task> taskList;
    private OnTaskClickListener listener;
    private TaskDAO taskDAO;
    private Runnable onDeleted;
    private List<Task> fullTaskList;
    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }
    public TaskAdapter(Context context, List<Task> taskList, TaskDAO taskDAO, Runnable onDeleted, OnTaskClickListener listener) {
        this.context = context;
        this.taskList = new ArrayList<>(taskList);
        this.fullTaskList = new ArrayList<>(taskList);
        this.taskDAO = taskDAO;
        this.onDeleted = onDeleted;
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
        // Đổi màu theo trạng thái
        String status = task.getStatus().toLowerCase();
        switch (status.toLowerCase()) {
            case "chưa làm":
                holder.statusTag.setBackgroundResource(R.drawable.tag_status_pending);
                holder.statusTag.setTextColor(Color.DKGRAY);
                break;
            case "đang thực hiện":
                holder.statusTag.setBackgroundResource(R.drawable.tag_status_inprogress);
                holder.statusTag.setTextColor(Color.parseColor("#1E3A8A")); // xanh đậm
                break;
            case "hoàn thành":
                holder.statusTag.setBackgroundResource(R.drawable.tag_status_completed);
                holder.statusTag.setTextColor(Color.parseColor("#065F46")); // xanh đậm
                break;
        }
        holder.priorityTag.setText(task.getPriority());
        String priority = task.getPriority().toLowerCase();
        switch (priority.toLowerCase()) {
            case "cao":
                holder.priorityTag.setBackgroundResource(R.drawable.tag_priority_high);
                holder.priorityTag.setTextColor(Color.parseColor("#C62828"));
                break;
            case "trung bình":
                holder.priorityTag.setBackgroundResource(R.drawable.tag_priority_medium);
                holder.priorityTag.setTextColor(Color.parseColor("#EF6C00"));
                break;
            case "thấp":
                holder.priorityTag.setBackgroundResource(R.drawable.tag_priority_low);
                holder.priorityTag.setTextColor(Color.parseColor("#2E7D32"));
                break;
        }

        //Bắt sự kiện ..
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });

        //Bắt sự kiện xóa
        holder.btnDelete.setOnClickListener(v -> {
            new DeleteConfirmDialog(
                    context,
                    task.getTitle(),
                    () -> taskDAO.deleteTask(task.getId()) > 0,
                    onDeleted
            ).show();
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
        ImageView btnDelete;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            assigneeName = itemView.findViewById(R.id.assigneeName);
            statusTag = itemView.findViewById(R.id.statusTag);
            priorityTag = itemView.findViewById(R.id.priorityTag);
            btnDelete = itemView.findViewById(R.id.deleteIcon);

        }
    }

    // Tìm kiếm theo tiêu đề
    public void filter(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            taskList = new ArrayList<>(fullTaskList);
        } else {
            List<Task> filtered = new ArrayList<>();
            for (Task task : fullTaskList) {
                if (task.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    filtered.add(task);
                }
            }
            taskList = filtered;
        }
        notifyDataSetChanged();
    }
}

