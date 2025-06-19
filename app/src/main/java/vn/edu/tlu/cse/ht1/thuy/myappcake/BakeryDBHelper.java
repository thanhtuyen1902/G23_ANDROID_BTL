package vn.edu.tlu.cse.ht1.thuy.myappcake;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

public class BakeryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bakery.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng
    public static final String TABLE_SANPHAM = "sanpham";

    // Tên cột
    public static final String COL_HINHANH = "hinhAnh";
    public static final String COL_MASP = "maSP";
    public static final String COL_TENSP = "tenSP";
    public static final String COL_GIABAN = "giaBan";
    public static final String COL_LOAIBAHN = "loaiBanh";
    public static final String COL_MOTA = "moTa";
    public static final String COL_SOLUONGDABAN = "soLuongDaBan";
    public static final String COL_HINHTHUCBAN = "hinhThucBan";

    public BakeryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo bảng sanpham (CÓ hình ảnh)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE sanpham (" +
                "hinhAnh TEXT," +                       // đường dẫn ảnh hoặc tên file ảnh
                "maSP TEXT PRIMARY KEY," +
                "tenSP TEXT," +
                "giaBan INTEGER," +
                "loaiBanh TEXT," +
                "moTa TEXT," +
                "soLuongDaBan INTEGER," +
                "hinhThucBan TEXT" +
                ")";
        db.execSQL(sql);

        // Thêm dữ liệu mẫu có hình ảnh
        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        insertSanPham(db, "chocolate_cake", "B001", "Bánh kem Chocolate", 80000, "Bánh kem",
                "Bánh kem socola mềm mịn, phủ lớp kem tươi", 45, "Đặt trước");
        insertSanPham(db, "sample_tiramisu", "B002", "Bánh tiramisu", 75000, "Bánh lạnh",
                "Bánh Ý với lớp mascarpone và cà phê đậm đà", 60, "Đặt trước");
        insertSanPham(db, "sample_sukem", "B003", "Bánh su kem nhỏ (10 cái)", 50000, "Bánh ngọt",
                "Vỏ mỏng, nhân kem vani thơm béo", 100, "Đặt ngay");
        insertSanPham(db, "sample_mousse", "B004", "Bánh mousse dâu", 70000, "Bánh lạnh",
                "Bánh mousse dâu tươi, mềm mịn và mát lạnh", 35, "Đặt trước");
        insertSanPham(db, "sample_trungmuoi", "B005", "Bánh bông lan trứng muối", 65000, "Bánh ngọt",
                "Bông lan mềm, trứng muối mặn ngọt hài hòa", 80, "Đặt ngay");
        insertSanPham(db, "sample_cupcake", "B006", "Bánh cupcake socola", 30000, "Cupcake",
                "Bánh nhỏ xinh với hương vị socola đậm đà", 50, "Đặt ngay");
        insertSanPham(db, "sample_tart", "B007", "Bánh tart trái cây", 40000, "Bánh ngọt",
                "Đế tart giòn, topping trái cây tươi", 32, "Đặt ngay");
        insertSanPham(db, "sample_crepe", "B008", "Bánh crepe sầu riêng", 85000, "Bánh lạnh",
                "Lớp bánh mỏng cuộn nhân kem sầu riêng béo ngậy", 48, "Đặt trước");
    }

    private void insertSanPham(SQLiteDatabase db, String hinhAnh, String maSP, String tenSP, int giaBan,
                               String loaiBanh, String moTa, int soLuongDaBan, String hinhThucBan) {
        ContentValues values = new ContentValues();
        values.put("hinhAnh", hinhAnh);
        values.put("maSP", maSP);
        values.put("tenSP", tenSP);
        values.put("giaBan", giaBan);
        values.put("loaiBanh", loaiBanh);
        values.put("moTa", moTa);
        values.put("soLuongDaBan", soLuongDaBan);
        values.put("hinhThucBan", hinhThucBan);
        db.insert("sanpham", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS sanpham");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANPHAM);
        onCreate(db);
    }

    // Trả về danh sách tất cả các  sản phẩm từ csdl

    public List<SanPham> getAllSanPham() {
        List<SanPham> sanPhamList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SANPHAM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                sanPhamList.add(createSanPhamFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sanPhamList;
    }
    // Tìm sản phẩm theo mã hoặc tên
    public List<SanPham> searchSanPham(String query) {
        List<SanPham> sanPhamList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Sử dụng LIKE để tìm kiếm gần đúng, tìm cả trong mã SP và tên SP
        String selectQuery = "SELECT * FROM " + TABLE_SANPHAM +
                " WHERE " + COL_MASP + " LIKE ?" +
                " OR " + COL_TENSP + " LIKE ?";

        // Thêm % vào đầu và cuối query để tìm kiếm chứa chuỗi
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%", "%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                sanPhamList.add(createSanPhamFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sanPhamList;
    }
    //Phương thức helper để tạo đối tượng SanPham từ Cursor.
    private SanPham createSanPhamFromCursor(Cursor cursor) {
        return new SanPham(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_HINHANH)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_MASP)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TENSP)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_GIABAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_LOAIBAHN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_MOTA)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_SOLUONGDABAN)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_HINHTHUCBAN))
        );
    }
    //chức năng thêm sản phẩm
    public boolean addSanPham(SanPham sanPham) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HINHANH, sanPham.getHinhAnh());
        values.put(COL_MASP, sanPham.getMaSP());
        values.put(COL_TENSP, sanPham.getTenSP());
        values.put(COL_GIABAN, sanPham.getGiaBan());
        values.put(COL_LOAIBAHN, sanPham.getLoaiBanh());
        values.put(COL_MOTA, sanPham.getMoTa());
        values.put(COL_SOLUONGDABAN, sanPham.getSoLuongDaBan());
        values.put(COL_HINHTHUCBAN, sanPham.getHinhThucBan());

        long result = db.insert(TABLE_SANPHAM, null, values);
        db.close();
        return result != -1; // -1 nếu lỗi
    }
    //chức năng sửa sản phẩm
    public int updateSanPham(SanPham sanPham) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HINHANH, sanPham.getHinhAnh());
        values.put(COL_TENSP, sanPham.getTenSP());
        values.put(COL_GIABAN, sanPham.getGiaBan());
        values.put(COL_LOAIBAHN, sanPham.getLoaiBanh());
        values.put(COL_MOTA, sanPham.getMoTa());
        values.put(COL_SOLUONGDABAN, sanPham.getSoLuongDaBan());
        values.put(COL_HINHTHUCBAN, sanPham.getHinhThucBan());

        int rowsAffected = db.update(TABLE_SANPHAM, values, COL_MASP + " = ?",
                new String[]{sanPham.getMaSP()});
        db.close();
        return rowsAffected;
    }
    //chức năng xóa
    public int deleteSanPham(String maSP) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_SANPHAM, COL_MASP + " = ?",
                new String[]{maSP});
        db.close();
        return rowsAffected;
    }
}
