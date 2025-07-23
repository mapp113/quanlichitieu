package com.example.quanlichitieu.ui.activity.goal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Goal;
import com.example.quanlichitieu.data.local.utils.MenuHandler;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.goal_list_goal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MenuHandler.handleMenuClick(this, item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}