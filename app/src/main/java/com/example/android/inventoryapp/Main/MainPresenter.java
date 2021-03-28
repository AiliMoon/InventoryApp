package com.example.android.inventoryapp.Main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.inventoryapp.data.Item;
import com.example.android.inventoryapp.repository.Repository;

import java.util.List;

public class MainPresenter extends AndroidViewModel implements MainContract.Presenter {

    private Repository repository;
    private LiveData<List<Item>> allItems;

    public MainPresenter(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allItems = repository.getAllItems();
    }

    @Override
    public void delete(Item item) {
        repository.delete(item);
    }

    @Override
    public void update(Item item) {
        repository.update(item);
    }

    @Override
    public void insert(Item item) {
        repository.insert(item);
    }

    @Override
    public void deleteAllItems() {
        repository.deleteAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }
}
