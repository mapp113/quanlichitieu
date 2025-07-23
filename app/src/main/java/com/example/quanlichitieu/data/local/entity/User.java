package com.example.quanlichitieu.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;

    public String username;
    public String password;
    public String fullName;
    public String email;
    public long createdAt;
    public String salt;

    public User(String username, String password, String fullName, String email, long createdAt, String salt) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = createdAt;
        this.salt = salt;
    }

}
