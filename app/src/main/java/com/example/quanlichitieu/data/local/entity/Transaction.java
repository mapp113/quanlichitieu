package com.example.quanlichitieu.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "transactions",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userOwnerId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = "id",
                        childColumns = "categoryId",
                        onDelete = ForeignKey.SET_NULL
                )
        }
)
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public double amount;
    public int category; //        FOOD,SALARY,ENTERTAINMENT,TRANSPORT, INVESTMENT
    public Type type; // "INCOME" OR "EXPENSE"
    public long date;
    public int address;

    @ColumnInfo(index = true)
    public int userOwnerId;

    public Transaction(String title, double amount, int category, Type type, long date, int userOwnerId,int address) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
        this.userOwnerId = userOwnerId;
        this.address=address;
    }

}

