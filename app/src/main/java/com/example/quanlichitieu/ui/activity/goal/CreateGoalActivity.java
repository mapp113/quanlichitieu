package com.example.quanlichitieu.ui.activity.goal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Goal;
import com.example.quanlichitieu.data.repository.GoalRepository;
import com.example.quanlichitieu.viewmodel.GoalViewModel;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateGoalActivity extends AppCompatActivity {
    private EditText editGoalTitle, editTargetAmount, editCurrentAmount;
    private DatePicker datePickerDeadline;
    private Button btnCreateGoal;
    private GoalViewModel goalViewModel;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_create_goal);

        editGoalTitle = findViewById(R.id.editGoalTitle);
        editTargetAmount = findViewById(R.id.editTargetAmount);
        editCurrentAmount = findViewById(R.id.editCurrentAmount);
        datePickerDeadline = findViewById(R.id.datePickerDeadline);
        btnCreateGoal = findViewById(R.id.btnCreateGoal);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);


        btnCreateGoal.setOnClickListener(view -> {
            String title = editGoalTitle.getText().toString().trim();
            String target = editTargetAmount.getText().toString().trim();
            String current = editCurrentAmount.getText().toString().trim();

            if (title.isEmpty() || target.isEmpty() || current.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double targetMoney = Double.parseDouble(target);
                double currentMoney = Double.parseDouble(current);

                Calendar calendar = Calendar.getInstance();
                calendar.set(datePickerDeadline.getYear(), datePickerDeadline.getMonth(), datePickerDeadline.getDayOfMonth());
                long deadline = calendar.getTimeInMillis();

                if (deadline < System.currentTimeMillis()) {
                    Toast.makeText(this, "Không được chọn ngày trong quá khứ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int userId = 1;
                Goal goal = new Goal(title, targetMoney, currentMoney, deadline, userId);

                goalViewModel.insert(goal);
                Toast.makeText(this, "Đã tạo mục tiêu!", Toast.LENGTH_SHORT).show();
                finish();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}