package com.example.quanlichitieu.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.quanlichitieu.data.local.dao.TransactionDao;
import com.example.quanlichitieu.data.local.entity.Transaction;

@Database(entities = {Transaction.class}, version = 1, exportSchema = false)
public abstract class appDatabase extends RoomDatabase{
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
