package com.example.android.inventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditItemActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.android.inventoryapp.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.android.inventoryapp.EXTRA_NAME";
    public static final String EXTRA_PRICE = "com.example.android.inventoryapp.EXTRA_PRICE";
    public static final String EXTRA_QUANTITY = "com.example.android.inventoryapp.EXTRA_QUANTITY";
    public static final String EXTRA_IMAGE = "com.example.android.inventoryapp.EXTRA_IMAGE";

    private EditText editTextNameOfProduct;
    private EditText editTextPrice;
    private EditText priorityQuantity;
    private ImageView image;
    private String imagePath;

    File imageFile = null;

    public static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextNameOfProduct = findViewById(R.id.edit_text_name);
        editTextPrice = findViewById(R.id.price);
        priorityQuantity = findViewById(R.id.quantity);
        image = findViewById(R.id.image_view);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Item");
            editTextNameOfProduct.setText(intent.getStringExtra(EXTRA_NAME));
            editTextPrice.setText(intent.getStringExtra(EXTRA_PRICE));
            priorityQuantity.setText(intent.getStringExtra(EXTRA_QUANTITY));

            imagePath = intent.getStringExtra(EXTRA_IMAGE);
            Bitmap map = BitmapFactory.decodeFile(imagePath);
            image.setImageBitmap(map);
        } else {
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

        String image = imagePath;

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_PRICE, price);
        data.putExtra(EXTRA_QUANTITY, quantity);
        data.putExtra(EXTRA_IMAGE, image);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    public void setImage(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            try {
                imageFile = getImageFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
            if (imageFile != null) {
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.android.fileProvider", imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                Glide.with(this).load(imageFile).into(image);
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File getImageFile() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String name = "JPEG_" + time + "_";
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(name, ".jpg", directory);
        imagePath = imageFile.getAbsolutePath();
        return imageFile;
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