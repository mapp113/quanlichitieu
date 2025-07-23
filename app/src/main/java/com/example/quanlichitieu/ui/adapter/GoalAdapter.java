package com.example.quanlichitieu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Goal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder>{
    private final Context context;
    private final List<Goal> goalList;

    public GoalAdapter(Context context, List<Goal> goalList) {
        this.context = context;
        this.goalList = goalList;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goal_goal_item, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goalList.get(position);
        holder.title.setText(goal.title);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String deadlineFormatted = sdf.format(new Date(goal.deadline));
        holder.deadline.setText("Hạn: " + deadlineFormatted);

        holder.current.setText(goal.currentAmount + " đ");
        holder.target.setText(goal.targetAmount + " đ");

        holder.btnEdit.setOnClickListener(v -> {
            // Xử lý chuyển sang activity chỉnh sửa
        });

    }

    public void setGoals(List<Goal> goals) {
        goalList.clear();
        goalList.addAll(goals);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return goalList != null ? goalList.size() : 0;
    }


    public static class GoalViewHolder extends RecyclerView.ViewHolder{
        public TextView title, deadline, current, target;
        public Button btnEdit;
        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.viewGoalTitile);
            deadline = itemView.findViewById(R.id.viewGoalDeadline);
            current = itemView.findViewById(R.id.viewGoalCurrent);
            target = itemView.findViewById(R.id.viewGoalTarget);
            btnEdit = itemView.findViewById(R.id.btnEditGoal);
        }
    }
}