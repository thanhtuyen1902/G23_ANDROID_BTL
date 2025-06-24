package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.EmployeeDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Employee;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText edtMaNV, edtTen, edtSDT, edtChucVu, edtUserName, edtPassword;
    private Button btnSave;
    private ImageView btnBack;
    private TextView tvEnterEmployeeIdLabel;
    private EmployeeDAO employeeDAO;
    private String mode;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_employee);

        // Khởi tạo các thành phần giao diện
        edtMaNV = findViewById(R.id.edtMaNV);
        edtTen = findViewById(R.id.edtTen);
        edtSDT = findViewById(R.id.edtSDT);
        edtChucVu = findViewById(R.id.edtChucVu);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        tvEnterEmployeeIdLabel = findViewById(R.id.tvEnterEmployeeIdLabel);

        // Khởi tạo DAO
        employeeDAO = new EmployeeDAO(this);

        // Lấy mode từ Intent và đặt tiêu đề
        mode = getIntent().getStringExtra("mode");
        if ("edit".equals(mode)) {
            tvEnterEmployeeIdLabel.setText("Sửa thông tin nhân viên");
            // Điền thông tin nhân viên để chỉnh sửa
            String maNV = getIntent().getStringExtra("maNV");
            String ten = getIntent().getStringExtra("ten");
            String sdt = getIntent().getStringExtra("sdt");
            String chucVu = getIntent().getStringExtra("chucVu");
            String userName = getIntent().getStringExtra("userName");
            String password = getIntent().getStringExtra("password");
            position = getIntent().getIntExtra("position", -1);

            if (maNV != null) {
                edtMaNV.setText(maNV);
                edtTen.setText(ten);
                edtSDT.setText(sdt);
                edtChucVu.setText(chucVu);
                edtUserName.setText(userName);
                edtPassword.setText(password);
            } else {
                Toast.makeText(this, "Dữ liệu nhân viên không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        } else {
            tvEnterEmployeeIdLabel.setText("Thêm nhân viên");
        }

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        // Xử lý nút lưu
        btnSave.setOnClickListener(v -> {
            String maNV = edtMaNV.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            String chucVu = edtChucVu.getText().toString().trim();
            String userName = edtUserName.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (maNV.isEmpty() || ten.isEmpty() || sdt.isEmpty() || chucVu.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Employee employee = new Employee();
            employee.setMaNV(maNV);
            employee.setTenNV(ten);
            employee.setSoDienThoai(sdt);
            employee.setChucVu(chucVu);
            employee.setUserName(userName);
            employee.setPassWord(password);
            employee.setRole("staff");

            Intent resultIntent = new Intent();
            resultIntent.putExtra("mode", mode);

            if ("add".equals(mode)) {
                long result = employeeDAO.insertEmployee(employee);
                if (result != -1) {
                    setResult(RESULT_OK, resultIntent);
                    Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if ("edit".equals(mode)) {
                int result = employeeDAO.updateEmployee(employee);
                if (result > 0) {
                    resultIntent.putExtra("position", position);
                    setResult(RESULT_OK, resultIntent);
                    Toast.makeText(this, "Cập nhật nhân viên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cập nhật nhân viên thất bại", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            finish();
        });

        // Xử lý Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}