package com.example.android.inventoryapp.AddEdit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.android.inventoryapp.data.Item;
import com.example.android.inventoryapp.repository.Repository;

public class AddEditPresenter extends AndroidViewModel implements AddEditContract.Presenter {

    private Repository repository;
    private AddEditContract contract;

    public AddEditPresenter(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
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

    public void onDestroy() {
        contract = null;
    }
}
