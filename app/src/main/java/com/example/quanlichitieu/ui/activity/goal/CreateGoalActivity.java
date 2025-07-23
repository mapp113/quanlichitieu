package com.example.quanlichitieu.ui.activity.goal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlichitieu.R;

public class CreateGoalActivity extends AppCompatActivity {
    private EditText editGoalTitle, editTargetAmount, editCurrentAmount;
    private DatePicker datePickerDeadline;
    private Button btnCreateGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_create_goal);

        editGoalTitle = findViewById(R.id.editGoalTitle);
        editTargetAmount = findViewById(R.id.editTargetAmount);
        editCurrentAmount = findViewById(R.id.editCurrentAmount);
        datePickerDeadline = findViewById(R.id.datePickerDeadline);
        btnCreateGoal = findViewById(R.id.btnCreateGoal);
    }
}