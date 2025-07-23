package com.example.quanlichitieu.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.quanlichitieu.data.local.dao.TransactionDao;
import com.example.quanlichitieu.data.local.entity.Category;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.data.local.entity.User;
import com.example.quanlichitieu.data.local.entity.Converters;

@Database(entities = {Transaction.class}, version = 5, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class appDatabase extends RoomDatabase {
    private static appDatabase instance;

    public abstract TransactionDao transactionDao();

    public static synchronized appDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            appDatabase.class, "transaction_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
