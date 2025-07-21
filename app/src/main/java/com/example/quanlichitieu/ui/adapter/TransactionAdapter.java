package com.example.quanlichitieu.ui.adapter;

import android.view.ViewGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDesc, tvAmount, tvDate;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(View view) {
            super(view);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvDate = view.findViewById(R.id.tvDate);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);

            view.setOnClickListener(v -> onClick.onClick(items.get(getAdapterPosition())));
            view.setOnLongClickListener(v -> {
                onLongClick.onLongClick(items.get(getAdapterPosition()));
                return true;
            });
            btnEdit.setOnClickListener(v -> onEditClick.onEdit(items.get(getAdapterPosition())));
            btnDelete.setOnClickListener(v -> onDeleteClick.onDelete(items.get(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction item = items.get(position);
        holder.tvDesc.setText(item.getDescription());
        holder.tvAmount.setText((item.getType().equals("income") ? "+" : "-") + item.getAmount() + " đ");
        holder.tvDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}