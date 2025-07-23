package com.example.quanlichitieu.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlichitieu.data.local.entity.Goal;

import java.util.List;

@Dao
public interface GoalDAO {
    @Insert
    void Inset(Goal goal);

    @Update
    void Update(Goal goal);

    @Delete
    void Delete(Goal goal);

    @Query("SELECT * FROM goals")
    LiveData<List<Goal>> getAll();

    @Query("SELECT * FROM goals Where goalId = :id")
    LiveData<Goal> findById(int id);


}
