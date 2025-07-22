package com.example.quanlichitieu.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.auth.User;

@Entity(tableName = "transactions",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "userOwnerId",
                onDelete = ForeignKey.CASCADE
        ))
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public double amount;
    public Category category; //        FOOD,SALARY,ENTERTAINMENT,TRANSPORT, INVESTMENT
    public Type type; // "INCOME" OR "EXPENSE"
    public long date;

    @ColumnInfo(index = true)
    public int userOwnerId;

    public Transaction(String title, double amount, Category category, Type type, long date, int userOwnerId) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
        this.userOwnerId = userOwnerId;
    }
}

