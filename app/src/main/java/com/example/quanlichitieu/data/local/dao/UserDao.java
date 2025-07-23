package com.example.quanlichitieu.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quanlichitieu.data.local.entity.User;
@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Insert
    void insertUser(User user);
}
