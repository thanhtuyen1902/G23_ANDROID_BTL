package vn.edu.tlu.group23.mybakeryapp.activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.ShiftDAO;

public class AddShiftDialog extends Dialog {
    private Context context;
    private ShiftDAO shiftDAO;
    private Runnable onSaved;
    EditText edtName, edtStart, edtEnd;
    Button btnSave;
    public AddShiftDialog(@NonNull Context context, ShiftDAO shiftDAO, Runnable onSaved) {
        super(context);
        this.context = context;
        this.shiftDAO = shiftDAO;
        this.onSaved = onSaved;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_shift);

        // Set chiều rộng dialog 90% màn hình
        if (getWindow() != null) {
            getWindow().setLayout(
                    (int)(context.getResources().getDisplayMetrics().widthPixels * 0.9),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getWindow().setBackgroundDrawableResource(android.R.color.transparent); // nếu cần bo góc
        }

        //Định vị các phần tử
        edtName = findViewById(R.id.edtShiftName);
        edtStart = findViewById(R.id.edtStartTime);
        edtEnd = findViewById(R.id.edtEndTime);
        btnSave = findViewById(R.id.btnSaveShift);

        // Nút đóng X
        findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());

        edtStart.setOnClickListener(v -> showTimePicker(edtStart));
        edtEnd.setOnClickListener(v -> showTimePicker(edtEnd));

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String start = edtStart.getText().toString().trim();
            String end = edtEnd.getText().toString().trim();

            if (name.isEmpty() || start.isEmpty() || end.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = shiftDAO.insertShift(name, start, end);
            if (result > 0) {
                Toast.makeText(context, "Đã thêm ca làm việc", Toast.LENGTH_SHORT).show();
                dismiss();
                onSaved.run(); // gọi callback để load lại danh sách
            } else {
                Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showTimePicker(EditText target) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(context, (view, hourOfDay, minuteOfHour) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
            target.setText(time);
        }, hour, minute, true); // 24h format
        dialog.show();
    }

}
