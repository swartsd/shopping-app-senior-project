package com.example.shoppingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

    public void setItems(List<ShoppingItem> newItems) {
        if (this.itemList == null) {
            this.itemList = newItems;
            notifyItemRangeInserted(0, newItems.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                    new ShoppingItemDiffCallback(this.itemList, newItems));
            this.itemList = newItems;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    public void setSalesTax(float salesTax) {
        this.salesTax = salesTax;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_shopping, parent, false);
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemName, tvItemTotal, tvItemDetails;
        private final ColorStateList defaultTextColors;
        private final View container;
        private final ShoppingItemAdapter adapter;

        public ItemViewHolder(@NonNull View itemView, ShoppingItemAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            tvItemName    = itemView.findViewById(R.id.tvItemName);
            tvItemTotal   = itemView.findViewById(R.id.tvItemTotal);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetails);
            container     = itemView.findViewById(R.id.itemContainer);

            // Resolve and cache the theme's primary text color
            TypedValue tv = new TypedValue();
            Context ctx = itemView.getContext();
            ctx.getTheme().resolveAttribute(android.R.attr.textColorPrimary, tv, true);
            defaultTextColors = ContextCompat.getColorStateList(ctx, tv.resourceId);

            // Long-press actions
            itemView.setOnLongClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ShoppingItem currentItem = adapter.itemList.get(position);
                    String toggleOption = currentItem.isInCart() ? "Remove from Cart" : "Add to Cart";
                    String[] options = {"Edit", "Delete", toggleOption};

                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Select Action")
                            .setItems(options, (dialog, which) -> {
                                if (which == 0) {
                                    adapter.listener.onEdit(currentItem);
                                } else if (which == 1) {
                                    new AlertDialog.Builder(itemView.getContext())
                                            .setTitle("Confirm Delete")
                                            .setMessage("Are you sure you want to delete this item?")
                                            .setPositiveButton("Yes", (d, w) -> adapter.listener.onDelete(currentItem))
                                            .setNegativeButton("Cancel", null)
                                            .show();
                                } else {
                                    currentItem.setInCart(!currentItem.isInCart());
                                    new Thread(() -> {
                                        ShoppingDatabaseInstance.getInstance(itemView.getContext())
                                                .shoppingItemDao().update(currentItem);
                                        itemView.post(() -> adapter.notifyItemChanged(getBindingAdapterPosition()));
                                    }).start();
                                }
                            }).show();
                }
                return true;
            });
        }

        void bind(ShoppingItem item) {
            // Set texts
            tvItemName   .setText(item.getItemName());
            float baseTotal = item.getPricePerUnit() * item.getUnit();
            float taxAmount = baseTotal * adapter.salesTax;
            float total = baseTotal + taxAmount;
            String totalFormatted = NumberFormat.getCurrencyInstance().format(total);
            tvItemTotal.setText(totalFormatted);

            String details = "Price per unit: "
                    + NumberFormat.getCurrencyInstance().format(item.getPricePerUnit())
                    + ", Unit: " + item.getUnit();
            tvItemDetails.setText(details);

            if (item.isInCart()) {
                int fadeColor = ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray);
                tvItemName   .setTextColor(fadeColor);
                tvItemTotal  .setTextColor(fadeColor);
                tvItemDetails.setTextColor(fadeColor);

                tvItemName.setPaintFlags(tvItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                container.setBackgroundResource(R.drawable.item_highlight_background);
            } else {
                // Reset background & strike-through
                container.setBackgroundColor(Color.TRANSPARENT);
                tvItemName.setPaintFlags(tvItemName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

                // Restore themed primary text color (white in dark mode, black in light)
                tvItemName   .setTextColor(defaultTextColors);
                tvItemTotal  .setTextColor(defaultTextColors);
                tvItemDetails.setTextColor(defaultTextColors);
            }
        }
    }

    public static class ShoppingItemDiffCallback extends DiffUtil.Callback {
        private final List<ShoppingItem> oldList;
        private final List<ShoppingItem> newList;

        public ShoppingItemDiffCallback(List<ShoppingItem> oldList, List<ShoppingItem> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() { return oldList.size(); }

        @Override
        public int getNewListSize() { return newList.size(); }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ShoppingItem oldItem = oldList.get(oldItemPosition);
            ShoppingItem newItem = newList.get(newItemPosition);
            return oldItem.getItemName().equals(newItem.getItemName())
                    && oldItem.getPricePerUnit() == newItem.getPricePerUnit()
                    && oldItem.getUnit() == newItem.getUnit()
                    && oldItem.isInCart() == newItem.isInCart();
        }
    }
}
