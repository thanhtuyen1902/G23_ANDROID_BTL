package vn.edu.tlu.group23.mybakeryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.models.Product;
public class ProductDAO {
    private DatabaseHelper dbHelper;
    public ProductDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertInitialSampleData() {
        if (getAllProducts().isEmpty()) {
            addProduct(new Product("chocolate_cake", "B001", "Bánh kem Chocolate", 80000, "Bánh kem",
                    "Bánh kem socola mềm mịn, phủ lớp kem tươi", 45, "Đặt trước"));
            addProduct(new Product("tiramisu", "B002", "Bánh Tiramisu", 75000, "Bánh lạnh",
                    "Bánh Ý với lớp mascarpone và cà phê đậm đà", 60, "Đặt trước"));
            addProduct(new Product("sukem", "B003", "Bánh su kem nhỏ (10 cái)", 50000, "Bánh ngọt",
                    "Vỏ mỏng, nhân kem vani thơm béo", 100, "Đặt ngay"));
            addProduct(new Product("mousse_dau", "B004", "Bánh Mousse Dâu", 70000, "Bánh lạnh",
                    "Bánh mousse dâu tươi, mềm mịn và mát lạnh", 35, "Đặt trước"));
            addProduct(new Product("trung_muoi", "B005", "Bánh bông lan trứng muối", 65000, "Bánh ngọt",
                    "Bông lan mềm, trứng muối mặn ngọt hài hòa", 80, "Đặt ngay"));
            addProduct(new Product("cupcake", "B006", "Bánh Cupcake Dâu tây", 30000, "Cupcake",
                    "Bánh nhỏ xinh với hương vị dâu tây ngọt ngào", 50, "Đặt ngay"));
            addProduct(new Product("tart", "B007", "Bánh Tart Trái Cây", 40000, "Bánh ngọt",
                    "Đế tart giòn, topping trái cây tươi", 32, "Đặt ngay"));
            addProduct(new Product("crepe_saurieng", "B008", "Bánh Crepe Sầu Riêng", 85000, "Bánh lạnh",
                    "Lớp bánh mỏng cuộn nhân kem sầu riêng béo ngậy", 48, "Đặt trước"));
        }
    }
    public boolean addProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Lấy database để ghi
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_HINHANH, product.getHinhAnh());
        values.put(DatabaseHelper.COL_MASP, product.getMaSP());
        values.put(DatabaseHelper.COL_TENSP, product.getTenSP());
        values.put(DatabaseHelper.COL_GIABAN, product.getGiaBan());
        values.put(DatabaseHelper.COL_LOAIBAHN, product.getLoaiBanh());
        values.put(DatabaseHelper.COL_MOTA, product.getMoTa());
        values.put(DatabaseHelper.COL_SOLUONGDABAN, product.getSoLuongDaBan());
        values.put(DatabaseHelper.COL_HINHTHUCBAN, product.getHinhThucBan());

        long result = db.insert(DatabaseHelper.TABLE_SANPHAM, null, values);
        db.close(); // Đóng kết nối database sau khi dùng
        return result != -1;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SANPHAM;
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Lấy database để đọc
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                productList.add(createProductFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Đóng kết nối database sau khi dùng
        return productList;
    }

    public Product getProductByMaSP(String maSP) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_SANPHAM,
                null,
                DatabaseHelper.COL_MASP + " = ?",
                new String[]{maSP},
                null, null, null
        );

        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = createProductFromCursor(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return product;
    }

    public List<Product> searchProducts(String query) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_SANPHAM +
                " WHERE " + DatabaseHelper.COL_MASP + " LIKE ?" +
                " OR " + DatabaseHelper.COL_TENSP + " LIKE ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%", "%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                productList.add(createProductFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_HINHANH, product.getHinhAnh());
        values.put(DatabaseHelper.COL_TENSP, product.getTenSP());
        values.put(DatabaseHelper.COL_GIABAN, product.getGiaBan());
        values.put(DatabaseHelper.COL_LOAIBAHN, product.getLoaiBanh());
        values.put(DatabaseHelper.COL_MOTA, product.getMoTa());
        values.put(DatabaseHelper.COL_SOLUONGDABAN, product.getSoLuongDaBan());
        values.put(DatabaseHelper.COL_HINHTHUCBAN, product.getHinhThucBan());

        int rowsAffected = db.update(DatabaseHelper.TABLE_SANPHAM, values, DatabaseHelper.COL_MASP + " = ?",
                new String[]{product.getMaSP()});
        db.close();
        return rowsAffected;
    }

    public int deleteProduct(String maSP) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(DatabaseHelper.TABLE_SANPHAM, DatabaseHelper.COL_MASP + " = ?",
                new String[]{maSP});
        db.close();
        return rowsAffected;
    }

    // Phương thức helper để tạo đối tượng Product từ Cursor
    private Product createProductFromCursor(Cursor cursor) {
        int loaiBanhColumnIndex = cursor.getColumnIndex(DatabaseHelper.COL_LOAIBAHN);
        String loaiBanh = (loaiBanhColumnIndex != -1) ? cursor.getString(loaiBanhColumnIndex) : "";

        return new Product(
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HINHANH)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MASP)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TENSP)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GIABAN)),
                loaiBanh,
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MOTA)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SOLUONGDABAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_HINHTHUCBAN))
        );
    }
}
