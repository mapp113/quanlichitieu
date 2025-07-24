package com.example.quanlichitieu.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlichitieu.R;
import android.content.Intent;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

public class ToolsActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tools);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        LinearLayout tvCurrencyConverter = findViewById(R.id.tvCurrencyConverter);
        LinearLayout tvCalculator = findViewById(R.id.tvCalculator);
        LinearLayout tvShoppingList = findViewById(R.id.tvShoppingList);

        // Chuyển sang màn hình chuyển đổi ngoại tệ
        tvCurrencyConverter.setOnClickListener(v -> {
            Intent intent = new Intent(ToolsActivity.this, CurrencyConverterActivity.class);
            startActivity(intent);
        });
        // Chuyển sang màn hình danh sách mua sắm
        tvShoppingList.setOnClickListener(v -> {
            Intent intent = new Intent(ToolsActivity.this, ShoppingListActivity.class);
            startActivity(intent);
        });

        LinearLayout reminderLayout = findViewById(R.id.reminder);
        reminderLayout.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
        });


        // Chuyển sang màn hình máy tính
//        tvCalculator.setOnClickListener(v -> {
//            Intent intent = new Intent(ToolsActivity.this, CalculatorActivity.class);
//            startActivity(intent);
//        });
    }
}
