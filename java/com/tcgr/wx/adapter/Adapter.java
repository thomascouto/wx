package com.tcgr.wx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;

public abstract class Adapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    SparseBooleanArray selectedItems;
    Context context;

    Adapter() {
        this.selectedItems = new SparseBooleanArray();
    }

    public abstract ArrayList<?> getList();

    public abstract String getShareableAdapter();

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }
}