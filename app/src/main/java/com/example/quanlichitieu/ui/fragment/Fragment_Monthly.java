package com.example.quanlichitieu.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.CategorySummary;
import com.example.quanlichitieu.ui.adapter.CategorySummaryAdapter;
import com.example.quanlichitieu.viewmodel.TransactionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Monthly extends Fragment {
    private TextView txtMonthYear, txtTotalExpense, txtTotalIncome, txtNet;
    private TextView tabExpense, tabIncome;
    private PieChart pieChart;
    private RecyclerView recyclerView;
    private CategorySummaryAdapter adapter;

    private LocalDate selectedDate = LocalDate.now();
    private boolean isDailyMode = false;
    private String selectedType = "EXPENSE"; // hoặc "INCOME"

    private TransactionViewModel transactionViewModel;
    private List<CategorySummary> categorySummaries = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__monthly, container, false);
        initViews(view);
        setupViewModel();
        loadData();
        return view;
    }

    private void initViews(View view) {
        txtMonthYear = view.findViewById(R.id.txtMonthYear);
        txtTotalExpense = view.findViewById(R.id.txtTotalExpense);
        txtTotalIncome = view.findViewById(R.id.txtTotalIncome);
        txtNet = view.findViewById(R.id.txtNet);

        tabExpense = view.findViewById(R.id.tabExpense);
        tabIncome = view.findViewById(R.id.tabIncome);

        pieChart = view.findViewById(R.id.pieChart);
        recyclerView = view.findViewById(R.id.recyclerCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategorySummaryAdapter(categorySummaries);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btnPrevMonth).setOnClickListener(v -> {
            selectedDate = selectedDate.minusDays(isDailyMode ? 1 : 30);
            loadData();
        });

        view.findViewById(R.id.btnNextMonth).setOnClickListener(v -> {
            selectedDate = selectedDate.plusDays(isDailyMode ? 1 : 30);
            loadData();
        });

        // Tab chọn theo ngày / tháng
        view.findViewById(R.id.DayTab).setOnClickListener(v -> {
            isDailyMode = true;
            loadData();
        });

        view.findViewById(R.id.MonthTab).setOnClickListener(v -> {
            isDailyMode = false;
            loadData();
        });

        // Tab chọn chi / thu
        tabExpense.setOnClickListener(v -> {
            selectedType = "EXPENSE";
            updateTypeTabs();
            loadData();
        });

        tabIncome.setOnClickListener(v -> {
            selectedType = "INCOME";
            updateTypeTabs();
            loadData();
        });
    }

    private void setupViewModel() {
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
    }

    private void updateTypeTabs() {
        if (selectedType.equals("EXPENSE")) {
            tabExpense.setTextColor(getResources().getColor(R.color.orange));
            tabIncome.setTextColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            tabIncome.setTextColor(getResources().getColor(R.color.orange));
            tabExpense.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void loadData() {
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

        String formattedDate = isDailyMode
                ? selectedDate.format(dayFormatter)
                : selectedDate.format(monthFormatter);

        txtMonthYear.setText(isDailyMode
                ? selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : selectedDate.format(DateTimeFormatter.ofPattern("MM/yyyy")));

        LiveData<List<CategorySummary>> liveData = isDailyMode
                ? transactionViewModel.getCategorySummariesByDay(selectedType, formattedDate)
                : transactionViewModel.getCategorySummariesByMonth(selectedType, formattedDate);

        liveData.observe(getViewLifecycleOwner(), summaries -> {
            if (summaries == null) return;

            categorySummaries.clear();
            categorySummaries.addAll(summaries);
            adapter.notifyDataSetChanged();

            updatePieChart(summaries);
            updateTotals();
        });
    }

    private void updatePieChart(List<CategorySummary> summaries) {
        List<PieEntry> entries = new ArrayList<>();
        for (CategorySummary cs : summaries) {
            entries.add(new PieEntry((float) cs.getAmount(), cs.getCategoryName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void updateTotals() {
        double total = 0;
        for (CategorySummary cs : categorySummaries) {
            total += cs.getAmount();
        }

        if (selectedType.equals("EXPENSE")) {
            txtTotalExpense.setText("-" + total + "đ");
            txtTotalIncome.setText("+0đ");
            txtNet.setText("-" + total + "đ");
        } else {
            txtTotalIncome.setText("+" + total + "đ");
            txtTotalExpense.setText("-0đ");
            txtNet.setText("+" + total + "đ");
        }
    }
}
