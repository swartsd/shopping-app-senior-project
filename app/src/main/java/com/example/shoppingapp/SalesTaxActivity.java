package com.example.shoppingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SalesTaxActivity extends AppCompatActivity {

    private EditText etTax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_tax);

        etTax = findViewById(R.id.etTax);
        Button btnSaveTax = findViewById(R.id.btnSaveTax);

        btnSaveTax.setOnClickListener(view -> {
            String taxInput = etTax.getText().toString();
            if (!taxInput.isEmpty()) {
                // Remove any '%' character and trim extra spaces.
                taxInput = taxInput.replace("%", "").trim();
                try {
                    // Parse the percentage input.
                    float taxPercentage = Float.parseFloat(taxInput);
                    // Convert to a decimal by dividing by 100.
                    float salesTax = taxPercentage / 100f;
                    // Save the sales tax value using SharedPreferences.
                    SharedPreferences prefs = getSharedPreferences("ShoppingAppPrefs", MODE_PRIVATE);
                    prefs.edit().putFloat("sales_tax", salesTax).apply();
                    Toast.makeText(SalesTaxActivity.this, "Sales Tax set to " + salesTax, Toast.LENGTH_SHORT).show();
                    // Automatically return to the previous activity (MainActivity)
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(SalesTaxActivity.this, "Invalid sales tax value", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SalesTaxActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
