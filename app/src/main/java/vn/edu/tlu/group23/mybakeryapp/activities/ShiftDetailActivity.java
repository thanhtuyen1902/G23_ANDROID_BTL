package vn.edu.tlu.group23.mybakeryapp.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.adapters.TaskAdapter;
import vn.edu.tlu.group23.mybakeryapp.database.DatabaseHelper;
import vn.edu.tlu.group23.mybakeryapp.database.TaskDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Task;

public class ShiftDetailActivity extends AppCompatActivity {
    private TextView shiftNameTitle;
    private SearchView searchView;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();

    private SQLiteDatabase db;
    private TaskDAO taskDAO;
    private int shiftId;
    private String shiftName;
    Button btnAddJob;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shift_detail);

        // Nhận dữ liệu từ Intent
        shiftId = getIntent().getIntExtra("shift_id", -1);
        shiftName = getIntent().getStringExtra("shift_name");



        //Định vị các phần tử
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        searchView = findViewById(R.id.editTextSearch);
        shiftNameTitle = findViewById(R.id.shiftNameTitle);
        btnAddJob = findViewById(R.id.btnAddJob);
        backArrow = findViewById(R.id.backArrow);

        // Gán view
        shiftNameTitle.setText(shiftName);

        // DB & DAO
        db = new DatabaseHelper(this).getWritableDatabase();
        taskDAO = new TaskDAO(db);

        //Thực hiện chèn dữ liệu mẫu
        taskDAO.insertSampleTasksIfEmpty(shiftId);

        // Hiển thị danh sách task ban đầu
        loadTasks();

        // Tìm kiếm task theo tên
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override public boolean onQueryTextSubmit(String query) { return false; }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                taskAdapter.filter(newText);
//                return true;
//            }
//        });
        // Thêm công việc mới
        btnAddJob.setOnClickListener(view -> {
            AddEditTaskDialog dialog = new AddEditTaskDialog(this, shiftId, taskDAO, this::loadTasks);
            dialog.show();
        });
        // Quay lại
        backArrow.setOnClickListener(v -> finish());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks(); // refresh khi quay lại
    }

    private void loadTasks() {
        taskList = taskDAO.getTasksByShift(shiftId);
        taskAdapter = new TaskAdapter(this, taskList, task -> {
            // Mở dialog sửa khi click vào 1 task
            AddEditTaskDialog editDialog = new AddEditTaskDialog(
                    ShiftDetailActivity.this,
                    task,
                    taskDAO,
                    this::loadTasks
            );
            editDialog.show();
        });
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(taskAdapter);
    }
}