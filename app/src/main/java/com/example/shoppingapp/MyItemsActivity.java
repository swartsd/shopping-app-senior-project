package com.example.shoppingapp;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.utils.StringUtils;

public class MyItemsActivity extends AppCompatActivity {

    private MyItemDao myItemDao;
    private MyItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        // Get DAO instance from the database
        ShoppingDatabase db = ShoppingDatabaseInstance.getInstance(getApplicationContext());
        myItemDao = db.myItemDao();

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMyItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyItemAdapter();
        recyclerView.setAdapter(adapter);

        // Observe data from the database
        myItemDao.getAllItems().observe(this, myItems -> adapter.setItems(myItems));

        // Set up the "Add Item" button with duplicate check logic
        Button addItemButton = findViewById(R.id.buttonAddItem);
        addItemButton.setOnClickListener(v -> showAddItemDialog());

        // Set up the "Clear List" button with confirmation
        Button clearListButton = findViewById(R.id.buttonClearList);
        clearListButton.setOnClickListener(v ->
                new AlertDialog.Builder(MyItemsActivity.this)
                        .setTitle("Clear List")
                        .setMessage("Are you sure you want to clear all items?")
                        .setPositiveButton("Yes", (dialog, which) ->
                                new Thread(() -> myItemDao.deleteAllItems()).start())
                        .setNegativeButton("No", null)
                        .show()
        );

        // Set up long press listener for Edit/Delete actions
        adapter.setOnItemLongClickListener(item ->
                new AlertDialog.Builder(MyItemsActivity.this)
                        .setTitle("Select Action")
                        .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
                            if (which == 0) { // Edit option selected
                                showEditItemDialog(item);
                            } else if (which == 1) { // Delete option selected
                                new AlertDialog.Builder(MyItemsActivity.this)
                                        .setTitle("Confirm Delete")
                                        .setMessage("Are you sure you want to delete this item?")
                                        .setPositiveButton("Yes", (confirmDialog, whichButton) ->
                                                new Thread(() -> myItemDao.delete(item)).start())
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        })
                        .show()
        );
    }

    // Method to show a dialog for adding a new item with duplicate check.
    private void showAddItemDialog() {
        final AddItemDialog dialog = new AddItemDialog(this);
        dialog.setOnItemSavedListener(itemName -> {
            String normalized = StringUtils.normalizeText(itemName);
            new Thread(() -> {
                MyItem existing = myItemDao.getItemByNormalizedName(normalized);
                if (existing == null) {
                    myItemDao.insert(new MyItem(itemName, normalized));
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(MyItemsActivity.this, "Item already exists.", Toast.LENGTH_SHORT).show()
                    );
                }
            }).start();
        });
        dialog.show();
    }

    // Method to show a dialog for editing an existing item with Save and Delete options.
    private void showEditItemDialog(MyItem item) {
        Dialog editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.dialog_edit_item);

        EditText etEditItemName = editDialog.findViewById(R.id.etEditItemName);
        Button btnSaveEdit = editDialog.findViewById(R.id.btnSaveEdit);
        Button btnDeleteEdit = editDialog.findViewById(R.id.btnDeleteEdit);

        // Pre-fill with current item name
        etEditItemName.setText(item.getItemName());

        btnSaveEdit.setOnClickListener(v -> {
            String newName = etEditItemName.getText().toString().trim();
            if (!newName.isEmpty()) {
                String normalized = StringUtils.normalizeText(newName);
                new Thread(() -> {
                    MyItem duplicate = myItemDao.getItemByNormalizedName(normalized);
                    if (duplicate != null && duplicate.getId() != item.getId()) {
                        runOnUiThread(() ->
                                Toast.makeText(MyItemsActivity.this, "Another item with that name exists.", Toast.LENGTH_SHORT).show()
                        );
                    } else {
                        item.setItemName(newName);
                        item.setNormalizedName(normalized);
                        myItemDao.update(item);
                        editDialog.dismiss();
                    }
                }).start();
            }
        });

        btnDeleteEdit.setOnClickListener(v ->
                new AlertDialog.Builder(MyItemsActivity.this)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", (confirmDialog, whichButton) -> {
                            new Thread(() -> myItemDao.delete(item)).start();
                            editDialog.dismiss();
                        })
                        .setNegativeButton("No", null)
                        .show()
        );

        editDialog.show();
    }
}
