package vn.edu.tlu.cse.ht1.thuy.myappcake;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView; // Import ImageView
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvSanPham;
    private SanPhamAdapter adapter;
    private BakeryDBHelper dbHelper;
    private EditText edtMaSP;
    private ImageView btnBack; // Khai báo ImageView cho nút back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Đảm bảo layout gốc của activity_main.xml có id là @+id/main
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các View từ layout
        rvSanPham = findViewById(R.id.rvSanPham);
        edtMaSP = findViewById(R.id.edtMaSP);
        Button btnTim = findViewById(R.id.btnTimKiem);
        Button btnThem = findViewById(R.id.btnThemSanPham);
        btnBack = findViewById(R.id.btnBack); // Ánh xạ nút back

        rvSanPham.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new BakeryDBHelper(this);

        loadDanhSach();

        // Thiết lập sự kiện click cho nút Tìm
        btnTim.setOnClickListener(v -> {
            String query = edtMaSP.getText().toString().trim();
            if (!query.isEmpty()) {
                List<SanPham> ketQua = dbHelper.searchSanPham(query);
                if (ketQua.isEmpty()) {
                    Toast.makeText(this, "Không tìm thấy sản phẩm nào với từ khóa: \"" + query + "\"", Toast.LENGTH_SHORT).show();
                }
                adapter = new SanPhamAdapter(this, ketQua);
                rvSanPham.setAdapter(adapter);
            } else {
                loadDanhSach(); // Nếu ô tìm kiếm rỗng, tải lại toàn bộ danh sách
            }
        });

        // Thiết lập sự kiện click cho nút Thêm
        btnThem.setOnClickListener(v ->
                Toast.makeText(this, "Chức năng thêm sản phẩm sẽ được triển khai tại đây!", Toast.LENGTH_SHORT).show()
        );

        // Thiết lập sự kiện click cho nút Back (quay lại màn hình trước, hoặc đóng activity nếu là màn hình đầu)
        btnBack.setOnClickListener(v -> {
            onBackPressed(); // Hàm này sẽ mô phỏng việc nhấn nút back của hệ thống
        });
    }

    private void loadDanhSach() {
        List<SanPham> danhSachSanPham = dbHelper.getAllSanPham();
        adapter = new SanPhamAdapter(this, danhSachSanPham);
        rvSanPham.setAdapter(adapter);
    }
}