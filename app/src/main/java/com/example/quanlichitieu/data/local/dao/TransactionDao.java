package com.example.quanlichitieu.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


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
    List<Transaction> getAll();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income'")
    double getTotalIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense'")
    double getTotalExpense();

}
