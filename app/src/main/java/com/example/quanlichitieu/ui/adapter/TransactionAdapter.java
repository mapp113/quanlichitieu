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
import com.example.quanlichitieu.data.local.entity.Category;

import java.util.List;
import android.location.Address;
import android.location.Geocoder;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.NumberFormat;

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
    private List<Category> categoryList;
    private OnItemClick onClick;
    private OnItemLongClick onLongClick;
    private OnEditClick onEditClick;
    private OnDeleteClick onDeleteClick;
    public TransactionAdapter(List<Transaction> items, List<Category> categoryList, OnItemClick onClick, OnItemLongClick onLongClick,
                              OnEditClick onEditClick, OnDeleteClick onDeleteClick) {
        this.items = items;
        this.categoryList = categoryList;
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
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String amountStr = formatter.format(transaction.amount) + "đ";
        holder.tvAmount.setText((transaction.type != null && transaction.type == Type.INCOME ? "+" : "-") + amountStr);
        holder.tvAmount.setTextColor(transaction.type != null && transaction.type == Type.INCOME ? 0xFF4CAF50 : 0xFFF44336);
        // Format ngày hiển thị
        String formattedDate = transaction.date;
        try {
            Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(transaction.date);
            formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(parsed);
        } catch (Exception ignored) {}
        holder.tvDate.setText("Ngày: "+formattedDate);
        String categoryName = "";
        if (categoryList != null && transaction.categoryId != null) {
            for (Category c : categoryList) {
                if (c.getId() == transaction.categoryId) {
                    categoryName = c.getName();
                    break;
                }
            }
        }
        holder.tvCategory.setText("Thể loại: " + categoryName);
        if (transaction.address != null && transaction.address.contains(",")) {
            String[] parts = transaction.address.split(",");
            try {
                double lat = Double.parseDouble(parts[0]);
                double lng = Double.parseDouble(parts[1]);
                Geocoder geocoder = new Geocoder(holder.itemView.getContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    String addressLine = addresses.get(0).getAddressLine(0);
                    holder.tvAddress.setText("Địa điểm: " + addressLine);
                } else {
                    holder.tvAddress.setText("Địa điểm: " + transaction.address);
                }
            } catch (Exception e) {
                holder.tvAddress.setText("Địa điểm: " + transaction.address);
            }
        } else {
            holder.tvAddress.setText("Địa điểm: " + transaction.address);
        }
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

    public void setItems(List<Transaction> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
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
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ivType = itemView.findViewById(R.id.ivType);
        }
    }
}
