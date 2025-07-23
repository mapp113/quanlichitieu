package com.example.quanlichitieu.data.local.entity;

import androidx.room.Ignore;

public class CategorySummary {
    private String categoryName;
    private double amount;
    public CategorySummary(String categoryName, double amount) {
        this.categoryName = categoryName;
        this.amount = amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
