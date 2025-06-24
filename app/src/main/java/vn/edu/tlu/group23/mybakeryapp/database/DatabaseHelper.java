package vn.edu.tlu.group23.mybakeryapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import vn.edu.tlu.group23.mybakeryapp.models.Product;
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Bakery.db";
    public static final int DATABASE_VERSION = 8;
    private Context context;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // lưu context để gọi DAO
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Gọi hàm tạo bảng
        createEmployeeTable(db);
        // Thêm tài khoản mẫu để test
//        db.execSQL("INSERT INTO Employee (username, password, fullname, phone, email) " +
//                "VALUES ('admin', '123456', 'Quản trị hệ thống', '0123456789', 'admin@bakery.com')");
        //tạo bảng sản phẩm
        createProductTable(db);
        //tạo bảng shift
        createShiftTable(db);
        //tạo bảng task
        createTaskTable(db);

        //chèn dữ liệu mẫu
        db.execSQL("INSERT INTO Employee (maNV, tenNV, soDienThoai, chucVu, userName, passWord, role) " +
                "VALUES ('NV001', 'Admin Bakery', '0909000001', 'Quản trị hệ thống', 'admin', '123456', 'admin')");

        db.execSQL("INSERT INTO Employee (maNV, tenNV, soDienThoai, chucVu, userName, passWord, role) " +
                "VALUES ('NV002', 'Nhân viên Bán hàng', '0909000002', 'Nhân viên', 'staff', '123456', 'staff')");

        db.execSQL("INSERT INTO Employee (maNV, tenNV, soDienThoai, chucVu, userName, passWord, role) " +

                "VALUES ('NV003', 'Nhân viên Làm bánh', '0909560002', 'Nhân viên', 'staff2', '123456', 'staff')");

                "VALUES ('NV003', 'Nguyễn Lan Anh', '0323456182', 'Thợ làm bánh', 'nguyenlananh', '123456', 'staff')");

        db.execSQL("INSERT INTO Employee (maNV, tenNV, soDienThoai, chucVu, userName, passWord, role) " +
                "VALUES ('NV004', 'Đinh Hoàng Anh', '0385998231', 'Thợ nướng bánh', 'dinhhoanganh', '123456', 'staff')");


        Cursor cursor = db.rawQuery("SELECT * FROM Employee", null);
        Log.d("TEST_EMPLOYEES", "Tổng số tài khoản: " + cursor.getCount());
        while (cursor.moveToNext()) {
            String u = cursor.getString(cursor.getColumnIndexOrThrow("userName"));
            String p = cursor.getString(cursor.getColumnIndexOrThrow("passWord"));
            Log.d("TEST_EMPLOYEES", "Tài khoản: " + u + " - Mật khẩu: " + p);
        }
        cursor.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANPHAM);
        db.execSQL("DROP TABLE IF EXISTS shifts");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANPHAM);
        db.execSQL("DROP TABLE IF EXISTS shifts");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    //Hàm tạo bảng Employee
    private void createEmployeeTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_EMPLOYEE + " (" +
                COL_MANV + " TEXT PRIMARY KEY, " +
                COL_TENNV + " TEXT, " +
                COL_SDT + " TEXT, " +
                COL_CHUCVU + " TEXT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT DEFAULT 'staff'" +
                ")";
        db.execSQL(sql);
    }

    // Bảng Employee
    public static final String TABLE_EMPLOYEE = "Employee";
    public static final String COL_MANV = "maNV";
    public static final String COL_TENNV = "tenNV";
    public static final String COL_SDT = "soDienThoai";
    public static final String COL_CHUCVU = "chucVu";
    public static final String COL_USERNAME = "userName";
    public static final String COL_PASSWORD = "passWord";
    public static final String COL_ROLE = "role";


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

    //Tạo bảng shift
    private void createShiftTable(SQLiteDatabase db) {

        String createShiftTable = "CREATE TABLE shifts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "start_time TEXT, " +
                "end_time TEXT)";
        db.execSQL(createShiftTable);
    }


    private void createTaskTable(SQLiteDatabase db) {
        String createTaskTable = "CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shift_id INTEGER NOT NULL, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "assignee_id TEXT, " +
                "status TEXT, " +
                "priority TEXT, " +
                "FOREIGN KEY (shift_id) REFERENCES shifts(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (assignee_id) REFERENCES Employee(maNV) ON DELETE SET NULL" +
                ")";
        db.execSQL(createTaskTable);
    }
}