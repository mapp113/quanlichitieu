package com.example.quanlichitieu.ui.activity.goal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Goal;
import com.example.quanlichitieu.ui.adapter.GoalAdapter;
import com.example.quanlichitieu.viewmodel.GoalViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListGoalActivity extends AppCompatActivity {
    private GoalViewModel goalViewModel;
    private RecyclerView recyclerView;
    private GoalAdapter adapter;

    private Button btnCurrent, btnDone, btnAll, btnCreateGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_list_goal);

        recyclerView = findViewById(R.id.GoalList);
        btnCurrent = findViewById(R.id.btnFilterCurrent);
        btnDone = findViewById(R.id.btnFilterDone);
        btnAll = findViewById(R.id.btnFilterAll);
        btnCreateGoal = findViewById(R.id.btnToCreateGoal);

        adapter = new GoalAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        // 🔰 Mặc định: hiển thị tất cả
        observeGoals(goalViewModel.getAllGoals());

        btnAll.setOnClickListener(v -> observeGoals(goalViewModel.getAllGoals()));
        btnCurrent.setOnClickListener(v -> observeGoals(goalViewModel.getCurrentGoals()));
        btnDone.setOnClickListener(v -> observeGoals(goalViewModel.getDoneGoals()));

        btnCreateGoal.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateGoalActivity.class);
            startActivity(intent);
        });
    }

    private void observeGoals(LiveData<List<Goal>> goalsLiveData) {
        // ⚠️ Cần remove previous observers nếu không sẽ bị gọi lặp
        goalsLiveData.removeObservers(this);

        goalsLiveData.observe(this, goals -> {
            adapter.setGoals(goals); // Bạn cần có phương thức setGoals mới trong Adapter
        });
    }
}