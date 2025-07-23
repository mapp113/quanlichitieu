package com.example.quanlichitieu.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

import com.example.quanlichitieu.data.local.entity.CategorySummary;
import com.example.quanlichitieu.data.local.entity.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    void insert(Transaction transaction);
    @Update
    void update(Transaction transaction);
    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    LiveData<List<Transaction>> getAll();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 0")
    LiveData<Double> getTotalIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 1")
    LiveData<Double> getTotalExpense();

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    Transaction getById(int id);
    @Query("SELECT c.name AS categoryName, SUM(t.amount) AS amount " +
            "FROM transactions t " +
            "LEFT JOIN category c ON t.categoryId = c.id " +
            "WHERE t.type = :type AND strftime('%m', datetime(t.date / 1000, 'unixepoch')) = :month " +
            "GROUP BY t.categoryId")
    LiveData<List<CategorySummary>> getCategorySummariesByMonth(String type, String month);
    @Query("SELECT c.name AS categoryName, SUM(t.amount) AS amount " +
            "FROM transactions t " +
            "LEFT JOIN category c ON t.categoryId = c.id " +
            "WHERE t.type = :type AND strftime('%Y-%m-%d', datetime(t.date / 1000, 'unixepoch')) = :day " +
            "GROUP BY t.categoryId")
    LiveData<List<CategorySummary>> getCategorySummariesByDay(String type, String day);

}
