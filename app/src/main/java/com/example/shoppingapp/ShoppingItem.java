package com.example.shoppingapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_item")
public class ShoppingItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String itemName;
    private float pricePerUnit;
    private float unit;
    private String normalizedName;

    // Constructor
    public ShoppingItem(String itemName, float pricePerUnit, float unit, String normalizedName) {
        this.itemName = itemName;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
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

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public float getUnit() {
        return unit;
    }

    public void setUnit(float unit) {
        this.unit = unit;
    }

    public String getNormalizedName() { return normalizedName; }

    public void setNormalizedName(String normalizedName) { this.normalizedName = normalizedName; }
}
