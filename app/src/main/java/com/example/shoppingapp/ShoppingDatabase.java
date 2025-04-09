package com.example.shoppingapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ShoppingItem.class, MyItem.class}, version = 2, exportSchema = false)
public abstract class ShoppingDatabase extends RoomDatabase {
    public abstract ShoppingItemDao shoppingItemDao();
    public abstract MyItemDao myItemDao();
}
