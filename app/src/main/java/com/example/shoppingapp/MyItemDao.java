package com.example.shoppingapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyItemDao {

    @Insert
    void insert(MyItem item);

    @Update
    void update(MyItem item);

    @Delete
    void delete(MyItem item);

    @Query("SELECT * FROM my_item")
    LiveData<List<MyItem>> getAllItems();

    // New method to get a single item by its id
    @Query("SELECT * FROM my_item WHERE id = :id LIMIT 1")
    MyItem getItemById(int id);

    // New Method to delete entire list
    @Query("DELETE FROM my_item")
    void deleteAllItems();

    @Query("SELECT * FROM my_item WHERE normalizedName = :normalized LIMIT 1")
    MyItem getItemByNormalizedName(String normalized);

}
