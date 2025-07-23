package com.example.quanlichitieu.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.SessionManager;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.User;
import com.example.quanlichitieu.data.local.utils.PasswordUtils;
import com.example.quanlichitieu.ui.activity.goal.GoalActivity;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        registerTextView = findViewById(R.id.signupText);
        loginButton = findViewById(R.id.loginBtn);

        registerTextView.setOnClickListener(view -> {
            // Chuyển sang màn hình đăng ký
            startActivity(new Intent(this, RegisterActivity.class));
        });

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chạy DB query trong luồng nền
            new Thread(() -> {
                User user = appDatabase.getInstance(this).userDao().getUserByUsername(username);

                runOnUiThread(() -> {
                    if (user == null) {
                        Toast.makeText(this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        String hashedInput = PasswordUtils.hashPassword(password, user.salt);
                        if (hashedInput.equals(user.password)) {
                            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            //save session and go to next tab
                            SessionManager.saveUserId(this,user.userId);
                            Intent intent = new Intent(this, TransactionActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }).start();
        });
    }
}
