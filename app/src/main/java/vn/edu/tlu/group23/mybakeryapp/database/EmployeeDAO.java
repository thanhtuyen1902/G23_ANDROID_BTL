package vn.edu.tlu.group23.mybakeryapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//Xử lý login và CRUD nhân viên
public class EmployeeDAO {
    DatabaseHelper dbHelper;

    public EmployeeDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Kiểm tra đăng nhập
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM Employee WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
}
