package com.example.quanlichitieu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.ShoppingItem;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    public interface OnItemActionListener {
        void onItemCheckedChanged(ShoppingItem item, boolean isChecked);
        void onItemDeleted(ShoppingItem item);
    }

    private List<ShoppingItem> itemList;
    private OnItemActionListener listener;

    public ShoppingListAdapter(List<ShoppingItem> itemList, OnItemActionListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public void updateList(List<ShoppingItem> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem item = itemList.get(position);
        holder.tvItemName.setText(item.getName());
        // ✅ Gỡ listener cũ (nếu có) trước khi gán trạng thái checkbox
        holder.cbChecked.setOnCheckedChangeListener(null);
        holder.cbChecked.setChecked(item.isChecked());

        // ✅ Gắn lại listener sau khi đã thiết lập trạng thái
        holder.cbChecked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            listener.onItemCheckedChanged(item, isChecked);
        });

        holder.btnDelete.setOnClickListener(v -> listener.onItemDeleted(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbChecked;
        TextView tvItemName;
        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbChecked = itemView.findViewById(R.id.cbChecked);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
