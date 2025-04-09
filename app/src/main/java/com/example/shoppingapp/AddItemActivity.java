package com.example.shoppingapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.shoppingapp.utils.StringUtils;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etPricePerUnit, etUnitValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etPricePerUnit = findViewById(R.id.etPricePerUnit);
        etUnitValue = findViewById(R.id.etUnitValue);
        Button btnSaveItem = findViewById(R.id.btnSaveItem);

        btnSaveItem.setOnClickListener(view -> {
            String itemName = etItemName.getText().toString().trim();
            String priceText = etPricePerUnit.getText().toString().trim();
            String unitText = etUnitValue.getText().toString().trim();

            if (TextUtils.isEmpty(itemName)) {
                etItemName.setError("Item name is required");
                Toast.makeText(AddItemActivity.this, "Item name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            float pricePerUnit = TextUtils.isEmpty(priceText) ? 0f : Float.parseFloat(priceText);
            float unitValue = TextUtils.isEmpty(unitText) ? 0f : Float.parseFloat(unitText);

            // Normalize the input name.
            String normalized = StringUtils.normalizeText(itemName);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                // Check for duplicate items by normalized name.
                ShoppingItem duplicate = ShoppingDatabaseInstance.getInstance(getApplicationContext())
                        .shoppingItemDao().getItemByNormalizedName(normalized);
                if (duplicate == null) {
                    // No duplicate exists; insert the new item.
                    ShoppingItem newItem = new ShoppingItem(itemName, pricePerUnit, unitValue,  normalized);
                    ShoppingDatabaseInstance.getInstance(getApplicationContext())
                            .shoppingItemDao().insert(newItem);
                    runOnUiThread(() ->
                            Toast.makeText(AddItemActivity.this, "Item saved", Toast.LENGTH_SHORT).show());
                    runOnUiThread(this::finish);
                } else {
                    // Duplicate found; notify the user.
                    runOnUiThread(() ->
                            Toast.makeText(AddItemActivity.this, "Item already exists", Toast.LENGTH_SHORT).show());
                }
            });
        });
    }
}
