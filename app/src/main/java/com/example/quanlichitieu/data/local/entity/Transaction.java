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

    public Type type; // INCOME or EXPENSE

    public String date;

    public int address;

    @ColumnInfo(index = true)
    public Integer categoryId;

    @ColumnInfo(index = true)
    public int userOwnerId;

    public Transaction(String title, double amount, Type type, String date, int address, Integer categoryId, int userOwnerId) {
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.address = address;
        this.categoryId = categoryId;
        this.userOwnerId = userOwnerId;
    }

}
