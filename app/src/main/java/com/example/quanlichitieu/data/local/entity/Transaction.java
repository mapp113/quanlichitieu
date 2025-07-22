package com.example.quanlichitieu.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.quanlichitieu.data.local.entity.User;
import com.example.quanlichitieu.data.local.entity.Category;

@Entity(
        tableName = "transactions"
)
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public double amount;
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

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}

