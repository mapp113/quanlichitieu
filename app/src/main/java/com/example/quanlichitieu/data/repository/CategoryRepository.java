package com.example.quanlichitieu.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.quanlichitieu.data.local.dao.CategoryDao;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.Category;

import java.util.List;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private final CategoryDao categoryDao;
    private final LiveData<List<Category>> allCategories;

    public CategoryRepository(Application application) {
        appDatabase db = appDatabase.getInstance(application);
        categoryDao = db.categoryDao();
        allCategories = categoryDao.getAll();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void insert(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> categoryDao.insert(category));
    }

    public void update(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> categoryDao.update(category));
    }

    public void delete(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> categoryDao.delete(category));
    }

    public Category findById(int id) {
        // Chạy trên thread riêng nếu không dùng LiveData
        return categoryDao.findById(id).getValue();
    }
}
