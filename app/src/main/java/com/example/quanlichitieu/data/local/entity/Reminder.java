package com.example.quanlichitieu.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.auth.User;

@Entity(tableName = "reminders",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ))
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    public int reminderId;

    public String message;
    public long remindAt;
    public boolean isRepeated;

    @ColumnInfo(index = true)
    public int userId;

    public Reminder(String message, long remindAt, boolean isRepeated, int userId) {
        this.message = message;
        this.remindAt = remindAt;
        this.isRepeated = isRepeated;
        this.userId = userId;
    }
}
