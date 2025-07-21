package com.example.quanlichitieu.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {

    private EditText etAmount, etDesc, etDate;
    private RadioButton rbIncome, rbExpense;
    private Button btnSave;

    private Transaction transaction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        etAmount = findViewById(R.id.etAmount);
        etDesc = findViewById(R.id.etDesc);
        etDate = findViewById(R.id.etDate);
        rbIncome = findViewById(R.id.rbIncome);
        rbExpense = findViewById(R.id.rbExpense);
        btnSave = findViewById(R.id.btnSave);

        transaction = (Transaction) getIntent().getSerializableExtra("item");

        if (transaction != null) {
            etAmount.setText(String.valueOf(transaction.getAmount()));
            etDesc.setText(transaction.getDescription());
            etDate.setText(transaction.getDate());
            etDate.setEnabled(true); // Cho phép sửa ngày khi edit
            if ("income".equals(transaction.getType())) rbIncome.setChecked(true);
            else rbExpense.setChecked(true);
        } else {
            // Thêm mới: tự động lấy ngày hiện tại, không cho sửa
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            etDate.setText(currentDate);
            etDate.setEnabled(false);
        }

        btnSave.setOnClickListener(v -> {
            String type = rbIncome.isChecked() ? "income" : "expense";
            double amount = Double.parseDouble(etAmount.getText().toString());
            String desc = etDesc.getText().toString();
            String date = etDate.getText().toString();

            new Thread(() -> {
                if (transaction == null) {
                    appDatabase.getDatabase(this).transactionDao().insert(
                            new Transaction(type, amount, desc, date)
                    );
                } else {
                    transaction.setType(type);
                    transaction.setAmount(amount);
                    transaction.setDescription(desc);
                    transaction.setDate(date);
                    appDatabase.getDatabase(this).transactionDao().update(transaction);
                }
                runOnUiThread(this::finish);
            }).start();
        });

    };
}