package com.example.quanlichitieu.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlichitieu.data.local.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM category ORDER BY id ASC")
    LiveData<List<Category>> getAll();

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    LiveData<Category> findById(int id);
    @Query("Select name from category where id=:id")
    LiveData<String> getCategoryNameById(int id);
}

