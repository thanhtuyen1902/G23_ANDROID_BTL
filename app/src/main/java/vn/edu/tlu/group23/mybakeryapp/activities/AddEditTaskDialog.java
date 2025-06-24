package vn.edu.tlu.group23.mybakeryapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.DatabaseHelper;
import vn.edu.tlu.group23.mybakeryapp.database.EmployeeDAO;
import vn.edu.tlu.group23.mybakeryapp.database.TaskDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Employee;
import vn.edu.tlu.group23.mybakeryapp.models.Task;

public class AddEditTaskDialog extends Dialog {
    private Context context;
    private TaskDAO taskDAO;
    private int shiftId;
    private Task taskToEdit; // null nếu thêm mới, có giá trị nếu sửa
    private Runnable onTaskChanged; // callback load lại danh sách
    private EmployeeDAO employeeDAO;
    private List<Employee> employeeList;
    TextView dialogTitle;
    EditText edtTaskName, edtDescription;
    Spinner spinnerAssignee, spinnerPriority;
    Button btnSave;
    ImageView btnClose;
    public AddEditTaskDialog(Context context, int shiftId, TaskDAO taskDAO, Runnable onTaskChanged) {
        super(context);
        this.context = context;
        this.shiftId = shiftId;
        this.taskDAO = taskDAO;
        this.onTaskChanged = onTaskChanged;
        this.employeeDAO = new EmployeeDAO(context);
    }

    // Constructor dùng khi sửa
    public AddEditTaskDialog(Context context, Task taskToEdit, TaskDAO taskDAO, Runnable onTaskChanged) {
        super(context);
        this.context = context;
        this.taskToEdit = taskToEdit;
        this.shiftId = taskToEdit.getShiftId();
        this.taskDAO = taskDAO;
        this.onTaskChanged = onTaskChanged;
        this.employeeDAO = new EmployeeDAO(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_edit_task);

        // Set chiều rộng dialog 90% màn hình
        if (getWindow() != null) {
            getWindow().setLayout(
                    (int)(context.getResources().getDisplayMetrics().widthPixels * 0.9),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getWindow().setBackgroundDrawableResource(android.R.color.transparent); // nếu cần bo góc
        }

        // Ánh xạ View
        dialogTitle = findViewById(R.id.dialogTitle);
        edtTaskName = findViewById(R.id.editTextTaskName);
        edtDescription = findViewById(R.id.editTextDescription);
        spinnerAssignee = findViewById(R.id.spinnerAssignTo);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        btnSave = findViewById(R.id.btnSaveShift);
        btnClose = findViewById(R.id.closeButton);

        //Thiết lập dữ liệu cho spinner mức độ ưu tiên
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item,
                new String[]{"Cao", "Trung bình", "Thấp"});
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        // Thiết lập spinner nhân viên từ SQLite
        employeeList = employeeDAO.getAllEmployees();
        ArrayAdapter<Employee> employeeAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                employeeList
        );
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignee.setAdapter(employeeAdapter);

        // Nếu đang sửa, điền dữ liệu cũ
        if (taskToEdit != null) {
            dialogTitle.setText("Sửa công việc");
            btnSave.setText("Cập nhật");

            edtTaskName.setText(taskToEdit.getTitle());
            edtDescription.setText(taskToEdit.getDescription());

            // set selection cho spinner
            for (int i = 0; i < employeeList.size(); i++) {
                if (employeeList.get(i).getMaNV().equals(taskToEdit.getAssignee())) {
                    spinnerAssignee.setSelection(i);
                    break;
                }
            }

            int priorityPos = priorityAdapter.getPosition(taskToEdit.getPriority());
            if (priorityPos >= 0) spinnerPriority.setSelection(priorityPos);
        } else {
            dialogTitle.setText("Thêm công việc mới");
            btnSave.setText("Thêm công việc");
        }

        // Đóng dialog
        btnClose.setOnClickListener(v -> dismiss());

        // Xử lý lưu (khi thêm hoặc sửa)
        btnSave.setOnClickListener(v -> {
            String title = edtTaskName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            Employee selectedEmployee = (Employee) spinnerAssignee.getSelectedItem();
            String assignee = selectedEmployee.getMaNV();
            String priority = spinnerPriority.getSelectedItem().toString();
            String status = "Chưa làm"; // mặc định là to-do

            if (title.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập tên công việc", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean success;
            if (taskToEdit != null) {
                // Sửa
                taskToEdit.setTitle(title);
                taskToEdit.setDescription(description);
                taskToEdit.setAssignee(assignee);
                taskToEdit.setPriority(priority);
                success = taskDAO.updateTask(taskToEdit) > 0;
            } else {
                // Thêm mới
                Task newTask = new Task(shiftId, title, description, assignee, status, priority);
                success = taskDAO.insertTask(newTask) != -1;
            }

            if (success) {
                Toast.makeText(context, "Đã lưu công việc", Toast.LENGTH_SHORT).show();
                onTaskChanged.run();
                dismiss();
            } else {
                Toast.makeText(context, "Lưu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
