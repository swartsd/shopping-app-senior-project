package com.example.shoppingapp;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddFromMyItemsActivity extends AppCompatActivity {

    private SelectableMyItemAdapter adapter;
    private ShoppingDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_from_my_items);

        RecyclerView recyclerView = findViewById(R.id.rvMyItems);
        Button btnAddItems = findViewById(R.id.btnAddItems);
        Button btnSelectAll = findViewById(R.id.btnSelectAll);
        Button btnUnselectAll = findViewById(R.id.btnUnselectAll);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get your database instance.
        db = ShoppingDatabaseInstance.getInstance(getApplicationContext());

        // Set up the new adapter that supports selection.
        adapter = new SelectableMyItemAdapter();
        recyclerView.setAdapter(adapter);

        // Observe all items from the my_item table.
        db.myItemDao().getAllItems().observe(this, myItems -> adapter.setItems(myItems));

        // Set up the Select All button.
        btnSelectAll.setOnClickListener(v -> adapter.selectAll());

        // Set up the Unselect All button.
        btnUnselectAll.setOnClickListener(v -> adapter.unselectAll());

        // Handle the Add Items button.
        btnAddItems.setOnClickListener(v -> {
            List<MyItem> selectedItems = adapter.getSelectedItems();
            executor.execute(() -> {
                for (MyItem item : selectedItems) {
                    ShoppingItem duplicate = db.shoppingItemDao().getItemByNormalizedName(item.getNormalizedName());
                    if (duplicate == null) {
                        ShoppingItem newShoppingItem = new ShoppingItem(
                                item.getItemName(),
                                0,
                                0,
                                item.getNormalizedName()
                        );
                        db.shoppingItemDao().insert(newShoppingItem);
                    }
                }
                runOnUiThread(this::finish);
            });
        });
    }
}
