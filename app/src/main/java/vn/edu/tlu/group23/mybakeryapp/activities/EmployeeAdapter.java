package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.models.Employee;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private final Context context;
    private List<Employee> employeeList;
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onViewClick(Employee employee);
        void onEditClick(Employee employee);
        void onDeleteClick(Employee employee);
    }

    public EmployeeAdapter(Context context, List<Employee> employeeList, OnItemActionListener listener) {
        this.context = context;
        this.employeeList = employeeList;
        this.listener = listener;
    }

    public void setEmployeeList(List<Employee> newEmployeeList) {
        this.employeeList = newEmployeeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);

        holder.tvMNV.setText("MNV: " + employee.getMaNV());
        holder.tvTen.setText("Tên: " + employee.getTenNV());
        holder.tvSDT.setText("SĐT: " + employee.getSoDienThoai());
        holder.tvChucVu.setText("Chức vụ: " + employee.getChucVu());
        holder.tvUserName.setText("UserName: " + employee.getUserName());
        holder.tvPassword.setText("Password: " + employee.getPassWord());


        holder.iconEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(employee);
        });
        holder.iconDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(employee);
        });
    }

    @Override
    public int getItemCount() {
        return employeeList != null ? employeeList.size() : 0;
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvMNV, tvTen, tvSDT, tvChucVu, tvUserName, tvPassword;
        ImageView imgHinhAnh, iconEdit, iconDelete;
        CardView cardView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMNV = itemView.findViewById(R.id.tvMNV);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvChucVu = itemView.findViewById(R.id.tvChucVu);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            imgHinhAnh = itemView.findViewById(R.id.imgHinhAnh);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            iconDelete = itemView.findViewById(R.id.iconDelete);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}