package com.example.shoppingapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingItemDao {

    @Insert
    void insert(ShoppingItem item);

    @Update
    void update(ShoppingItem item);

    @Delete
    void delete(ShoppingItem item);

    @Query("SELECT * FROM shopping_item")
    LiveData<List<ShoppingItem>> getAllItems();

    // New method to get a single item by its id
    @Query("SELECT * FROM shopping_item WHERE id = :id LIMIT 1")
    ShoppingItem getItemById(int id);

    // New Method to delete entire list
    @Query("DELETE FROM shopping_item")
    void deleteAllItems();

    @Query("SELECT * FROM shopping_item WHERE normalizedName = :normalized LIMIT 1")
    ShoppingItem getItemByNormalizedName(String normalized);

}
