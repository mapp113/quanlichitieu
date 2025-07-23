package com.example.quanlichitieu.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.quanlichitieu.data.local.dao.GoalDAO;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.Goal;

import java.util.List;

public class GoalRepository {
    private final GoalDAO goalDao;

    public GoalRepository(Application application) {
        appDatabase db = appDatabase.getInstance(application);
        goalDao = db.goalDAO();
    }

    public LiveData<List<Goal>> getAll() {
        return goalDao.getAll();
    }

    public LiveData<Goal> findById(int goalId) {
        return goalDao.findById(goalId);
    }

    public void insert(Goal goal) {
        goalDao.inset(goal);
    }

    public void update(Goal goal) {
        goalDao.update(goal);
    }

    public void delete(Goal goal) {
        goalDao.update(goal);
    }
}
