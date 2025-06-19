package vn.edu.tlu.cse.ht1.thuy.myappcake;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SanPhamService {
    private BakeryDBHelper dbHelper;

    public SanPhamService(Context context) {
        dbHelper = new BakeryDBHelper(context);
    }

    public SanPham timTheoMa(String maSP) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sanpham WHERE maSP = ?", new String[]{maSP});

        if (cursor.moveToFirst()) {
            String hinhAnh = cursor.getString(cursor.getColumnIndexOrThrow("hinhAnh"));
            String tenSP = cursor.getString(cursor.getColumnIndexOrThrow("tenSP"));
            int giaBan = cursor.getInt(cursor.getColumnIndexOrThrow("giaBan"));
            String loaiBanh = cursor.getString(cursor.getColumnIndexOrThrow("loaiBanh"));
            String moTa = cursor.getString(cursor.getColumnIndexOrThrow("moTa"));
            int soLuongDaBan = cursor.getInt(cursor.getColumnIndexOrThrow("soLuongDaBan"));
            String hinhThucBan = cursor.getString(cursor.getColumnIndexOrThrow("hinhThucBan"));

            cursor.close();
            db.close();

            return new SanPham(hinhAnh, maSP, tenSP, giaBan, loaiBanh, moTa, soLuongDaBan, hinhThucBan);
        }

        cursor.close();
        db.close();
        return null;
    }
}
