package vn.edu.tlu.group23.mybakeryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import vn.edu.tlu.group23.mybakeryapp.models.Product;
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Bakery.db";
    public static final int DATABASE_VERSION = 2;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Gọi hàm tạo bảng
        createEmployeeTable(db);
        // Thêm tài khoản mẫu để test
        db.execSQL("INSERT INTO Employee (username, password, fullname, phone, email) " +
                "VALUES ('admin', '123456', 'Quản trị hệ thống', '0123456789', 'admin@bakery.com')");
        //tạo bảng sản phẩm
        createProductTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Employee");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANPHAM);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        onCreate(db);
    }

    //Hàm tạo bảng Employee
    private void createEmployeeTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_EMPLOYEE + " (" +
                COL_MANV + " TEXT PRIMARY KEY, " +
                COL_TENNV + " TEXT, " +
                COL_SDT + " TEXT, " +
                COL_CHUCVU + " TEXT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT" +
                ")";
        db.execSQL(sql);
    }

    // Bảng Employee
    public static final String TABLE_EMPLOYEE = "Employee";
    public static final String COL_MANV = "maNV";
    public static final String COL_TENNV = "tenNV";
    public static final String COL_SDT = "soDienThoai";
    public static final String COL_CHUCVU = "chucVu";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
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

    public void createProductTable(@NonNull SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_SANPHAM + " (" +
                COL_HINHANH + " TEXT," +
                COL_MASP + " TEXT PRIMARY KEY," +
                COL_TENSP + " TEXT," +
                COL_GIABAN + " INTEGER," +
                COL_LOAIBAHN + " TEXT," +
                COL_MOTA + " TEXT," +
                COL_SOLUONGDABAN + " INTEGER," +
                COL_HINHTHUCBAN + " TEXT" +
                ")";
        db.execSQL(sql);
        // Dữ liệu mẫu giờ sẽ được chèn qua ProductDao.insertInitialSampleData()
    }



}
