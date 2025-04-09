package com.example.shoppingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ItemViewHolder> {

    private List<ShoppingItem> itemList;
    private final Context context;
    private float salesTax; // current sales tax value
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onDelete(ShoppingItem item);
        void onEdit(ShoppingItem item);
    }

    public ShoppingItemAdapter(Context context, List<ShoppingItem> itemList, float salesTax, OnItemActionListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.salesTax = salesTax;
        this.listener = listener;
    }

    /**
     * Updates the adapter's data using DiffUtil to compute the minimal changes.
     */
    public void setItems(List<ShoppingItem> newItems) {
        if (this.itemList == null) {
            this.itemList = newItems;
            notifyItemRangeInserted(0, newItems.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ShoppingItemDiffCallback(this.itemList, newItems));
            this.itemList = newItems;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    // Setter to update salesTax when it changes.
    public void setSalesTax(float salesTax) {
        this.salesTax = salesTax;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shopping, parent, false);
        return new ItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ShoppingItem item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    // Make the ViewHolder class public static.
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemName, tvItemTotal, tvItemDetails;
        private final ShoppingItemAdapter adapter;

        public ItemViewHolder(@NonNull View itemView, ShoppingItemAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemTotal = itemView.findViewById(R.id.tvItemTotal);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetails);

            // Set a long click listener on the entire item view.
            itemView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Select Action")
                        .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
                            // Use getBindingAdapterPosition() instead of the deprecated getAdapterPosition()
                            int position = getBindingAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                if (which == 0) { // Edit option
                                    adapter.listener.onEdit(adapter.itemList.get(position));
                                } else if (which == 1) { // Delete option
                                    new AlertDialog.Builder(itemView.getContext())
                                            .setTitle("Confirm Delete")
                                            .setMessage("Are you sure you want to delete this item?")
                                            .setPositiveButton("Yes", (d, w) -> adapter.listener.onDelete(adapter.itemList.get(position)))
                                            .setNegativeButton("Cancel", null)
                                            .show();
                                }
                            }
                        }).show();
                return true;
            });
        }

        void bind(ShoppingItem item) {
            tvItemName.setText(item.getItemName());
            // Calculate base total, tax amount, and final total.
            float baseTotal = item.getPricePerUnit() * item.getUnit();
            float taxAmount = baseTotal * adapter.salesTax;
            float total = baseTotal + taxAmount;
            String totalFormatted = NumberFormat.getCurrencyInstance().format(total);
            tvItemTotal.setText(totalFormatted);

            // Set additional details: price per unit and unit.
            String details = "Price per unit: " + NumberFormat.getCurrencyInstance().format(item.getPricePerUnit())
                    + ", Unit: " + item.getUnit();
            tvItemDetails.setText(details);
        }
    }

    /**
     * A DiffUtil.Callback implementation for comparing ShoppingItem lists.
     */
    public static class ShoppingItemDiffCallback extends DiffUtil.Callback {

        private final List<ShoppingItem> oldList;
        private final List<ShoppingItem> newList;

        public ShoppingItemDiffCallback(List<ShoppingItem> oldList, List<ShoppingItem> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // Compare by unique ID.
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ShoppingItem oldItem = oldList.get(oldItemPosition);
            ShoppingItem newItem = newList.get(newItemPosition);
            return oldItem.getItemName().equals(newItem.getItemName())
                    && oldItem.getPricePerUnit() == newItem.getPricePerUnit()
                    && oldItem.getUnit() == newItem.getUnit();
        }
    }
}
