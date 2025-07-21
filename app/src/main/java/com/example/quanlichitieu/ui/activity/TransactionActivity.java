package com.example.quanlichitieu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.ui.adapter.TransactionAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvTotalIncome, tvTotalExpense, tvBalance;
    private FloatingActionButton fabAdd;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        recyclerView = findViewById(R.id.recyclerView);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvBalance = findViewById(R.id.tvBalance);
        fabAdd = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, AddEditActivity.class));
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
    private void loadData() {
        new Thread(() -> {
            List<Transaction> list = appDatabase.getDatabase(this).transactionDao().getAll();
            double income = appDatabase.getDatabase(this).transactionDao().getTotalIncome();
            double expense = appDatabase.getDatabase(this).transactionDao().getTotalExpense();

            runOnUiThread(() -> {
                adapter = new TransactionAdapter(list,
                    transaction -> {
                        Intent intent = new Intent(this, AddEditActivity.class);
                        intent.putExtra("item", transaction);
                        startActivity(intent);
                    },
                    transaction -> {
                    },
                    transaction -> {
                        // onEditClick: mở màn hình sửa
                        Intent intent = new Intent(this, AddEditActivity.class);
                        intent.putExtra("item", transaction);
                        startActivity(intent);
                    },
                    transaction -> {
                        // onDeleteClick: xác nhận xoá
                        new AlertDialog.Builder(this)
                                .setTitle("Xoá giao dịch")
                                .setMessage("Bạn có chắc chắn muốn xoá không?")
                                .setPositiveButton("Xoá", (dialog, which) -> {
                                    new Thread(() -> {
                                        appDatabase.getDatabase(this).transactionDao().delete(transaction);
                                        runOnUiThread(this::loadData);
                                    }).start();
                                })
                                .setNegativeButton("Huỷ", null)
                                .show();
                    }
                );
                recyclerView.setAdapter(adapter);

                tvTotalIncome.setText("Tổng thu: " + income + " đ");
                tvTotalExpense.setText("Tổng chi: " + expense + " đ");
                tvBalance.setText("Số dư: " + (income - expense) + " đ");
            });
        }).start();
    }
}