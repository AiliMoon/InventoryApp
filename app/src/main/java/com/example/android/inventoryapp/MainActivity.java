package com.example.android.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_ITEM_REQUEST = 1;
    public static final int EDIt_ITEM_REQUEST = 2;

    private ViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton button_add_item = findViewById(R.id.button_add_item);
        button_add_item.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            startActivityForResult(intent, ADD_ITEM_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ViewModel.class);
        viewModel.getAllItems().observe(this, adapter::setItems);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage(R.string.are_you_sure);
                alertDialog.setPositiveButton(R.string.delete, (dialog, which) -> {
                    viewModel.delete(adapter.getItemAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, R.string.has_deleted, Toast.LENGTH_SHORT).show();
                });
                alertDialog.setNegativeButton(R.string.cancel, (dialog, which) -> {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    Toast.makeText(MainActivity.this, R.string.has_not_deleted, Toast.LENGTH_SHORT).show();
                });
                alertDialog.create().show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            intent.putExtra(AddEditItemActivity.EXTRA_ID, item.getId());
            intent.putExtra(AddEditItemActivity.EXTRA_NAME, item.getTitle());
            intent.putExtra(AddEditItemActivity.EXTRA_PRICE, item.getPrice());
            intent.putExtra(AddEditItemActivity.EXTRA_QUANTITY, item.getQuantity());
            intent.putExtra(AddEditItemActivity.EXTRA_IMAGE, item.getImage());
            startActivityForResult(intent, EDIt_ITEM_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK) {

            String name = data.getStringExtra(AddEditItemActivity.EXTRA_NAME);
            int price = data.getIntExtra(AddEditItemActivity.EXTRA_PRICE, 1);
            int quantity = data.getIntExtra(AddEditItemActivity.EXTRA_QUANTITY, 1);
            String image = data.getStringExtra(AddEditItemActivity.EXTRA_IMAGE);

            Item item = new Item(name, price, quantity, image);
            viewModel.insert(item);

            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIt_ITEM_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditItemActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Item can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(AddEditItemActivity.EXTRA_NAME);
            int price = data.getIntExtra(AddEditItemActivity.EXTRA_PRICE, 1);
            int quantity = data.getIntExtra(AddEditItemActivity.EXTRA_QUANTITY, 1);
            String image = data.getStringExtra(AddEditItemActivity.EXTRA_IMAGE);

            Item item = new Item(name, price, quantity, image);
            item.setId(id);
            viewModel.update(item);

            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.has_not_saved, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_items) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.are_you_sure)
                    .setPositiveButton(R.string.delete, (dialog, id) -> viewModel.deleteAllItems())
                    .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}