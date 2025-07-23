package com.example.quanlichitieu.data.local.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quanlichitieu.data.local.dao.CategoryDao;
import com.example.quanlichitieu.data.local.dao.GoalDAO;
import com.example.quanlichitieu.data.local.dao.TransactionDao;
import com.example.quanlichitieu.data.local.dao.UserDao;
import com.example.quanlichitieu.data.local.entity.Category;
import com.example.quanlichitieu.data.local.entity.Goal;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.data.local.entity.User;
import com.example.quanlichitieu.data.local.entity.Converters;

import java.util.concurrent.Executors;

@Database(entities = {Transaction.class, Category.class, User.class, Goal.class}, version = 5, exportSchema = false)

@TypeConverters(Converters.class)
public abstract class appDatabase extends RoomDatabase {
    private static appDatabase instance;
    public abstract CategoryDao categoryDao();
    public abstract UserDao userDao();
    public abstract TransactionDao transactionDao();
    public abstract GoalDAO goalDAO();

    public static synchronized appDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            appDatabase.class, "app_db")
                    .allowMainThreadQueries()
                    .addCallback(roomCallback) // ✅ Thêm dòng này
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                appDatabase dbInstance = instance;
                if (dbInstance != null) {
                    CategoryDao dao = dbInstance.categoryDao();
                    dao.insert(new Category("Ăn uống"));
                    dao.insert(new Category("Lương"));
                    dao.insert(new Category("Giải trí"));
                    dao.insert(new Category("Di chuyển"));
                    dao.insert(new Category("Khác"));
                }
            });
        }
    };

}
