package com.example.android.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditItemActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.android.inventoryapp.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.android.inventoryapp.EXTRA_NAME";
    public static final String EXTRA_PRICE = "com.example.android.inventoryapp.EXTRA_PRICE";
    public static final String EXTRA_QUANTITY = "com.example.android.inventoryapp.EXTRA_QUANTITY";
    public static final String EXTRA_IMAGE = "com.example.android.inventoryapp.EXTRA_IMAGE";

    private EditText editTextNameOfProduct;
    private EditText editTextPrice;
    private EditText priorityQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextNameOfProduct = findViewById(R.id.edit_text_name);
        editTextPrice = findViewById(R.id.price);
        priorityQuantity = findViewById(R.id.quantity);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Item");
            editTextNameOfProduct.setText(intent.getStringExtra(EXTRA_NAME));
            editTextPrice.setText(intent.getStringExtra(EXTRA_PRICE));
            priorityQuantity.setText(intent.getStringExtra(EXTRA_QUANTITY));
        }
        else {
            setTitle("Add Item");
        }
    }

    private void saveItem() {
        String name = editTextNameOfProduct.getText().toString();
        String price = editTextPrice.getText().toString();
        String quantity = priorityQuantity.getText().toString();

        if(name.trim().isEmpty() || price.trim().isEmpty() || quantity.trim().isEmpty()) {
            Toast.makeText(this, "Please fill all empty fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_PRICE, price);
        data.putExtra(EXTRA_QUANTITY, quantity);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                saveItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}