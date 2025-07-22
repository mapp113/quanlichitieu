package com.example.quanlichitieu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.data.local.entity.Type;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>{
    public interface OnItemClick {
        void onClick(Transaction transaction);
    }
    public interface OnItemLongClick {
        void onLongClick(Transaction transaction);
    }

    public interface OnEditClick {
        void onEdit(Transaction transaction);
    }
    public interface OnDeleteClick {
        void onDelete(Transaction transaction);
    }
    private List<Transaction> items;
    private OnItemClick onClick;
    private OnItemLongClick onLongClick;
    private OnEditClick onEditClick;
    private OnDeleteClick onDeleteClick;
    public TransactionAdapter(List<Transaction> items, OnItemClick onClick, OnItemLongClick onLongClick,
                              OnEditClick onEditClick, OnDeleteClick onDeleteClick) {
        this.items = items;
        this.onClick = onClick;
        this.onLongClick = onLongClick;
        this.onEditClick = onEditClick;
        this.onDeleteClick = onDeleteClick;
    }
    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        Transaction transaction = items.get(position);
        holder.tvTitle.setText(transaction.title);
        holder.tvAmount.setText((transaction.type != null && transaction.type == Type.INCOME ? "+" : "-") + transaction.amount);
        holder.tvAmount.setTextColor(transaction.type != null && transaction.type == Type.INCOME ? 0xFF4CAF50 : 0xFFF44336);
        holder.tvDate.setText(transaction.date);
//        holder.tvCategory.setText(String.valueOf(transaction.category));
        holder.tvAddress.setText(String.valueOf(transaction.address));
        holder.btnEdit.setOnClickListener(v -> {
            if (onEditClick != null) onEditClick.onEdit(transaction);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClick != null) onDeleteClick.onDelete(transaction);
        });
        holder.itemView.setOnClickListener(v -> {
            if (onClick != null) onClick.onClick(transaction);
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (onLongClick != null) onLongClick.onLongClick(transaction);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAmount, tvDate, tvCategory, tvAddress;
        ImageButton btnEdit, btnDelete;
        ImageView ivType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
//            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ivType = itemView.findViewById(R.id.ivType);
        }
    }

    public void setItems(List<Transaction> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }
}
