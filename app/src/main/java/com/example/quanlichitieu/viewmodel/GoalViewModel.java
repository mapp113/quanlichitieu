package com.example.quanlichitieu.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quanlichitieu.data.local.entity.Goal;
import com.example.quanlichitieu.data.repository.GoalRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoalViewModel extends AndroidViewModel {
    private final GoalRepository goalRepository;
    private final ExecutorService executorService;


    public GoalViewModel(@NonNull Application application) {
        super(application);
        this.goalRepository = new GoalRepository(application);
        this.executorService = Executors.newSingleThreadExecutor();
    }



    public LiveData<Goal> findById(int goalId) {
        return goalRepository.findById(goalId);
    }


    public void insert(Goal goal) {
        executorService.execute(() -> goalRepository.insert(goal));
    }


    public void update(Goal goal) {
        executorService.execute(() -> goalRepository.update(goal));
    }


    public void delete(Goal goal) {
        executorService.execute(() -> goalRepository.delete(goal));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
