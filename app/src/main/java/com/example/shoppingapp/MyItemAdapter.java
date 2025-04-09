package com.example.shoppingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyItemViewHolder> {

    private List<MyItem> items = new ArrayList<>();
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(MyItem item);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_item, parent, false);
        return new MyItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {
        MyItem currentItem = items.get(position);
        holder.textViewName.setText(currentItem.getItemName());
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(currentItem);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Use DiffUtil to update the list more efficiently.
     */
    public void setItems(List<MyItem> newItems) {
        if (this.items == null || this.items.isEmpty()) {
            this.items = newItems;
            notifyItemRangeInserted(0, newItems.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyItemDiffCallback(this.items, newItems));
            this.items = newItems;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    // Make the ViewHolder class public to expose it properly.
    public static class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewItemName);
        }
    }

    /**
     * DiffUtil.Callback implementation for comparing MyItem lists.
     */
    public static class MyItemDiffCallback extends DiffUtil.Callback {
        private final List<MyItem> oldList;
        private final List<MyItem> newList;

        public MyItemDiffCallback(List<MyItem> oldList, List<MyItem> newList) {
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
            // Assuming MyItem has a unique ID field, if available. Otherwise compare names.
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MyItem oldItem = oldList.get(oldItemPosition);
            MyItem newItem = newList.get(newItemPosition);
            return oldItem.getItemName().equals(newItem.getItemName());
        }
    }
}
