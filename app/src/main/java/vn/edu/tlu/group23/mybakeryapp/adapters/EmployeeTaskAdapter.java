package vn.edu.tlu.group23.mybakeryapp.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.ShiftDAO;
import vn.edu.tlu.group23.mybakeryapp.database.TaskDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Task;

public class EmployeeTaskAdapter extends RecyclerView.Adapter<EmployeeTaskAdapter.TaskViewHolder>{
    private List<Task> taskList;
    private TaskDAO taskDAO;
    private ShiftDAO shiftDAO;
    private Activity activity;
    public EmployeeTaskAdapter(Activity activity, List<Task> taskList, TaskDAO taskDAO, ShiftDAO shiftDAO) {
        this.activity = activity;
        this.taskList = taskList;
        this.taskDAO = taskDAO;
        this.shiftDAO = shiftDAO;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckbox;
        TextView taskTitle, taskDescription, taskShiftName;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskCheckbox = itemView.findViewById(R.id.taskCheckbox);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskShiftName = itemView.findViewById(R.id.taskShiftName);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());
//        holder.taskShiftName.setText("Ca " + task.getShiftId());
        String shiftName = shiftDAO.getShiftNameById(task.getShiftId());
        holder.taskShiftName.setText(shiftName);

        holder.taskCheckbox.setOnCheckedChangeListener(null); // Tránh lặp listener khi scroll

        boolean isDone = "Hoàn thành".equalsIgnoreCase(task.getStatus());
        holder.taskCheckbox.setChecked(isDone);

        // Cho phép tick để đổi trạng thái
        holder.taskCheckbox.setEnabled(true);

        holder.taskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setStatus(isChecked ? "Hoàn thành" : "Đang thực hiện");
//            taskDAO.updateTask(task);  // Cập nhật trạng thái vào DB
//            notifyItemChanged(holder.getAdapterPosition());
            new Thread(() -> {
                taskDAO.updateTask(task);
                activity.runOnUiThread(() -> {
                    notifyItemChanged(holder.getAdapterPosition());
                });
            }).start();
        });

        holder.taskTitle.setPaintFlags(
                isDone
                        ? holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                        : holder.taskTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
        );
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }
}
