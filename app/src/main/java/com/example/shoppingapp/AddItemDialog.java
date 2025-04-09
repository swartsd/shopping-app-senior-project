package com.example.shoppingapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddItemDialog extends Dialog {

    private EditText editTextItemName;
    private OnItemSavedListener listener;

    public interface OnItemSavedListener {
        void onItemSaved(String itemName);
    }

    public void setOnItemSavedListener(OnItemSavedListener listener) {
        this.listener = listener;
    }

    public AddItemDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_item);

        // Ensure these IDs match the ones defined in dialog_add_item.xml
        editTextItemName = findViewById(R.id.editTextItemName);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(view -> {
            String itemName = editTextItemName.getText().toString().trim();
            if (!itemName.isEmpty()) {
                if (listener != null) {
                    listener.onItemSaved(itemName);
                }
                dismiss();
            }
        });

        buttonCancel.setOnClickListener(view -> dismiss());
    }
}
