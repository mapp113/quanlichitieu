package com.example.quanlichitieu.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Category;
import com.example.quanlichitieu.viewmodel.CategoryViewModel;

public class AddCategoryActivity extends AppCompatActivity {
    private EditText editCategoryName;
    private Button btnSaveCategory;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        editCategoryName = findViewById(R.id.editCategoryName);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        btnSaveCategory.setOnClickListener(v -> {
            String name = editCategoryName.getText().toString().trim();
            if (!name.isEmpty()) {
                categoryViewModel.insert(new Category(name));
                finish();
            } else {
                Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
