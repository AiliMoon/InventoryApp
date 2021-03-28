package com.example.android.inventoryapp.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id; // automatically increment id and add to the column

    private String title;

    private int price;

    private int quantity;

    private String image;

    public Item(String title, int price, int quantity, String image) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }
}
