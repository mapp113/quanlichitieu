package com.example.quanlichitieu.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.auth.User;

@Entity(tableName = "notification_history",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ))
public class NotificationHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String content;
    public long timestamp;

    @ColumnInfo(index = true)
    public int userId;

    public NotificationHistory(String content, long timestamp, int userId) {
        this.content = content;
        this.timestamp = timestamp;
        this.userId = userId;
    }
}
