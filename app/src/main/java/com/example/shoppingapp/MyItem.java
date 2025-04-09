package com.example.shoppingapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_item")
public class MyItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String itemName;
    private String normalizedName;


    // Constructor
    public MyItem(String itemName, String normalizedName) {
        this.itemName = itemName;
        this.normalizedName = normalizedName;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNormalizedName() { return normalizedName; }
    public void setNormalizedName(String normalizedName) { this.normalizedName = normalizedName; }
}
