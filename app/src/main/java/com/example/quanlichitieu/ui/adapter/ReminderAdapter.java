package com.example.quanlichitieu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.Reminder;
import com.example.quanlichitieu.data.local.entity.TaskReminder;
import com.example.quanlichitieu.data.local.utils.AlarmUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private List<TaskReminder> list;
    private appDatabase db;
    private Context context;

    public ReminderAdapter(Context context, List<TaskReminder> list, appDatabase db) {
        this.context = context;
        this.list = list;
        this.db = db;
    }

    public void updateData(List<TaskReminder> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvTime;
    Switch switchActive;
    ImageView btnDelete;

    ViewHolder(View view) {
        super(view);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTime = view.findViewById(R.id.tvTime); // thêm dòng này
        switchActive = view.findViewById(R.id.switchActive);
        btnDelete = view.findViewById(R.id.btnDelete);
    }
}


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tools_reminder_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskReminder reminder = list.get(position);
        holder.tvTitle.setText(reminder.title);
        holder.switchActive.setChecked(reminder.isActive);

        holder.switchActive.setOnCheckedChangeListener(null); // tránh callback không mong muốn
        holder.switchActive.setChecked(reminder.isActive);

        String formattedTime = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
                .format(new Date(reminder.timeMillis));
        holder.tvTime.setText(formattedTime);


        holder.switchActive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            reminder.isActive = isChecked;
            db.reminderDao().update(reminder);

            // (Tùy chọn) Cập nhật AlarmManager:
            AlarmUtils.updateAlarm(context, reminder);
        });

        holder.btnDelete.setOnClickListener(v -> {
            db.reminderDao().delete(reminder);
            AlarmUtils.cancelAlarm(context, reminder);
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


