package com.example.quanlichitieu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.CategorySummary;

import java.util.List;

public class CategorySummaryAdapter extends RecyclerView.Adapter<CategorySummaryAdapter.ViewHolder> {
    private List<CategorySummary> data;

    public CategorySummaryAdapter(List<CategorySummary> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtAmount, txtPercent;
        ImageView iconCategory;

        public ViewHolder(View view) {
            super(view);
            txtCategory = view.findViewById(R.id.txtCategory);
            txtAmount = view.findViewById(R.id.txtAmount);
            txtPercent = view.findViewById(R.id.txtPercent);
            iconCategory = view.findViewById(R.id.iconCategory);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategorySummary item = data.get(position);
        holder.txtCategory.setText(item.getCategoryName());
        holder.txtAmount.setText(item.getAmount() + "đ");
        holder.txtPercent.setText(String.format("%.1f%%", item.getPercent()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
