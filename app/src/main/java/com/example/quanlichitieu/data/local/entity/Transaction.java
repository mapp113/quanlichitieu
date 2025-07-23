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
y=======
import com.example.quanlichitieu.data.local.entity.User;
import com.example.quanlichitieu.data.local.entity.Category;

@Entity(
        tableName = "transactions"

    public int category; //        FOOD,SALARY,ENTERTAINMENT,TRANSPORT, INVESTMENT
    public Type type; // "INCOME" OR "EXPENSE"
    public long date;
    public int address;
    public Type type;
    public String date;
    public int address;

    public Transaction(String title, double amount, Type type, String date, int address) {
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.address = address;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;


    }

}

