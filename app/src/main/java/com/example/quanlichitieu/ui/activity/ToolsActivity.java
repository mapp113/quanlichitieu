package com.example.quanlichitieu.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlichitieu.R;
import android.content.Intent;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

public class ToolsActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        LinearLayout tvCurrencyConverter = findViewById(R.id.tvCurrencyConverter);
        LinearLayout tvCalculator = findViewById(R.id.tvCalculator);

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
