package com.example.android.inventoryapp.AddEdit;

import com.example.android.inventoryapp.data.Item;

public interface AddEditContract {

    interface Presenter {
        void delete(Item item);
        void update(Item item);
        void insert(Item item);
        void deleteAllItems();
    }

    interface View {
        void saveItem();
    }
}
