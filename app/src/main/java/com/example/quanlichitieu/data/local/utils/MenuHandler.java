package com.example.quanlichitieu.data.local.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.ui.activity.AddEditActivity;
import com.example.quanlichitieu.ui.activity.SettingsActivity;
import com.example.quanlichitieu.ui.activity.ToolsActivity;
import com.example.quanlichitieu.ui.activity.TransactionActivity;
import com.example.quanlichitieu.ui.activity.TransactionRealActivity;
import com.example.quanlichitieu.ui.activity.goal.ListGoalActivity;

public class MenuHandler {

    public static boolean handleMenuClick(Context context, MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(context, TransactionActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.report) {
            Intent intent = new Intent(context, TransactionRealActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.goals) {
            Intent intent = new Intent(context, ListGoalActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.tools) {
            Intent intent = new Intent(context, ToolsActivity.class);
            context.startActivity(intent);
            return true;
        } else if (id == R.id.setting) {
            Intent intent = new Intent(context, SettingsActivity.class);
            context.startActivity(intent);
            return true;
        }

        return false;
    }
}
