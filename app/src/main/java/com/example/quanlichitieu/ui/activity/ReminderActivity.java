package com.example.quanlichitieu.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.TaskReminder;
import com.example.quanlichitieu.data.local.utils.ReminderReceiver;
import com.example.quanlichitieu.ui.adapter.ReminderAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {
    private appDatabase db;
    private ReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tools_reminder);
        checkExactAlarmPermission();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = Room.databaseBuilder(getApplicationContext(), appDatabase.class, "reminder-db")
                .allowMainThreadQueries().build();
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        findViewById(R.id.add_reminder_button).setOnClickListener(v -> showAddDialog());

        RecyclerView rv = findViewById(R.id.recyclerReminders);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReminderAdapter(this, db.reminderDao().getAll(), db);
        rv.setAdapter(adapter);

    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.tools_add_reminder_dialog, null);
        EditText etTitle = view.findViewById(R.id.etTitle);
        TimePicker timePicker = view.findViewById(R.id.timePicker);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        Spinner spinner = view.findViewById(R.id.spinnerRepeat);

//        timePicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//        datePicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        timePicker.setIs24HourView(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.repeat_options, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        new AlertDialog.Builder(this)
                .setTitle("Thêm lời nhắc")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, datePicker.getYear());
                    cal.set(Calendar.MONTH, datePicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    cal.set(Calendar.MINUTE, timePicker.getMinute());
                    cal.set(Calendar.SECOND, 0);

                    TaskReminder reminder = new TaskReminder();
                    reminder.title = etTitle.getText().toString();
                    reminder.timeMillis = cal.getTimeInMillis();
                    reminder.repeatType = spinner.getSelectedItem().toString();
                    reminder.isActive = true;


                    long id = db.reminderDao().insert(reminder);
                    scheduleAlarm((int) id, cal.getTimeInMillis(), reminder.repeatType);

                    refreshList();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void refreshList() {
        adapter.updateData(db.reminderDao().getAll());
    }

    private void scheduleAlarm(int id, long timeMillis, String repeatType) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("title", "Lời nhắc");
        intent.putExtra("id", id);
        PendingIntent pi = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (repeatType.equals("Một lần")) {
            try {
                am.setExact(AlarmManager.RTC_WAKEUP, timeMillis, pi);
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            }
        } else {
            long interval;
            switch (repeatType) {
                case "Mỗi ngày":
                    interval = AlarmManager.INTERVAL_DAY;
                    break;
                case "Mỗi tuần":
                    interval = AlarmManager.INTERVAL_DAY * 7;
                    break;
                case "Mỗi tháng":
                    interval = AlarmManager.INTERVAL_DAY * 30;
                    break;
                default:
                    interval = AlarmManager.INTERVAL_DAY;
                    break;
            }

            am.setRepeating(AlarmManager.RTC_WAKEUP, timeMillis, interval, pi);
        }
    }
    private void checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                new AlertDialog.Builder(this)
                        .setTitle("Cấp quyền nhắc giờ chính xác")
                        .setMessage("Ứng dụng cần quyền đặt nhắc giờ chính xác. Vui lòng cấp quyền ở màn hình tiếp theo.")
                        .setPositiveButton("Mở cài đặt", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            startActivity(intent);
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            }
        }
    }

}


