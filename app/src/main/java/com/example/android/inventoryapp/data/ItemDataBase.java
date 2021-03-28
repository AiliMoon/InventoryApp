package com.example.android.inventoryapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


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
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
