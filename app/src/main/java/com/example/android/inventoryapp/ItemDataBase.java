package com.example.android.inventoryapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Item.class, version = 1, exportSchema = false)
public abstract class ItemDataBase extends RoomDatabase {
    // same instance
    private static ItemDataBase instance;

    public abstract ItemDao itemDao();

    public static synchronized ItemDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ItemDataBase.class, "item_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDbAsyncTask(instance).execute();
        }
    };

    private static class populateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ItemDao itemDao;

        private populateDbAsyncTask(ItemDataBase db) {
            itemDao = db.itemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.insert(new Item("Name", 1000, 1, ""));
            return null;
        }
    }
}
