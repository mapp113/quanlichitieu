package com.example.quanlichitieu.data.local.entity;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static int fromType(Type type) {
        return type == null ? -1 : type.ordinal();
    }

    @TypeConverter
    public static Type toType(int ordinal) {
        if (ordinal < 0 || ordinal >= Type.values().length) return null;
        return Type.values()[ordinal];
    }
}