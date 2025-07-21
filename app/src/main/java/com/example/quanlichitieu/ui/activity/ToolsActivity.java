package com.example.quanlichitieu.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlichitieu.R;
import android.content.Intent;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

public class ToolsActivity  extends AppCompatActivity {
    private MaterialTextView tvCurrencyConverter;
    private MaterialTextView tvCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvCurrencyConverter = findViewById(R.id.tvCurrencyConverter);
        tvCalculator = findViewById(R.id.tvCalculator);

        // Chuyển sang màn hình chuyển đổi ngoại tệ
        tvCurrencyConverter.setOnClickListener(v -> {
            Intent intent = new Intent(ToolsActivity.this, CurrencyConverterActivity.class);
            startActivity(intent);
        });

        // Chuyển sang màn hình máy tính
//        tvCalculator.setOnClickListener(v -> {
//            Intent intent = new Intent(ToolsActivity.this, CalculatorActivity.class);
//            startActivity(intent);
//        });
    }
}
