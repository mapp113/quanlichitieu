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
import java.util.stream.Collectors;

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
        txtMonthYear.setText(selectedDate.format(DateTimeFormatter.ofPattern("MM/yyyy")));

        // Giả sử bạn lấy dữ liệu từ database:
        List<Transaction> all = TransactionDatabase.getInstance(getContext()).transactionDao().getAll();

        // Lọc theo tháng
        List<Transaction> monthTransactions = all.stream()
                .filter(t -> {
                    LocalDate transactionDate = Instant.ofEpochMilli(t.date)  // long → Instant
                            .atZone(ZoneId.systemDefault())                       // xét theo múi giờ hệ thống
                            .toLocalDate();                                       // Instant → LocalDate

                    return YearMonth.from(transactionDate)
                            .equals(YearMonth.from(selectedDate));
                })
                .collect(Collectors.toList());


        double totalIncome = 0, totalExpense = 0;
        Map<Integer, Double> categoryMap = new HashMap<>();

        for (Transaction t : monthTransactions) {
            if (t.type== Type.INCOME) {
                totalIncome +=  t.amount;
            } else {
                totalExpense += t.amount;
                Double currentObj = categoryMap.get(t.category);
                double current = currentObj != null ? currentObj : 0.0;
                categoryMap.put(t.category, current + t.amount);
            }
        }

        txtTotalIncome.setText("+" + totalIncome + "đ");
        txtTotalExpense.setText("-" + totalExpense + "đ");
        txtNet.setText((totalIncome - totalExpense) + "đ");

        // Biểu đồ
        pieChart.setUsePercentValues(true);
        List<PieEntry> entries = new ArrayList<>();
        categorySummaries.clear();

        for (Map.Entry<Integer, Double> entry : categoryMap.entrySet()) {
            double percent = (entry.getValue() * 100f) / totalExpense;
            Category category = TransactionDatabase.getInstance(getContext())
                    .categoryDao()
                    .findById(entry.getKey());
            String categoryName = category != null ? category.getName() : "Không rõ";

            categorySummaries.add(new CategorySummary(categoryName, entry.getValue(), percent));
            entries.add(new PieEntry( entry.getValue().floatValue(), categoryName));

        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.invalidate();

        adapter.notifyDataSetChanged();
    }
}
