package com.example.quanlichitieu.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;

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

//    @Query("SELECT * FROM transactions WHERE userOwnerId = :userId ORDER BY id DESC")
//    List<Transaction> getByUserOwnerId(int userId);
}
