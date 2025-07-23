package com.example.quanlichitieu.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quanlichitieu.data.local.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);
    @Delete
    void delete(Category category);
    @Query("select * from category")
    LiveData<List<Category>> categoryList();
    @Query("select * from category where id=:id limit 1")
    LiveData<Category> findById(int id);
}
