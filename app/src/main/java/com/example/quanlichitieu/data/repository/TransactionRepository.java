package com.example.quanlichitieu.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlichitieu.data.local.dao.TransactionDao;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.CategorySummary;
import com.example.quanlichitieu.data.local.entity.Transaction;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionRepository {
    private TransactionDao transactionDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TransactionRepository(Application application) {
        appDatabase db = appDatabase.getInstance(application);
        transactionDao = db.transactionDao();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionDao.getAll();
    }

    public LiveData<Transaction> getTransactionById(int id) {
        MutableLiveData<Transaction> data = new MutableLiveData<>();
        executorService.execute(() -> data.postValue(transactionDao.getById(id)));
        return data;
    }

    public void insert(Transaction transaction) {
        executorService.execute(() -> transactionDao.insert(transaction));
    }

    public void update(Transaction transaction) {
        executorService.execute(() -> transactionDao.update(transaction));
    }

    public void delete(Transaction transaction) {
        executorService.execute(() -> transactionDao.delete(transaction));
    }

    public LiveData<Double> getTotalIncome() {
        return transactionDao.getTotalIncome();
    }

    public LiveData<Double> getTotalExpense() {
        return transactionDao.getTotalExpense();
    }
    public LiveData<List<CategorySummary>> getCategorySummariesByMonth(String type, long monthMillis) {
        return transactionDao.getCategorySummariesByMonth(type, monthMillis);
    }

    public LiveData<List<CategorySummary>> getCategorySummariesByDay(String type, long dayMillis) {
        return transactionDao.getCategorySummariesByDay(type, dayMillis);
    }
}