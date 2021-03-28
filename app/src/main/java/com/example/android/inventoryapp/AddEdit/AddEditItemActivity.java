package com.example.android.inventoryapp.AddEdit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.inventoryapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditItemActivity extends AppCompatActivity implements AddEditContract.View {

    public static final String EXTRA_ID = "com.example.android.inventoryapp.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.android.inventoryapp.EXTRA_NAME";
    public static final String EXTRA_PRICE = "com.example.android.inventoryapp.EXTRA_PRICE";
    public static final String EXTRA_QUANTITY = "com.example.android.inventoryapp.EXTRA_QUANTITY";
    public static final String EXTRA_IMAGE = "com.example.android.inventoryapp.EXTRA_IMAGE";

    public static final int REQUEST_TAKE_IMAGE = 1;

    private EditText editTextNameOfProduct;
    private EditText editTextPrice;
    private EditText quantity;
    private ImageView imageViewImage;
    private String imagePath;
    private Button button;

    private AddEditPresenter presenter;

    File imageFile = null;
    File directory;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextNameOfProduct = findViewById(R.id.edit_text_name);
        editTextPrice = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        imageViewImage = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AddEditPresenter.class);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Item");
            editTextNameOfProduct.setText(intent.getStringExtra(EXTRA_NAME));
            editTextPrice.setText(String.valueOf(intent.getIntExtra(EXTRA_PRICE, 1)));
            quantity.setText(String.valueOf(intent.getIntExtra(EXTRA_QUANTITY, 1)));

            imagePath = intent.getStringExtra(EXTRA_IMAGE);
            Bitmap map = BitmapFactory.decodeFile(imagePath);
            imageViewImage.setImageBitmap(map);
        } else {
            setTitle("Add Item");
        }

        button.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    imageFile = getImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (imageFile != null) {
                    imageUri = FileProvider.getUriForFile(this,
                            "com.example.android.fileProvider", imageFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                }
                startActivityForResult(cameraIntent, REQUEST_TAKE_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_IMAGE && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFile.getAbsolutePath()).into(imageViewImage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void saveItem() {
        String name = editTextNameOfProduct.getText().toString();

        if(name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a name of the product", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(editTextPrice.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(this, "Please insert price of the product", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantityOfItems;
        try {
            quantityOfItems = Integer.parseInt(quantity.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(this, "Please insert quantity of the product", Toast.LENGTH_SHORT).show();
            return;
        }

        String image = imageUri.toString();

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_PRICE, price);
        data.putExtra(EXTRA_QUANTITY, quantityOfItems);
        data.putExtra(EXTRA_IMAGE, image);

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

    private File getImageFile() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String name = "jpg_" + time + "_";
        directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        imageFile = File.createTempFile(name, ".jpg", directory);
        imagePath = imageFile.getAbsolutePath();
        return imageFile;
    }
}