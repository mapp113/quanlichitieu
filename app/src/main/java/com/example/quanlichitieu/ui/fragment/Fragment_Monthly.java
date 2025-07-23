package com.example.quanlichitieu.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.Category;
import com.example.quanlichitieu.data.local.entity.CategorySummary;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.data.local.entity.Type;
import com.example.quanlichitieu.ui.adapter.CategorySummaryAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_Monthly extends Fragment {
    private TextView txtMonthYear, txtTotalExpense, txtTotalIncome, txtNet;
    private PieChart pieChart;
    private RecyclerView recyclerView;
    private CategorySummaryAdapter adapter;

    private List<CategorySummary> categorySummaries = new ArrayList<>();


    private LocalDate selectedDate = LocalDate.now(); // Requires API 26+

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__monthly, container, false);
        initViews(view);
        loadData();
        return view;
    }

    private void initViews(View view) {
        appDatabase db = appDatabase.getInstance(getContext());

        txtMonthYear = view.findViewById(R.id.txtMonthYear);
        txtTotalExpense = view.findViewById(R.id.txtTotalExpense);
        txtTotalIncome = view.findViewById(R.id.txtTotalIncome);
        txtNet = view.findViewById(R.id.txtNet);
        pieChart = view.findViewById(R.id.pieChart);
        recyclerView = view.findViewById(R.id.recyclerCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategorySummaryAdapter(categorySummaries);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btnPrevMonth).setOnClickListener(v -> {
            selectedDate = selectedDate.minusMonths(1);
            loadData();
        });

        view.findViewById(R.id.btnNextMonth).setOnClickListener(v -> {
            selectedDate = selectedDate.plusMonths(1);
            loadData();
        });
    }

    private void loadData() {
        appDatabase db = appDatabase.getInstance(getContext());
        db.categoryDao().getAll().observe(getViewLifecycleOwner(), categories -> {
            Map<Integer, String> categoryNameMap = new HashMap<>();
            for (Category c : categories) {
                categoryNameMap.put(c.getId(), c.getName()); // hoặc c.getId(), c.getName() tùy getter bạn có
            }

            db.transactionDao().getAll().observe(getViewLifecycleOwner(), transactions -> {
                if (transactions == null) return;

                // Lọc theo tháng
                List<Transaction> monthTransactions = new ArrayList<>();
                for (Transaction t : transactions) {
                    LocalDate transactionDate = Instant.ofEpochMilli(Long.parseLong(t.date))
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    if (YearMonth.from(transactionDate).equals(YearMonth.from(selectedDate))) {
                        monthTransactions.add(t);
                    }
                }

                // Thống kê
                double totalIncome = 0, totalExpense = 0;
                Map<Integer, Double> categoryMap = new HashMap<>();

                for (Transaction t : monthTransactions) {
                    if (t.type == Type.INCOME) {
                        totalIncome += t.amount;
                    } else {
                        totalExpense += t.amount;
                        double current = categoryMap.getOrDefault(t.categoryId, 0.0);
                        categoryMap.put(t.categoryId, current + t.amount);
                    }
                }

                txtTotalIncome.setText("+" + totalIncome + "đ");
                txtTotalExpense.setText("-" + totalExpense + "đ");
                txtNet.setText((totalIncome - totalExpense) + "đ");

                // Pie chart
                pieChart.setUsePercentValues(true);
                List<PieEntry> entries = new ArrayList<>();
                categorySummaries.clear();

                for (Map.Entry<Integer, Double> entry : categoryMap.entrySet()) {
                    int categoryId = entry.getKey();
                    double amount = entry.getValue();
                    double percent = (amount * 100f) / totalExpense;

                    String categoryName = categoryNameMap.getOrDefault(categoryId, "Khác");

                    categorySummaries.add(new CategorySummary(categoryName, amount, percent));
                    entries.add(new PieEntry((float) amount, categoryName));
                }

                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                PieData pieData = new PieData(dataSet);
                pieData.setValueTextSize(12f);
                pieData.setValueTextColor(Color.WHITE);

                pieChart.setData(pieData);
                pieChart.invalidate();
                adapter.notifyDataSetChanged();
            });
        });

    }

}
