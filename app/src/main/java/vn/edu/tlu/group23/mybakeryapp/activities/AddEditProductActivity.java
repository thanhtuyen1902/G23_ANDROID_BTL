package vn.edu.tlu.group23.mybakeryapp.activities;

import android.net.Uri; // Thêm import này
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter; // Thêm import này
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton; // Thêm import này
import android.widget.RadioGroup; // Thêm import này
import android.widget.Spinner; // Thêm import này
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher; // Thêm import này
import androidx.activity.result.contract.ActivityResultContracts; // Thêm import này
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays; // Có thể không cần, nhưng để cho chắc
import java.util.List; // Có thể không cần, nhưng để cho chắc

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.ProductDAO; // Chú ý: nếu bạn dùng ProductDAO, hãy sửa lại thành ProductDao
import vn.edu.tlu.group23.mybakeryapp.models.Product;

public class AddEditProductActivity extends AppCompatActivity {

    private ImageView imgProduct; // Đã sửa tên biến
    private Button btnSelectImage; // Nút chọn ảnh mới
    private EditText edtMaSP, edtTenSP, edtGiaBan, edtMoTaSP, edtSoLuongDaBan; // Đã sửa tên biến
    private Spinner spinnerLoaiBanh; // Biến mới
    private RadioGroup rgHinhThucDatBanh; // Biến mới
    private RadioButton rbDatTrucTiep, rbDatOnline; // Biến mới
    private Button btnSaveProduct;
    private ImageView btnBack;
    // Các biến cũ đã bị loại bỏ: edtImageName, ivProductImagePreview, btnPreviewImage, edtLoaiBanh, edtMoTa, edtHinhThucBan, tvTitle

    private ProductDAO productDao; // Chú ý: ProductDao (lowercase 'd')
    private String currentProductCode = null;
    private Uri selectedImageUri; // Để lưu Uri của ảnh đã chọn

    // Launcher để chọn ảnh từ thư viện
    private ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imgProduct.setImageURI(uri);
                } else {
                    Toast.makeText(this, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show();
                    selectedImageUri = null;
                    imgProduct.setImageResource(R.drawable.ic_image);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ View (Đoạn này đã được sửa lại hoàn toàn để khớp với XML mới)
        imgProduct = findViewById(R.id.imgProduct);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        edtMaSP = findViewById(R.id.edtMaSP);
        edtTenSP = findViewById(R.id.edtTenSP);
        edtGiaBan = findViewById(R.id.edtGiaBan);
        edtMoTaSP = findViewById(R.id.edtMoTaSP);
        edtSoLuongDaBan = findViewById(R.id.edtSoLuongDaBan);
        spinnerLoaiBanh = findViewById(R.id.spinnerLoaiBanh);
        rgHinhThucDatBanh = findViewById(R.id.rgHinhThucDatBanh);
        rbDatTrucTiep = findViewById(R.id.rbDatTrucTiep);
        rbDatOnline = findViewById(R.id.rbDatOnline);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnBack = findViewById(R.id.btnBack);

        productDao = new ProductDAO(this); // Chú ý: ProductDao (lowercase 'd')

        // Khởi tạo Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.cake_types_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiBanh.setAdapter(adapter);

        // Kiểm tra xem là chế độ thêm hay sửa
        if (getIntent().hasExtra("PRODUCT_CODE")) {
            currentProductCode = getIntent().getStringExtra("PRODUCT_CODE");
            // Tiêu đề của bạn hiện đang hardcode trong XML, không thể thay đổi dynamic.
            // Nếu muốn thay đổi dynamic, bạn cần thêm android:id="@+id/tvTitle" vào TextView tiêu đề trong XML.
            loadProductData(currentProductCode);
            edtMaSP.setEnabled(false);
        } else {
            // Tiêu đề của bạn hiện đang hardcode trong XML.
            edtMaSP.setEnabled(true);
        }

        // Xử lý sự kiện nút "Chọn Ảnh" (thay thế nút "Xem trước ảnh" cũ)
        btnSelectImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnSaveProduct.setOnClickListener(v -> saveProduct());
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void loadProductData(String maSP) {
        Product product = productDao.getProductByMaSP(maSP);
        if (product != null) {
            String hinhAnhStr = product.getHinhAnh();
            if (hinhAnhStr != null && !hinhAnhStr.isEmpty()) {
                try {
                    selectedImageUri = Uri.parse(hinhAnhStr);
                    imgProduct.setImageURI(selectedImageUri);
                } catch (Exception e) {
                    int imageResId = getResources().getIdentifier(
                            hinhAnhStr, "drawable", getPackageName());
                    if (imageResId != 0) {
                        imgProduct.setImageResource(imageResId);
                    } else {
                        imgProduct.setImageResource(R.drawable.ic_image);
                    }
                }
            } else {
                imgProduct.setImageResource(R.drawable.ic_image);
            }

            edtMaSP.setText(product.getMaSP());
            edtTenSP.setText(product.getTenSP());
            edtGiaBan.setText(String.valueOf(product.getGiaBan()));
            edtMoTaSP.setText(product.getMoTa());
            edtSoLuongDaBan.setText(String.valueOf(product.getSoLuongDaBan()));

            String loaiBanh = product.getLoaiBanh();
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerLoaiBanh.getAdapter();
            if (loaiBanh != null && adapter != null) {
                int spinnerPosition = adapter.getPosition(loaiBanh);
                if (spinnerPosition >= 0) {
                    spinnerLoaiBanh.setSelection(spinnerPosition);
                }
            }

            String hinhThucBan = product.getHinhThucBan();
            if ("Đặt trực tiếp tại cửa hàng".equals(hinhThucBan)) {
                rbDatTrucTiep.setChecked(true);
            } else if ("Đặt Online".equals(hinhThucBan)) {
                rbDatOnline.setChecked(true);
            }

        } else {
            Toast.makeText(this, "Không tìm thấy sản phẩm để chỉnh sửa.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveProduct() {
        String hinhAnh = selectedImageUri != null ? selectedImageUri.toString() : null;

        String maSP = edtMaSP.getText().toString().trim();
        String tenSP = edtTenSP.getText().toString().trim();
        String giaBanStr = edtGiaBan.getText().toString().trim();
        String moTa = edtMoTaSP.getText().toString().trim();
        String soLuongDaBanStr = edtSoLuongDaBan.getText().toString().trim();

        String loaiBanh = spinnerLoaiBanh.getSelectedItem().toString();

        String hinhThucBan = "";
        int selectedRadioButtonId = rgHinhThucDatBanh.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            hinhThucBan = selectedRadioButton.getText().toString();
        }

        if (TextUtils.isEmpty(maSP) || TextUtils.isEmpty(tenSP) || TextUtils.isEmpty(giaBanStr)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Mã SP, Tên SP và Giá bán.", Toast.LENGTH_SHORT).show();
            return;
        }

        int giaBan = 0, soLuongDaBan = 0;
        try {
            giaBan = Integer.parseInt(giaBanStr);
            soLuongDaBan = Integer.parseInt(soLuongDaBanStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá bán và Số lượng đã bán phải là số hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(hinhAnh, maSP, tenSP, giaBan, loaiBanh,
                moTa, soLuongDaBan, hinhThucBan);

        boolean success;
        if (currentProductCode == null) {
            if (productDao.getProductByMaSP(maSP) != null) {
                Toast.makeText(this, "Mã sản phẩm này đã tồn tại. Vui lòng nhập mã khác.", Toast.LENGTH_SHORT).show();
                return;
            }
            success = productDao.addProduct(product);
            if (success) {
                Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thêm sản phẩm thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        } else {
            success = productDao.updateProduct(product) > 0;
            if (success) {
                Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cập nhật sản phẩm thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        }

        if (success) {
            finish();
        }
    }
}