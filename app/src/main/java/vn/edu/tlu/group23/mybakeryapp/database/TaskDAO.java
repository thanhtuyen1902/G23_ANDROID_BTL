package vn.edu.tlu.group23.mybakeryapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.models.Task;

//Xử lý CRUD Công việc
public class TaskDAO {
    private SQLiteDatabase db;

    public TaskDAO(SQLiteDatabase db) {
        this.db = db;
    }
    //Thêm dữ liệu mẫu
    public void insertSampleTasksIfEmpty(int shiftId) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tasks WHERE shift_id = ?", new String[]{String.valueOf(shiftId)});
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            insertTask(new Task(shiftId, "Kiểm tra thiết bị", "Kiểm tra tất cả thiết bị trước khi bắt đầu ca", "Nguyễn Văn A", "Hoàn thành", "Cao"));
            insertTask(new Task(shiftId, "Chuẩn bị nguyên liệu", "Chuẩn bị nguyên liệu cho ca sáng", "Nguyễn Thị B", "Đang thực hiện", "Trung bình"));
        }
        cursor.close();
    }
    // Lấy tất cả công việc theo shift_id
    public List<Task> getTasksByShift(int shiftId) {
        List<Task> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE shift_id = ?", new String[]{String.valueOf(shiftId)});

        while (cursor.moveToNext()) {
            list.add(new Task(
                    cursor.getInt(0),                      // id
                    cursor.getInt(1),                      // shift_id
                    cursor.getString(2),                   // title
                    cursor.getString(3),                   // description
                    cursor.getString(4),                   // assignee
                    cursor.getString(5),                   // status
                    cursor.getString(6)                    // priority
            ));
        }

        cursor.close();
        return list;
    }

    // Thêm công việc mới
    public long insertTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("shift_id", task.getShiftId());
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("assignee", task.getAssignee());
        values.put("status", task.getStatus());
        values.put("priority", task.getPriority());

        return db.insert("tasks", null, values);
    }

    // Cập nhật công việc
    public int updateTask(Task task) {
        ContentValues values = new ContentValues();
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("assignee", task.getAssignee());
        values.put("status", task.getStatus());
        values.put("priority", task.getPriority());

        return db.update("tasks", values, "id = ?", new String[]{String.valueOf(task.getId())});
    }

    // Xoá công việc theo ID
    public int deleteTask(int taskId) {
        return db.delete("tasks", "id = ?", new String[]{String.valueOf(taskId)});
    }
}
