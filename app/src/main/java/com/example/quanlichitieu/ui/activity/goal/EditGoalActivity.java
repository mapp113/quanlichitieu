package com.example.quanlichitieu.ui.activity.goal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Goal;
import com.example.quanlichitieu.viewmodel.GoalViewModel;

import java.text.DecimalFormat;
import java.util.Calendar;

public class EditGoalActivity extends AppCompatActivity {

    private EditText editTitle, editTarget, editCurrent;
    private DatePicker datePicker;
    private Button btnUpdate, btnDelete;
    private ImageButton btnBack;
    private GoalViewModel goalViewModel;
    private Goal currentGoal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_edit_goal);

        // Ánh xạ view
        editTitle = findViewById(R.id.editGoalTitle1);
        editTarget = findViewById(R.id.editTargetAmount1);
        editCurrent = findViewById(R.id.editCurrentAmount1);
        datePicker = findViewById(R.id.datePickerDeadline1);
        btnUpdate = findViewById(R.id.btnUpdateGoal);
        btnDelete = findViewById(R.id.btnDeleteGoal);
        btnBack = findViewById(R.id.btnBack1);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // ViewModel
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        // Lấy ID
        int goalId = getIntent().getIntExtra("goalId", -1);
        if (goalId == -1) {
            Toast.makeText(this, "Không tìm thấy mục tiêu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load dữ liệu
        goalViewModel.findById(goalId).observe(this, goal -> {
            if (goal != null) {
                currentGoal = goal;
                editTitle.setText(goal.title);

//                editTarget.setText(String.valueOf(goal.targetAmount));
//                editCurrent.setText(String.valueOf(goal.currentAmount));

                DecimalFormat format = new DecimalFormat("#,###");
                editTarget.setText(format.format(goal.targetAmount));
                editCurrent.setText(format.format(goal.currentAmount));

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(goal.deadline);
                datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
        });

        // Sửa
        btnUpdate.setOnClickListener(v -> {
            if (currentGoal == null) return;

            String title = editTitle.getText().toString();
            String targetStr = editTarget.getText().toString().replace(",", "");
            String currentStr = editCurrent.getText().toString().replace(",", "");

            double target = Double.parseDouble(targetStr);
            double current = Double.parseDouble(currentStr);

            Calendar cal = Calendar.getInstance();
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            long deadline = cal.getTimeInMillis();

            if (deadline < System.currentTimeMillis()) {
                Toast.makeText(this, "Không được chọn ngày trong quá khứ!", Toast.LENGTH_SHORT).show();
                return;
            }

            Goal updatedGoal = new Goal(currentGoal.goalId, title, target, current, deadline, currentGoal.userId);

            goalViewModel.update(updatedGoal);
            Toast.makeText(this, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Xóa
        btnDelete.setOnClickListener(v -> {
            if (currentGoal != null) {
                goalViewModel.delete(currentGoal);
                Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}