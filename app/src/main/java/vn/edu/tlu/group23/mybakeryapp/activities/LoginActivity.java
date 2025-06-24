package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.EmployeeDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Employee;

public class LoginActivity extends AppCompatActivity {
    EditText editUsername, editPassword;
    Button btnLogin;
    EmployeeDAO employeeDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Kiểm tra nếu đã đăng nhập trước đó
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String savedUserId = prefs.getString("user_id", null);
        String savedRole = prefs.getString("user_role", null);

        if (savedUserId != null && savedRole != null) {
            redirectToMain(savedRole);
            return;
        }

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        employeeDAO = new EmployeeDAO(this);



//        btnLogin.setOnClickListener(v -> {
//            String user = editUsername.getText().toString().trim();
//            String pass = editPassword.getText().toString().trim();
//
//            if (user.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
//            } else {
//                Employee emp = employeeDAO.getEmployeeByLogin(user, pass);
//                if (emp != null) {
//                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
//
//                    // Kiểm tra role và chuyển màn phù hợp
//                    switch (emp.getRole()) {
//                        case "admin":
//                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                            break;
//                        case "staff":
//                            startActivity(new Intent(LoginActivity.this, EmployeeTasksActivity.class));
//                            break;
//                        default:
//                            Toast.makeText(this, "Role không hợp lệ: " + emp.getRole(), Toast.LENGTH_SHORT).show();
//                            return;
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Tên người dùng hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        btnLogin.setOnClickListener(v -> {
            String user = editUsername.getText().toString().trim();
            String pass = editPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Employee emp = employeeDAO.getEmployeeByLogin(user, pass);
                if (emp != null) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Lưu user ID và role
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_id", emp.getMaNV());
                    editor.putString("user_role", emp.getRole());
                    editor.putString("tenNV", emp.getTenNV());
                    editor.putString("chucVu", emp.getChucVu());
                    editor.apply();

                    redirectToMain(emp.getRole());
                } else {
                    Toast.makeText(LoginActivity.this, "Tên người dùng hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void redirectToMain(String role) {
        Intent intent;
        switch (role) {
            case "admin":
                intent = new Intent(this, HomeActivity.class);
                break;
            case "staff":
                intent = new Intent(this, EmployeeTasksActivity.class);
                break;
            default:
                Toast.makeText(this, "Role không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }
}