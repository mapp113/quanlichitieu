package com.example.quanlichitieu.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlichitieu.data.local.entity.Reminder;
import com.example.quanlichitieu.data.local.entity.TaskReminder;

import java.util.List;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM TaskReminder ORDER BY isActive DESC, timeMillis ASC")
    List<TaskReminder> getAll();

    @Insert
    long insert(TaskReminder reminder);

    @Update
    void update(TaskReminder reminder);

    @Delete
    void delete(TaskReminder reminder);
}

