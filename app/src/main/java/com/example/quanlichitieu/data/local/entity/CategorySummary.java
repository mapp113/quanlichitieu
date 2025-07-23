package com.example.quanlichitieu.data.local.entity;

public class CategorySummary {
    private String categoryName;
    private double amount;
    private double percent;

    public CategorySummary(String categoryName, double amount, double percent) {
        this.categoryName = categoryName;
        this.amount = amount;
        this.percent = percent;
    }

    public String getCategoryName() { return categoryName; }
    public double getAmount() { return amount; }
    public double getPercent() { return percent; }
}
