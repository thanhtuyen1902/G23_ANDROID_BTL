package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.Intent;
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

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        employeeDAO = new EmployeeDAO(this);

        //bắt sự kiện khi click button login
//        btnLogin.setOnClickListener(v -> {
//            String user = editUsername.getText().toString().trim();
//            String pass = editPassword.getText().toString().trim();
//
//            if (user.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
//            } else {
//                if (employeeDAO.checkLogin(user, pass)) {
//                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
//                    //Chuyển đến màn home sau khi login
//                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                    finish();
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

                    // Kiểm tra role và chuyển màn phù hợp
                    switch (emp.getRole()) {
                        case "admin":
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            break;
                        case "staff":
                            startActivity(new Intent(LoginActivity.this, EmployeeTasksActivity.class));
                            break;
                        default:
                            Toast.makeText(this, "Role không hợp lệ: " + emp.getRole(), Toast.LENGTH_SHORT).show();
                            return;
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tên người dùng hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}