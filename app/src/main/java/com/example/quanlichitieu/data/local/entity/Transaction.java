package com.example.quanlichitieu.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "transactions")
public class Transaction implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String description;
    private double amount;
    private String type;
    private String date;

    public Transaction(String type, double amount, String description, String date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}