package com.example.quanlichitieu.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Category;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.data.local.entity.Type;
import com.example.quanlichitieu.ui.adapter.TransactionAdapter;
import com.example.quanlichitieu.viewmodel.CategoryViewModel;
import com.example.quanlichitieu.viewmodel.TransactionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionRealActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private CategoryViewModel categoryViewModel;

    private RecyclerView recyclerCategory;
    private TextView txtMonthYear, txtTotalExpense, txtTotalIncome, txtNet;
    private TextView tabExpense, tabIncome;
    Button dayTab,monthTab;
    private PieChart pieChart;
    private List<Transaction> allTransactions = new ArrayList<>();
    private List<Category> allCategories = new ArrayList<>();
    private TransactionAdapter adapter;

    private boolean isExpenseSelected = true;
    private boolean isDay = true;
    private LocalDate selectedDate = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment__monthly); // layout XML dùng chung fragment

        initViews();
        setupViewModels();
        setupRecyclerView();
        setupListeners();
        observeData();
    }

    private void initViews() {
        recyclerCategory = findViewById(R.id.recyclerCategory);
        txtMonthYear = findViewById(R.id.txtMonthYear);
        txtTotalExpense = findViewById(R.id.txtTotalExpense);
        txtTotalIncome = findViewById(R.id.txtTotalIncome);
        txtNet = findViewById(R.id.txtNet);
        tabExpense = findViewById(R.id.tabExpense);
        tabIncome = findViewById(R.id.tabIncome);
        pieChart = findViewById(R.id.pieChart);
        dayTab=findViewById(R.id.Daytab);
        monthTab=findViewById(R.id.MonthTab);
        updateMonthYearLabel();
    }

    private void setupViewModels() {
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    private void setupRecyclerView() {
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(new ArrayList<>(), allCategories, null, null, null, null);
        recyclerCategory.setAdapter(adapter);
    }

    private void setupListeners() {
        findViewById(R.id.btnPrevMonth).setOnClickListener(v -> {
            selectedDate = isDay ? selectedDate.minusDays(1) : selectedDate.minusMonths(1);
            updateMonthYearLabel();
            updateUI();
        });

        findViewById(R.id.btnNextMonth).setOnClickListener(v -> {
            selectedDate = isDay ? selectedDate.plusDays(1) : selectedDate.plusMonths(1);
            updateMonthYearLabel();
            updateUI();
        });

        tabExpense.setOnClickListener(v -> {
            isExpenseSelected = true;
            updateTabHighlight();
            updateUI();
        });

        tabIncome.setOnClickListener(v -> {
            isExpenseSelected = false;
            updateTabHighlight();
            updateUI();
        });
        dayTab.setOnClickListener(view -> {
            isDay=true;
            updateTabDay();
            updateMonthYearLabel();
            updateUI();
            Toast.makeText(this,isDay+" is",Toast.LENGTH_SHORT).show();
        });
        monthTab.setOnClickListener(view -> {
            isDay=false;
            updateTabDay();
            updateMonthYearLabel();
            updateUI();
            Toast.makeText(this,isDay+" is",Toast.LENGTH_SHORT).show();
        });
    }

    private void updateTabHighlight() {
        tabExpense.setBackgroundResource(isExpenseSelected ? R.drawable.selected_tab_background : R.drawable.unselected_tab_background);
        tabIncome.setBackgroundResource(!isExpenseSelected ? R.drawable.selected_tab_background : R.drawable.unselected_tab_background);
    }
    private void updateTabDay() {
        int orangeColor = ContextCompat.getColor(this, R.color.orange);
        int purpleColor = ContextCompat.getColor(this, R.color.purple);
        dayTab.setBackgroundColor(isDay ? orangeColor : purpleColor);
        monthTab.setBackgroundColor(!isDay ? orangeColor : purpleColor);
    }

    private void updateMonthYearLabel() {
        if (isDay) {
            txtMonthYear.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            txtMonthYear.setText(selectedDate.format(DateTimeFormatter.ofPattern("MM/yyyy")));
        }
    }


    private void observeData() {
        categoryViewModel.getAllCategories().observe(this, categories -> {
            allCategories = categories != null ? categories : new ArrayList<>();
            updateUI();
        });

        transactionViewModel.getAllTransactions().observe(this, transactions -> {
            allTransactions = transactions != null ? transactions : new ArrayList<>();
            updateUI();
        });
    }

    private void updateUI() {
        List<Transaction> filtered = new ArrayList<>();
        double total = 0;

        for (Transaction t : allTransactions) {
            if (t.date <= 0) continue;

            LocalDate transactionDate = Instant.ofEpochMilli(t.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            boolean sameMonth = transactionDate.getYear() == selectedDate.getYear()
                    && transactionDate.getMonthValue() == selectedDate.getMonthValue();

            boolean sameDay = transactionDate.equals(selectedDate);

            if ((isDay && sameDay) || (!isDay && sameMonth)) {
                if (isExpenseSelected && t.type == Type.EXPENSE) {
                    filtered.add(t);
                    total += t.amount;
                } else if (!isExpenseSelected && t.type == Type.INCOME) {
                    filtered.add(t);
                    total += t.amount;
                }
            }
        }

        adapter.setItems(filtered);
        updatePieChart(filtered);
        updateSummary();  // vẫn cập nhật tổng thu/chi để hiển thị ở đầu
    }


    private void updatePieChart(List<Transaction> transactions) {
        List<PieEntry> entries = new ArrayList<>();

        for (Transaction t : transactions) {
            Category category = findCategoryById(t.categoryId);
            if (category != null) {
                entries.add(new PieEntry((float) Math.abs(t.amount), category.getName()));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        pieChart.setData(new PieData(dataSet));
        pieChart.invalidate();
    }

    private void updateSummary() {
        double totalIncome = 0, totalExpense = 0;

        for (Transaction t : allTransactions) {
            if (t.date <= 0) continue;

            LocalDate transactionDate = Instant.ofEpochMilli(t.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            boolean match = isDay
                    ? transactionDate.equals(selectedDate)
                    : transactionDate.getYear() == selectedDate.getYear()
                    && transactionDate.getMonthValue() == selectedDate.getMonthValue();

            if (match) {
                if (t.type == Type.INCOME) totalIncome += t.amount;
                else if (t.type == Type.EXPENSE) totalExpense += t.amount;
            }
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        txtTotalIncome.setText("+" + formatter.format(totalIncome) + "đ");
        txtTotalExpense.setText("-" + formatter.format(totalExpense) + "đ");
        double net = totalIncome - totalExpense;
        txtNet.setText((net >= 0 ? "+" : "") + formatter.format(net) + "đ");
    }


    private Category findCategoryById(int categoryId) {
        for (Category c : allCategories) {
            if (c.getId() == categoryId) return c;
        }
        return null;
    }
}