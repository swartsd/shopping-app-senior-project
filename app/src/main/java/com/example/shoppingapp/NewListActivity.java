package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewListActivity extends AppCompatActivity {

    private ShoppingItemAdapter adapter;
    private float salesTax = 0.07f; // Default value; will update from SharedPreferences.
    private TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        RecyclerView rvItemList = findViewById(R.id.rvItemList);
        rvItemList.setLayoutManager(new LinearLayoutManager(this));
        tvTotalPrice = findViewById(R.id.tvTotalPrice);  // Ensure tvTotalPrice exists in your layout.

        // Set up adapter with an empty list initially.
        adapter = new ShoppingItemAdapter(this, null, salesTax, new ShoppingItemAdapter.OnItemActionListener() {
            @Override
            public void onDelete(ShoppingItem item) {
                deleteItem(item);
            }

            @Override
            public void onEdit(ShoppingItem item) {
                // Launch EditItemActivity with the item's id.
                Intent intent = new Intent(NewListActivity.this, EditItemActivity.class);
                intent.putExtra("itemId", item.getId());
                startActivity(intent);
            }
        });
        rvItemList.setAdapter(adapter);

        // Add Item Button - launch AddItemActivity.
        Button btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(view -> {
            Intent intent = new Intent(NewListActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        // Clear List Button - delete the entire shopping list.
        Button btnClearList = findViewById(R.id.btnClearList);
        btnClearList.setOnClickListener(view -> new AlertDialog.Builder(NewListActivity.this)
                .setTitle("Clear Shopping List")
                .setMessage("Are you sure you want to delete the entire shopping list?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> ShoppingDatabaseInstance.getInstance(getApplicationContext())
                            .shoppingItemDao().deleteAllItems());
                    Toast.makeText(NewListActivity.this, "Shopping list cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show());

        // Add Items From My List Button - launch AddItemsFromMyListActivity.
        Button btnAddFromMyItems = findViewById(R.id.btnAddFromMyItems);
        btnAddFromMyItems.setOnClickListener(view -> {
            Intent intent = new Intent(NewListActivity.this, AddFromMyItemsActivity.class);
            startActivity(intent);
        });

        // Observe LiveData from the Room database.
        ShoppingDatabaseInstance.getInstance(getApplicationContext())
                .shoppingItemDao().getAllItems().observe(this, shoppingItems -> {
                    List<ShoppingItem> safeList = shoppingItems != null ? shoppingItems : new ArrayList<>();
                    adapter.setItems(safeList);
                    updateTotalPrice(safeList);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve the updated sales tax from SharedPreferences.
        SharedPreferences prefs = getSharedPreferences("ShoppingAppPrefs", MODE_PRIVATE);
        salesTax = prefs.getFloat("sales_tax", 0.07f);
        adapter.setSalesTax(salesTax);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }

    private void updateTotalPrice(List<ShoppingItem> items) {
        float total = 0;
        for (ShoppingItem item : items) {
            total += item.getPricePerUnit() * item.getUnit() * (1 + salesTax);
        }
        String formattedTotal = NumberFormat.getCurrencyInstance().format(total);
        String displayText = "Total: " + formattedTotal + " (Tax: " + (int)(salesTax * 100) + "%)";
        tvTotalPrice.setText(displayText);
    }

    private void deleteItem(ShoppingItem item) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> ShoppingDatabaseInstance.getInstance(getApplicationContext())
                .shoppingItemDao().delete(item));
        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
    }
}
