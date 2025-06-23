package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.EmployeeDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Employee;

public class EmployeeActivity extends AppCompatActivity {

    private RecyclerView rvNhanVien;
    private EmployeeAdapter employeeAdapter;
    private List<Employee> employeeList;
    private Button btnThemNhanVien;
    private ImageView btnBack;
    private EmployeeDAO employeeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);

        // Khởi tạo các thành phần giao diện
        rvNhanVien = findViewById(R.id.rvNhanVien);
        btnThemNhanVien = findViewById(R.id.btnThemNhanVien);
        btnBack = findViewById(R.id.btnBack);

        // Khởi tạo DAO
        employeeDAO = new EmployeeDAO(this);

        // Lấy danh sách nhân viên từ cơ sở dữ liệu
        employeeList = employeeDAO.getAllEmployees();
        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }

        // Cài đặt Adapter
        employeeAdapter = new EmployeeAdapter(this, employeeList, new EmployeeAdapter.OnItemActionListener() {
            @Override
            public void onViewClick(Employee employee) {
                Toast.makeText(EmployeeActivity.this, "Xem chi tiết: " + employee.getTenNV(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEditClick(Employee employee) {
                // Chức năng sửa: Mở AddEmployeeActivity để chỉnh sửa
                Intent intent = new Intent(EmployeeActivity.this, AddEmployeeActivity.class);
                intent.putExtra("mode", "edit");
                intent.putExtra("maNV", employee.getMaNV());
                intent.putExtra("ten", employee.getTenNV());
                intent.putExtra("sdt", employee.getSoDienThoai());
                intent.putExtra("chucVu", employee.getChucVu());
                intent.putExtra("userName", employee.getUserName());
                intent.putExtra("password", employee.getPassWord());
                intent.putExtra("role", employee.getRole());
                intent.putExtra("position", employeeList.indexOf(employee));
                startActivityForResult(intent, 1);
            }

            @Override
            public void onDeleteClick(Employee employee) {
                // Chức năng xóa: Hiển thị hộp thoại xác nhận
                showDeleteConfirmationDialog(employee);
            }
        });

        rvNhanVien.setLayoutManager(new LinearLayoutManager(this));
        rvNhanVien.setAdapter(employeeAdapter);

        // Chức năng thêm: Xử lý nút thêm nhân viên
        btnThemNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeActivity.this, AddEmployeeActivity.class);
            intent.putExtra("mode", "add");
            startActivityForResult(intent, 1);
        });

        // Xử lý nút back
        btnBack.setOnClickListener(v -> onBackPressed());

        // Xử lý Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String mode = data.getStringExtra("mode");
            if ("add".equals(mode) || "edit".equals(mode)) {
                // Load lại dữ liệu từ SQLite để đồng bộ
                employeeList = employeeDAO.getAllEmployees();
                employeeAdapter.setEmployeeList(employeeList);
            }
        }
    }

    // Phương thức hiển thị hộp thoại xác nhận xóa
    private void showDeleteConfirmationDialog(Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa nhân viên này không?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = employeeList.indexOf(employee);
                if (position != -1) {
                    int result = employeeDAO.deleteEmployee(employee.getMaNV());
                    if (result > 0) {
                        employeeList.remove(position);
                        employeeAdapter.notifyItemRemoved(position);
                        Toast.makeText(EmployeeActivity.this, "Đã xóa nhân viên: " + employee.getTenNV(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EmployeeActivity.this, "Xóa nhân viên thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}