package com.example.quanlichitieu.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "app_settings")
public class AppSettings {
    @PrimaryKey
    public int userId;

    public String theme;         // "light", "dark", "system"
    public String language;
    public boolean notificationEnabled;
    public AppSettings(int userId, String theme, String language, boolean notificationEnabled) {
        this.userId = userId;
        this.theme = theme;
        this.language = language;
        this.notificationEnabled = notificationEnabled;
    }
}

