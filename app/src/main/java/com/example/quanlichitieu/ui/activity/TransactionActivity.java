package com.example.quanlichitieu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.ui.adapter.TransactionAdapter;
import com.example.quanlichitieu.viewmodel.TransactionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList = new ArrayList<>();
    private TextView tvTotalIncome, tvTotalExpense, tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvBalance = findViewById(R.id.tvBalance);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(transactionList,
            transaction -> {},
            transaction -> {},
            this::onEditTransaction,
            this::onDeleteTransaction
        );
        recyclerView.setAdapter(adapter);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        observeData();
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    private void observeData() {
        transactionViewModel.getAllTransactions().observe(this, transactions -> {
            transactionList = transactions != null ? transactions : new ArrayList<>();
            adapter.setItems(transactionList);
        });
        transactionViewModel.getTotalIncome().observe(this, income -> {
            Log.d("TransactionActivity", "Tổng thu: " + income);
            tvTotalIncome.setText("Tổng thu: " + (income != null ? income : 0));
            updateBalance();
        });
        transactionViewModel.getTotalExpense().observe(this, expense -> {
            Log.d("TransactionActivity", "Tổng chi: " + expense);
            tvTotalExpense.setText("Tổng chi: " + (expense != null ? expense : 0));
            updateBalance();
        });
    }

    private void updateBalance() {
        double income = 0, expense = 0;
        try { income = Double.parseDouble(tvTotalIncome.getText().toString().replaceAll("[^0-9.]", "")); } catch (Exception ignored) {}
        try { expense = Double.parseDouble(tvTotalExpense.getText().toString().replaceAll("[^0-9.]", "")); } catch (Exception ignored) {}
        tvBalance.setText("Số dư: " + (income - expense));
    }

    private void onEditTransaction(Transaction transaction) {
        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra("transaction_id", transaction.id);
        startActivityForResult(intent, 101);
    }

    private void onDeleteTransaction(Transaction transaction) {
        transactionViewModel.delete(transaction);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Không cần gọi lại observeData() ở đây vì LiveData đã tự động cập nhật
    }
}