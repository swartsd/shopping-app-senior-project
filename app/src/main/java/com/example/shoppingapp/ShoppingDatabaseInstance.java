package com.example.shoppingapp;

import android.content.Context;
import androidx.room.Room;

public class ShoppingDatabaseInstance {
    private static ShoppingDatabase instance;

    public static synchronized ShoppingDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ShoppingDatabase.class, "shopping_database")
                    .fallbackToDestructiveMigration()  // Use destructive migration during development
                    .build();
        }
        return instance;
    }
}
