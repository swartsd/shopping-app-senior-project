package com.example.shoppingapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox; // Import CheckBox
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.shoppingapp.utils.StringUtils;

public class EditItemActivity extends AppCompatActivity {

    private EditText etItemName, etPricePerUnit, etUnitValue;
    private CheckBox cbAddToCart; // Declare the new CheckBox
    private int itemId;
    private ShoppingItem currentItem;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItemName = findViewById(R.id.etItemName);
        etPricePerUnit = findViewById(R.id.etPricePerUnit);
        etUnitValue = findViewById(R.id.etUnitValue);
        cbAddToCart = findViewById(R.id.cbAddToCart); // Initialize the CheckBox
        Button btnSaveItem = findViewById(R.id.btnSaveItem);
        Button btnDeleteItem = findViewById(R.id.btnDeleteItem);

        // Retrieve the item ID passed from the previous activity.
        itemId = getIntent().getIntExtra("itemId", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid item", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load the item from the database on a background thread.
        executor.execute(() -> {
            currentItem = ShoppingDatabaseInstance.getInstance(getApplicationContext())
                    .shoppingItemDao().getItemById(itemId);
            if (currentItem != null) {
                runOnUiThread(() -> {
                    etItemName.setText(currentItem.getItemName());
                    etPricePerUnit.setText(String.valueOf(currentItem.getPricePerUnit()));
                    etUnitValue.setText(String.valueOf(currentItem.getUnit()));
                    // Set the CheckBox state based on the item's inCart flag.
                    cbAddToCart.setChecked(currentItem.isInCart());
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(EditItemActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });

        // Save button: update the item.
        btnSaveItem.setOnClickListener(v -> {
            String name = etItemName.getText().toString().trim();
            String priceStr = etPricePerUnit.getText().toString().trim();
            String unitStr = etUnitValue.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                etItemName.setError("Item name is required");
                Toast.makeText(EditItemActivity.this, "Item name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            float price = TextUtils.isEmpty(priceStr) ? 0f : Float.parseFloat(priceStr);
            float unit = TextUtils.isEmpty(unitStr) ? 0f : Float.parseFloat(unitStr);
            String normalized = StringUtils.normalizeText(name);

            executor.execute(() -> {
                // Check for duplicates by normalized name.
                ShoppingItem duplicate = ShoppingDatabaseInstance.getInstance(getApplicationContext())
                        .shoppingItemDao().getItemByNormalizedName(normalized);
                if (duplicate != null && duplicate.getId() != currentItem.getId()) {
                    runOnUiThread(() ->
                            Toast.makeText(EditItemActivity.this, "Another item with that name exists.", Toast.LENGTH_SHORT).show());
                } else {
                    // No duplicate conflict; update the current item.
                    currentItem.setItemName(name);
                    currentItem.setPricePerUnit(price);
                    currentItem.setUnit(unit);
                    currentItem.setNormalizedName(normalized);
                    // Update the inCart flag from the state of the CheckBox.
                    currentItem.setInCart(cbAddToCart.isChecked());
                    ShoppingDatabaseInstance.getInstance(getApplicationContext())
                            .shoppingItemDao().update(currentItem);
                    runOnUiThread(() -> {
                        Toast.makeText(EditItemActivity.this, "Item updated", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
        });

        // Delete button: ask for confirmation then delete the item.
        btnDeleteItem.setOnClickListener(v ->
                new AlertDialog.Builder(EditItemActivity.this)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Delete", (dialog, which) -> executor.execute(() -> {
                            ShoppingDatabaseInstance.getInstance(getApplicationContext())
                                    .shoppingItemDao().delete(currentItem);
                            runOnUiThread(() -> {
                                Toast.makeText(EditItemActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }))
                        .setNegativeButton("Cancel", null)
                        .show());
    }
}
