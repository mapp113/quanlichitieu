package com.example.quanlichitieu.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
}
