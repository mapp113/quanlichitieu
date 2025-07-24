package com.example.quanlichitieu.data.local.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.quanlichitieu.data.local.entity.TaskReminder;

public class AlarmUtils {
    public static void updateAlarm(Context context, TaskReminder reminder) {
        if (reminder.isActive) {
            // Đặt lại lịch
            AlarmScheduler.schedule(context, reminder);
        } else {
            cancelAlarm(context, reminder);
        }
    }

    public static void cancelAlarm(Context context, TaskReminder reminder) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, reminder.id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}

