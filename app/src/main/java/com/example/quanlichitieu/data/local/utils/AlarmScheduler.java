package com.example.quanlichitieu.data.local.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.quanlichitieu.data.local.entity.TaskReminder;

import java.util.Calendar;

public class AlarmScheduler {

    public static void schedule(Context context, TaskReminder reminder) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("title", reminder.title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.id, // dùng id làm requestCode
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getTimeMillis());

        long triggerAtMillis = calendar.getTimeInMillis();
        long intervalMillis = getIntervalMillis(reminder.repeatType);

        if (intervalMillis > 0) {
            // Lặp lại
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    intervalMillis,
                    pendingIntent
            );
        } else {
            // Một lần
            try {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                );
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private static long getIntervalMillis(String repeatType) {
        switch (repeatType) {
            case "daily":
                return AlarmManager.INTERVAL_DAY;
            case "weekly":
                return AlarmManager.INTERVAL_DAY * 7;
            case "monthly":
                return AlarmManager.INTERVAL_DAY * 30; // đơn giản hóa
            default:
                return 0; // một lần
        }
    }

    public static void cancel(Context context, TaskReminder reminder) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}

