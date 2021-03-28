package com.example.android.inventoryapp.Main;

import com.example.android.inventoryapp.data.Item;

public interface MainContract {

    interface Presenter {
        void delete(Item item);
        void update(Item item);
        void insert(Item item);
        void deleteAllItems();
    }
}
