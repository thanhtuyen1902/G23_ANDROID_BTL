package vn.edu.tlu.group23.mybakeryapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.models.Shift;

public class ShiftDAO {
    private SQLiteDatabase db;

    public ShiftDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public List<Shift> getAllShifts() {
        List<Shift> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM shifts ORDER BY id DESC", null);
        while (cursor.moveToNext()) {
            list.add(new Shift(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
        cursor.close();
        return list;
    }
    public long insertShift(String name, String startTime, String endTime) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("start_time", startTime);
        values.put("end_time", endTime);
        return db.insert("shifts", null, values);
    }
    public void insertSampleShiftsIfEmpty() {
        if(getAllShifts().isEmpty()) {
            insertShift("Ca sáng", "06:00", "10:30");
            insertShift("Ca chiều", "13:00", "17:00");
            insertShift("Ca tối", "18:00", "22:00");
        }

    }

    public void clearShifts() {
        db.delete("shifts", null, null);
    }
}
