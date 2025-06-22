package vn.edu.tlu.group23.mybakeryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.models.Employee;

public class EmployeeDAO {
    DatabaseHelper dbHelper;

    public EmployeeDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm nhân viên
    public long insertEmployee(Employee emp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNV", emp.getMaNV());
        values.put("tenNV", emp.getTenNV());
        values.put("soDienThoai", emp.getSoDienThoai());
        values.put("chucVu", emp.getChucVu());
        values.put("userName", emp.getUserName());
        values.put("passWord", emp.getPassWord());
        return db.insert("Employee", null, values);
    }

    // Lấy toàn bộ nhân viên
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Employee", null);
        while (cursor.moveToNext()) {
            Employee emp = new Employee();
            emp.setMaNV(cursor.getString(cursor.getColumnIndexOrThrow("maNV")));
            emp.setTenNV(cursor.getString(cursor.getColumnIndexOrThrow("tenNV")));
            emp.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow("soDienThoai")));
            emp.setChucVu(cursor.getString(cursor.getColumnIndexOrThrow("chucVu")));
            emp.setUserName(cursor.getString(cursor.getColumnIndexOrThrow("userName")));
            emp.setPassWord(cursor.getString(cursor.getColumnIndexOrThrow("passWord")));
            list.add(emp);
        }
        cursor.close();
        return list;
    }

    // Cập nhật nhân viên
    public int updateEmployee(Employee emp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenNV", emp.getTenNV());
        values.put("soDienThoai", emp.getSoDienThoai());
        values.put("chucVu", emp.getChucVu());
        values.put("userName", emp.getUserName());
        values.put("passWord", emp.getPassWord());
        return db.update("Employee", values, "maNV = ?", new String[]{emp.getMaNV()});
    }

    // Xoá nhân viên
    public int deleteEmployee(String maNV) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("Employee", "maNV = ?", new String[]{maNV});
    }

    // Kiểm tra đăng nhập
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM Employee WHERE userName = ? AND passWord = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
}