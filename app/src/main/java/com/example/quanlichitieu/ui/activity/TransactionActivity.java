package com.example.quanlichitieu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.data.local.utils.MenuHandler;
import com.example.quanlichitieu.ui.adapter.TransactionAdapter;
import com.example.quanlichitieu.viewmodel.TransactionViewModel;
import com.example.quanlichitieu.data.local.entity.Category;
import com.example.quanlichitieu.viewmodel.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private CategoryViewModel categoryViewModel;
    private TextView tvTotalIncome, tvTotalExpense, tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        Button swap = findViewById(R.id.buttonSwap);
        swap.setOnClickListener(view -> {
            startActivity(new Intent(this, AddEditActivity.class));
        });
        tvBalance = findViewById(R.id.tvBalance);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList = categories != null ? categories : new ArrayList<>();
            // Khởi tạo adapter khi đã có categoryList
            adapter = new TransactionAdapter(transactionList, categoryList,
                transaction -> {},
                transaction -> {},
                this::onEditTransaction,
                this::onDeleteTransaction
            );
            recyclerView.setAdapter(adapter);
            observeData();
        });
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCategoryActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    private void observeData() {
        transactionViewModel.getAllTransactions().observe(this, transactions -> {
            transactionList = transactions != null ? transactions : new ArrayList<>();
            if (adapter != null) adapter.setItems(transactionList);
        });
        transactionViewModel.getTotalIncome().observe(this, income -> {
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String incomeStr = formatter.format(income != null ? income : 0) + "đ";
            tvTotalIncome.setText("Tổng thu: " + incomeStr);
            updateBalance();
        });
        transactionViewModel.getTotalExpense().observe(this, expense -> {
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String expenseStr = formatter.format(expense != null ? expense : 0) + "đ";
            tvTotalExpense.setText("Tổng chi: " + expenseStr);
            updateBalance();
        });
    }

    private void updateBalance() {
        double income = 0, expense = 0;
        try { income = Double.parseDouble(tvTotalIncome.getText().toString().replaceAll("[^0-9.-]", "")); } catch (Exception ignored) {}
        try { expense = Double.parseDouble(tvTotalExpense.getText().toString().replaceAll("[^0-9.-]", "")); } catch (Exception ignored) {}
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String balanceStr = formatter.format(income - expense) + "đ";
        tvBalance.setText("Số dư: " + balanceStr);
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