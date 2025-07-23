package com.example.quanlichitieu.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.quanlichitieu.data.local.entity.Goal;
import com.example.quanlichitieu.data.repository.GoalRepository;

import java.util.ArrayList;
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

    public LiveData<List<Goal>> getAllGoals() {
        return goalRepository.getAll();
    }

    /**
     xử lý trong list
     */

    public LiveData<List<Goal>> getCurrentGoals() {
        return Transformations.map(getAllGoals(), goals -> {
            List<Goal> filtered = new ArrayList<>();
            for (Goal g : goals) {
                if (g.currentAmount < g.targetAmount) {
                    filtered.add(g);
                }
            }
            return filtered;
        });
    }

    public LiveData<List<Goal>> getDoneGoals() {
        return Transformations.map(getAllGoals(), goals -> {
            List<Goal> filtered = new ArrayList<>();
            for (Goal g : goals) {
                if (g.currentAmount >= g.targetAmount) {
                    filtered.add(g);
                }
            }
            return filtered;
        });
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
