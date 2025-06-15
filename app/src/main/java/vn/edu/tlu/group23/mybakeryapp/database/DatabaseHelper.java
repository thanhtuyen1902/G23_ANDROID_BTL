package vn.edu.tlu.group23.mybakeryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Bakery.db";
    public static final int DATABASE_VERSION = 1;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Employee");
        onCreate(db);
    }

    //Hàm tạo bảng Employee
    private void createEmployeeTable(SQLiteDatabase db) {
        //Tạo bảng employee
        String createEmployeeTable = "CREATE TABLE Employee (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT, " +
                "fullname TEXT, " +
                "phone TEXT, " +
                "email TEXT" +
                ")";
        db.execSQL(createEmployeeTable);
    }
}
