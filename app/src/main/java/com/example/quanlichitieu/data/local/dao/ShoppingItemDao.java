package com.example.quanlichitieu.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlichitieu.data.local.entity.ShoppingItem;

import java.util.List;
@Dao
public interface ShoppingItemDao {
    @Query("SELECT * FROM shoppingitems WHERE userId = :userId")
    List<ShoppingItem> getShoppingItemsByUserId(int userId);

    @Insert
    void insertShoppingItem(ShoppingItem shoppingItem);

    @Update
    void updateShoppingItem(ShoppingItem shoppingItem);

    @Delete
    void deleteShoppingItem(ShoppingItem shoppingItem);
}
