package com.example.quanlichitieu.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;



@Entity(tableName = "goals",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ))
public class Goal {
    @PrimaryKey(autoGenerate = true)
    public int goalId;

    public String title;
    public double targetAmount;
    public double currentAmount;
    public long deadline;

    @ColumnInfo(index = true)
    public int userId;

    public Goal(String title, double targetAmount, double currentAmount, long deadline, int userId) {
        this.title = title;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.userId = userId;
    }
}

