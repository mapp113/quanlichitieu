package com.example.quanlichitieu.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Toolbar xử lý nút back
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            // Xoá session
            SessionManager.clearSession(this);

            // Quay về màn hình đăng nhập
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        TextView settingsTheme = findViewById(R.id.settingsTheme);
        // Giao diện - xử lý sự kiện click
        settingsTheme.setOnClickListener(v -> showThemeDialog());
    }

    private void showThemeDialog() {
        final String[] themes = {"Sáng", "Tối", "Theo thiết bị"};
        int currentThemeIndex = getCurrentThemeIndex(); // 0: Light, 1: Dark, 2: Follow System

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn giao diện").setSingleChoiceItems(themes, currentThemeIndex, null).setPositiveButton("Xác nhận", (dialog, which) -> {
            AlertDialog alert = (AlertDialog) dialog;
            int selectedPosition = alert.getListView().getCheckedItemPosition();
            applyTheme(selectedPosition);
        }).setNegativeButton("Hủy", null).show();
    }

    private void applyTheme(int selectedIndex) {
        int mode;
        switch (selectedIndex) {
            case 0:
                mode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case 1:
                mode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case 2:
            default:
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        prefs.edit().putInt("theme_mode", mode).apply();
        recreate();
        Toast.makeText(this, "Đã thay đổi giao diện", Toast.LENGTH_SHORT).show();
    }

    private int getCurrentThemeIndex() {
        int mode = AppCompatDelegate.getDefaultNightMode();

        switch (mode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                return 0;
            case AppCompatDelegate.MODE_NIGHT_YES:
                return 1;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
            default:
                return 2;
        }
    }

}
