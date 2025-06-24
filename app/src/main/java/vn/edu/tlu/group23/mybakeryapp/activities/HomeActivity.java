package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button; // Vẫn giữ nếu bạn có các nút khác
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView; // Thêm import này
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tlu.group23.mybakeryapp.R;

public class HomeActivity extends AppCompatActivity {

    private ImageView ivLogout;
    private CardView cardManageProducts; // Thay đổi từ Button sang CardView
    private CardView cardManageEmployees; // Thay đổi từ Button sang CardView
    private CardView cardManageTasks;     // Thay đổi từ Button sang CardView
    private CardView cardStatistics;      // Thay đổi từ Button sang CardView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ View (sử dụng ID của CardView)
        ivLogout = findViewById(R.id.ivLogout);
        cardManageProducts = findViewById(R.id.cardManageProducts); // Ánh xạ CardView
        cardManageEmployees = findViewById(R.id.cardManageEmployees); // Ánh xạ CardView
        cardManageTasks = findViewById(R.id.cardManageTasks);         // Ánh xạ CardView
        cardStatistics = findViewById(R.id.cardStatistics);          // Ánh xạ CardView

        // Xử lý sự kiện click cho các CardView
        cardManageProducts.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        cardManageEmployees.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Chức năng Quản lý Nhân viên!", Toast.LENGTH_SHORT).show();
            // TODO: Chuyển đến EmployeeManagementActivity
        });

        cardManageTasks.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Chức năng Quản lý Công việc !", Toast.LENGTH_SHORT).show();
            // TODO: Chuyển đến TaskManagementActivity
            Intent intent = new Intent(HomeActivity.this, TaskActivity.class);
            startActivity(intent);
        });

        cardStatistics.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Chức năng Thống kê & Báo cáo sẽ được phát triển sau!", Toast.LENGTH_SHORT).show();
            // TODO: Chuyển đến StatisticsActivity
        });

        ivLogout.setOnClickListener(v -> {
            // Xoá dữ liệu đăng nhập
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(HomeActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
//            finish();
            // Nếu có LoginActivity:
            // Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // startActivity(intent);
            // Quay về màn đăng nhập và xoá backstack
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}