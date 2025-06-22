package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.adapters.ShiftAdapter;
import vn.edu.tlu.group23.mybakeryapp.database.DatabaseHelper;
import vn.edu.tlu.group23.mybakeryapp.database.ShiftDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Shift;

public class TaskActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    ShiftDAO shiftDAO;
    private RecyclerView recyclerView;
    private ShiftAdapter adapter;
    private List<Shift> shiftList;
    Button btnAddShift;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        shiftDAO = new ShiftDAO(db);

        shiftDAO.clearShifts();
        //chèn dữ liệu mẫu
        shiftDAO.insertSampleShiftsIfEmpty();

        // Định vị RecyclerView
        recyclerView = findViewById(R.id.recyclerViewShift);
        btnAddShift = findViewById(R.id.btnAddShift);
        backArrow = findViewById(R.id.backArrow);
        // Load danh sách ca ban đầu
        loadShifts();

        btnAddShift.setOnClickListener(v -> {
            AddShiftDialog dialog = new AddShiftDialog(
                    this,
                    shiftDAO,
                    this::loadShifts // callback reload sau khi thêm
            );
            dialog.show();
        });

        // Quay lại
        backArrow.setOnClickListener(v -> finish());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootConstraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

    }


    private void loadShifts() {
        shiftList = shiftDAO.getAllShifts();

        adapter = new ShiftAdapter(this, shiftList, shift -> {
            // Khi click vào ca -> chuyển màn chi tiết
            Intent intent = new Intent(TaskActivity.this, ShiftDetailActivity.class);
            intent.putExtra("shift_id", shift.getId());
            intent.putExtra("shift_name", shift.getName());
            intent.putExtra("start_time", shift.getStartTime());
            intent.putExtra("end_time", shift.getEndTime());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}