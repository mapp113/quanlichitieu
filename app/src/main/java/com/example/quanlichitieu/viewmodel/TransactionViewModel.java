package com.example.quanlichitieu.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quanlichitieu.data.local.entity.CategorySummary;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.data.repository.TransactionRepository;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {
    private TransactionRepository repository;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        repository = new TransactionRepository(application);
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return repository.getAllTransactions();
    }

    public LiveData<Transaction> getTransactionById(int id) {
        return repository.getTransactionById(id);
    }

//    public LiveData<List<Transaction>> getTransactionsByUser(int userId) {
//        return repository.getTransactionsByUser(userId);
//    }

    public void insert(Transaction transaction) {
        repository.insert(transaction);
    }

    public void update(Transaction transaction) {
        repository.update(transaction);
    }

    public void delete(Transaction transaction) {
        repository.delete(transaction);
    }

    public LiveData<Double> getTotalIncome() {
        return repository.getTotalIncome();
    }

    public LiveData<Double> getTotalExpense() {
        return repository.getTotalExpense();
    }
    public LiveData<List<CategorySummary>> getCategorySummariesByMonth(String type, long monthMillis) {
        return repository.getCategorySummariesByMonth(type, monthMillis);
    }

    public LiveData<List<CategorySummary>> getCategorySummariesByDay(String type, long dayMillis) {
        return repository.getCategorySummariesByDay(type, dayMillis);
    }
}
