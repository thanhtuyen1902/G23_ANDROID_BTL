package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView; // Đảm bảo đã import

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.ProductDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Product;
import vn.edu.tlu.group23.mybakeryapp.activities.ProductAdapter;
import com.google.android.material.button.MaterialButton;
public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnItemActionListener {
    private RecyclerView rvProduct;
    private ProductAdapter adapter;
    private ProductDAO productDao;
    private EditText edtMaSP;
    private ImageView btnBack;

   private MaterialButton btnThemSanPham;
    private Button btnTim;
  //  private ImageView iconCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các View
        rvProduct = findViewById(R.id.rvSanPham);
        edtMaSP = findViewById(R.id.edtMaSP);
        btnTim = findViewById(R.id.btnTimKiem);
        btnThemSanPham = findViewById(R.id.btnThemSanPham);
        btnBack = findViewById(R.id.btnBack);


        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        productDao = new ProductDAO(this);

        // Chèn dữ liệu mẫu nếu database trống
        productDao.insertInitialSampleData();

        // Xử lý sự kiện nút Tìm kiếm
        btnTim.setOnClickListener(v -> {
            String query = edtMaSP.getText().toString().trim();
            if (!query.isEmpty()) {
                List<Product> ketQua = productDao.searchProducts(query);
                if (ketQua.isEmpty()) {
                    Toast.makeText(this, "Không tìm thấy sản phẩm nào với từ khóa: \"" + query + "\"", Toast.LENGTH_SHORT).show();
                }
                adapter.setProductList(ketQua);
            } else {
                loadProductList();
            }
        });

        // Xử lý sự kiện nút Thêm sản phẩm (FAB)
        if (btnThemSanPham != null) { // Thêm kiểm tra null an toàn hơn
            btnThemSanPham.setOnClickListener(v -> { // <<< THAY btnAddProduct BẰNG btnThemSanPham
                Intent intent = new Intent(ProductActivity.this, AddEditProductActivity.class);
                startActivity(intent);
            });
        }


        // Xử lý sự kiện nút Back (trên header)
        if (btnBack != null) { // Thêm kiểm tra null an toàn hơn
            btnBack.setOnClickListener(v -> {
                onBackPressed();
            });
        }
        //btnBack.setOnClickListener(v -> {
           // onBackPressed();
       // });

        // Xử lý sự kiện icon giỏ hàng
//        iconCart.setOnClickListener(v -> {
//            Toast.makeText(this, "Chức năng giỏ hàng chưa được triển khai!", Toast.LENGTH_SHORT).show();
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductList();
    }

    private void loadProductList() {
        List<Product> productList = productDao.getAllProducts();
        if (adapter == null) {
            adapter = new ProductAdapter(this, productList, this);
            rvProduct.setAdapter(adapter);
        } else {
            adapter.setProductList(productList);
        }
    }

    // --- Triển khai các phương thức từ ProductAdapter.OnItemActionListener ---

    @Override
    public void onViewClick(Product product) {
        if (product == null) {
            Toast.makeText(this, "Thông tin sản phẩm không khả dụng.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chi tiết sản phẩm");

        TextView detailsTextView = new TextView(this);
        detailsTextView.setPadding(40, 20, 40, 20);

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getGiaBan());

        String details = "Mã SP: " + product.getMaSP() + "\n" +
                "Tên SP: " + product.getTenSP() + "\n" +
                "Giá: " + formattedPrice + " VNĐ\n" +
                "Loại bánh: " + product.getLoaiBanh() + "\n" +
                "Mô tả: " + product.getMoTa() + "\n" +
                "SL đã bán: " + product.getSoLuongDaBan() + "\n" +
                "Hình thức bán: " + product.getHinhThucBan();

        detailsTextView.setText(details);

        ImageView imageView = new ImageView(this);
        int imageSize = (int) (150 * getResources().getDisplayMetrics().density);
        android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(imageSize, imageSize);
        layoutParams.setMargins(40, 0, 40, 20);
        imageView.setLayoutParams(layoutParams);

        // Thay đổi logic tải ảnh từ Glide sang tải từ resource drawable
        if (product.getHinhAnh() != null && !product.getHinhAnh().isEmpty()) {
            int imageResId = getResources().getIdentifier(
                    product.getHinhAnh(), "drawable", getPackageName());
            if (imageResId != 0) {
                imageView.setImageResource(imageResId);
            } else {
                imageView.setImageResource(R.drawable.ic_image);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_image);
        }

        android.widget.LinearLayout dialogLayout = new android.widget.LinearLayout(this);
        dialogLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
        dialogLayout.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        dialogLayout.addView(imageView);
        dialogLayout.addView(detailsTextView);

        builder.setView(dialogLayout);
        builder.setPositiveButton("Đóng", null);
        builder.show();
    }

    @Override
    public void onEditClick(Product product) {
        Intent intent = new Intent(ProductActivity.this, AddEditProductActivity.class);
        intent.putExtra("PRODUCT_CODE", product.getMaSP());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm '" + product.getTenSP() + "' không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int rowsAffected = productDao.deleteProduct(product.getMaSP());
                        if (rowsAffected > 0) {
                            Toast.makeText(ProductActivity.this, "Đã xóa sản phẩm: " + product.getTenSP(), Toast.LENGTH_SHORT).show();
                            loadProductList();
                        } else {
                            Toast.makeText(ProductActivity.this, "Không thể xóa sản phẩm: " + product.getTenSP() + ". Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Không", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}