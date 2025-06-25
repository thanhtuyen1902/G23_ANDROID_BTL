package vn.edu.tlu.group23.mybakeryapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.adapters.EmployeeTaskAdapter;
import vn.edu.tlu.group23.mybakeryapp.adapters.TaskAdapter;
import vn.edu.tlu.group23.mybakeryapp.database.DatabaseHelper;
import vn.edu.tlu.group23.mybakeryapp.database.ShiftDAO;
import vn.edu.tlu.group23.mybakeryapp.database.TaskDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Shift;
import vn.edu.tlu.group23.mybakeryapp.models.Task;
import vn.edu.tlu.group23.mybakeryapp.receivers.ShiftReminderReceiver;

public class EmployeeTasksActivity extends AppCompatActivity {
    private ConstraintLayout layoutTimekeeping, layoutTasks;
    private Button btnCheckInCheckOut, btnChamCong, btnCongViec;
    private TextView tvCheckInStatus, tvCheckInTimeAndLocation;
    // Lấy danh sách task
    List<Task> taskList;
    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private FusedLocationProviderClient fusedLocationClient;
    private boolean isCheckedIn = false;

    private TaskDAO taskDAO;
    private ShiftDAO shiftDAO;
    private int selectedShiftId = -1;
    TextView tvEmployeeName, tvEmployeeRole;
    View includedHeader;
    ImageView imgLogout;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_tasks);


        //Xin quyền hiển thị thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1234);
            }
        }
        // Lấy thông tin nhân viên từ SharedPreferences
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String tenNV = prefs.getString("tenNV", "Tên nhân viên");
        String chucVu = prefs.getString("chucVu", "Chức vụ");
        String employeeId = prefs.getString("user_id", null);
        initViews();
        // Gán giá trị hiển thị
        tvEmployeeName.setText(tenNV);
        tvEmployeeRole.setText("Chức vụ: " + chucVu);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Init MapView
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(googleMap -> {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Vị trí của bạn"));
                    }
                });
            }
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        taskDAO = new TaskDAO(db);
        shiftDAO = new ShiftDAO(dbHelper.getReadableDatabase());
        // Lấy danh sách task
        taskList = taskDAO.getTasksByAssigneeId(employeeId);

        //Lăp qua để đặt lịch
        for (Task task : taskList) {
            Shift shift = shiftDAO.getShiftById(task.getShiftId());
            if (shift != null) {
                scheduleShiftReminder(shift.getId(), shift.getName(), shift.getStartTime());
            }
        }
        //chuyển qua lại giữa 2 tabs
        setupTabs();
        // Mặc định hiển thị tab Chấm công
//        layoutTimekeeping.setVisibility(View.VISIBLE);
//        layoutTasks.setVisibility(View.GONE);
        setupCheckInButton();

        // Gắn vào RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEmployeeTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EmployeeTaskAdapter adapter = new EmployeeTaskAdapter(this, taskList, taskDAO, shiftDAO);
        recyclerView.setAdapter(adapter);

        //Đăng xuất
        imgLogout.setOnClickListener(v -> {
            // Xoá dữ liệu đăng nhập
            prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(EmployeeTasksActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

            // Quay về màn đăng nhập và xoá backstack
            Intent intent = new Intent(EmployeeTasksActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void initViews() {
        layoutTimekeeping = findViewById(R.id.layoutTimekeeping);
        layoutTasks = findViewById(R.id.layoutTasks);
        btnCheckInCheckOut = findViewById(R.id.btnCheckInCheckOut);
        tvCheckInStatus = findViewById(R.id.tvCheckInStatus);
        tvCheckInTimeAndLocation = findViewById(R.id.tvCheckInTimeAndLocation);

        btnChamCong = findViewById(R.id.btnChamCong);
        btnCongViec = findViewById(R.id.btnCongViec);
        mapView = findViewById(R.id.mapView);
        includedHeader = findViewById(R.id.includedHeader);
        imgLogout = includedHeader.findViewById(R.id.imgLogout);

        tvEmployeeName = findViewById(R.id.tvEmployeeName);
        tvEmployeeRole = findViewById(R.id.tvEmployeeRole);
    }

    private void setupTabs() {
        btnChamCong.setOnClickListener(v -> {
            layoutTimekeeping.setVisibility(View.VISIBLE);
            layoutTasks.setVisibility(View.GONE);
            // Làm nổi bật tab Chấm công
            btnChamCong.setSelected(true);
            btnCongViec.setSelected(false);
        });

        btnCongViec.setOnClickListener(v -> {
            layoutTimekeeping.setVisibility(View.GONE);
            layoutTasks.setVisibility(View.VISIBLE);
            btnChamCong.setSelected(false);
            btnCongViec.setSelected(true);
        });

        // Trạng thái mặc định ban đầu
        layoutTimekeeping.setVisibility(View.VISIBLE);
        layoutTasks.setVisibility(View.GONE);
        btnChamCong.setSelected(true);
        btnCongViec.setSelected(false);
    }

    private void setupCheckInButton() {
        btnCheckInCheckOut.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1001);
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) updateCheckInUI(location);
                else Toast.makeText(this, "Không lấy được vị trí.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void updateCheckInUI(Location location) {
        isCheckedIn = !isCheckedIn;
        String status = isCheckedIn ? "Đã check-in" : "Đã check-out";
        String time = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String locationStr = String.format("Tại vị trí: %.5f, %.5f", location.getLatitude(), location.getLongitude());

        tvCheckInStatus.setText(status);
        tvCheckInStatus.setVisibility(View.VISIBLE);
        tvCheckInTimeAndLocation.setText("Vào lúc " + time + "\n" + locationStr);
        tvCheckInTimeAndLocation.setVisibility(View.VISIBLE);
        btnCheckInCheckOut.setText(isCheckedIn ? "Check-out" : "Check-in");
    }

    //Hàm đặt lịch nhắc nhở
    private void scheduleShiftReminder(int shiftId, String shiftName, String startTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date shiftStart = sdf.parse(startTime);

            Calendar now = Calendar.getInstance();
            Calendar shiftTime = Calendar.getInstance();

            Calendar tempCal = Calendar.getInstance();  // để lấy giờ/phút từ Date
            tempCal.setTime(shiftStart);

            shiftTime.set(Calendar.HOUR_OF_DAY, tempCal.get(Calendar.HOUR_OF_DAY));
            shiftTime.set(Calendar.MINUTE, tempCal.get(Calendar.MINUTE));
            shiftTime.set(Calendar.SECOND, 0);
            shiftTime.set(Calendar.MILLISECOND, 0);

            Calendar remindTime = (Calendar) shiftTime.clone();
            remindTime.add(Calendar.MINUTE, -15);  // Thời gian nhắc
            // 📌 Log kiểm tra
            Log.d("SHIFT_REMINDER", "Shift " + shiftName + " start at " + startTime +
                    " → sẽ báo lúc: " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(remindTime.getTime()));
            Log.d("SHIFT_REMINDER", "Giờ hiện tại: " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now.getTime()));
            if (remindTime.after(now)) { // Nếu giờ đó chưa tới thì đặt lịch
                Log.d("SHIFT_REMINDER", "→ ĐẶT lịch nhắc thành công");
                Intent intent = new Intent(this, ShiftReminderReceiver.class);
                intent.putExtra("shift_name", shiftName);
                intent.putExtra("shift_id", shiftId);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, shiftId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, remindTime.getTimeInMillis(), pendingIntent);
            }else if (now.before(shiftTime)) {
                Log.d("SHIFT_REMINDER", "→ ĐANG TRONG KHOẢNG NHẮC → gửi thông báo ngay!");

                // Gửi thông báo ngay lập tức
                Intent intent = new Intent(this, ShiftReminderReceiver.class);
                intent.putExtra("shift_name", shiftName);
                sendBroadcast(intent);  // Gửi ngay
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MapView lifecycle methods
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });


    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }


}