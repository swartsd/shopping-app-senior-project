package com.example.shoppingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectableMyItemAdapter extends RecyclerView.Adapter<SelectableMyItemAdapter.ViewHolder> {

    private List<MyItem> items = new ArrayList<>();
    // Track the selection state for each item.
    private final List<Boolean> selectedStates = new ArrayList<>();

    // Call this to update the displayed list using DiffUtil for efficiency.
    public void setItems(List<MyItem> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return items.size();
            }

            @Override
            public int getNewListSize() {
                return newItems.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                // Assuming MyItem has a unique ID field.
                return items.get(oldItemPosition).getId() == newItems.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                MyItem oldItem = items.get(oldItemPosition);
                MyItem newItem = newItems.get(newItemPosition);
                // You can add more detailed comparison if needed.
                return oldItem.getItemName().equals(newItem.getItemName());
            }
        });
        // Update the items list.
        items = new ArrayList<>(newItems);
        // Reinitialize selection states (default false for each item).
        selectedStates.clear();
        for (int i = 0; i < newItems.size(); i++) {
            selectedStates.add(false);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    // Returns the list of selected MyItem objects.
    public List<MyItem> getSelectedItems() {
        List<MyItem> selectedItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (selectedStates.get(i)) {
                selectedItems.add(items.get(i));
            }
        }
        return selectedItems;
    }

    // Method to select all items.
    public void selectAll() {
        Collections.fill(selectedStates, true);
        // Notify that the entire range has changed.
        notifyItemRangeChanged(0, selectedStates.size());
    }

    // Method to unselect all items.
    public void unselectAll() {
        Collections.fill(selectedStates, false);
        // Notify that the entire range has changed.
        notifyItemRangeChanged(0, selectedStates.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout that includes a checkbox.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_item_selectable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyItem currentItem = items.get(position);
        holder.tvItemName.setText(currentItem.getItemName());
        holder.checkBox.setChecked(selectedStates.get(position));

        // Toggle selection when the checkbox is clicked.
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> selectedStates.set(position, isChecked));

        // Optional: toggle checkbox when the whole item is clicked.
        holder.itemView.setOnClickListener(v -> {
            boolean currentState = selectedStates.get(position);
            selectedStates.set(position, !currentState);
            holder.checkBox.setChecked(!currentState);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Make the ViewHolder class public.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvItemName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxSelect);
            tvItemName = itemView.findViewById(R.id.tvItemNameSelectable);
        }
    }
}
