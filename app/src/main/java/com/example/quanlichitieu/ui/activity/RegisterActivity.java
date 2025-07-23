package com.example.quanlichitieu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.User;
import com.example.quanlichitieu.data.local.utils.PasswordUtils;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText, fullNameEditText, confirmPasswordEditText;
    Button registerButton;
    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        fullNameEditText = findViewById(R.id.fullname);
        confirmPasswordEditText = findViewById(R.id.confirmpassword);
        registerButton = findViewById(R.id.RegisterBtn);

        loginTextView = findViewById(R.id.signinText);
        loginTextView.setOnClickListener(view -> {
            // Chuyển sang màn hình đăng nhập
            startActivity(new Intent(this, LoginActivity.class));
        });

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String fullName = fullNameEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            //kiểm tra xem password và confirmPassword có giống nhau không
            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Mật khẩu không khớp");
                return;
            }

            //kiểm tra xem username đã tồn tại chưa
            User existingUser = appDatabase.getInstance(this).userDao().getUserByUsername(username);
            if (existingUser != null) {
                Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            } else if (PasswordUtils.checkUserInfoValid(password, username, fullName)) {
                // Chạy DB query trong luồng nền
                new Thread(() -> {
                    String salt = PasswordUtils.generateSalt();
                    String hashedPassword = PasswordUtils.hashPassword(password, salt);
                    appDatabase.getInstance(this).userDao().insertUser(new User(username, hashedPassword,fullName,"",System.currentTimeMillis() ,salt));
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        // TODO: Chuyển sang màn hình chính
                    });
                }).start();
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

    }
}