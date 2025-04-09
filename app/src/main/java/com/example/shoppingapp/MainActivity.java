package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSetTax = findViewById(R.id.btnSetTax);
        Button btnStartList = findViewById(R.id.btnStartList);
        Button btnMyItems = findViewById(R.id.btnMyItems);

        // Navigate to SalesTaxActivity when clicked
        btnSetTax.setOnClickListener(view -> {
            Intent taxIntent = new Intent(MainActivity.this, SalesTaxActivity.class);
            startActivity(taxIntent);
        });

        // Navigate to NewListActivity when clicked
        btnStartList.setOnClickListener(view -> {
            Intent listIntent = new Intent(MainActivity.this, NewListActivity.class);
            startActivity(listIntent);
        });

        // Navigate to MyItemsActivity when clicked
        btnMyItems.setOnClickListener(view -> {
            Intent listIntent = new Intent(MainActivity.this, MyItemsActivity.class);
            startActivity(listIntent);
        });
    }
}
