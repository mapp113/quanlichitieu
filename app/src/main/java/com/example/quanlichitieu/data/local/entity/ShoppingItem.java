package com.example.quanlichitieu.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shoppingitems")
public class ShoppingItem {
    @PrimaryKey(autoGenerate = true)
    private int shoppingItemId;

    private String name;
    private boolean isChecked;

    private int userId;

    public ShoppingItem(String name, boolean isChecked, int userId) {
        this.name = name;
        this.isChecked = isChecked;
        this.userId = userId;
    }

    public int getShoppingItemId() {
        return shoppingItemId;
    }

    public void setShoppingItemId(int shoppingItemId) {
        this.shoppingItemId = shoppingItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
