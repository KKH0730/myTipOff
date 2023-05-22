package com.life.myTipOff.ui.send_mail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.life.myTipOff.databinding.ViewholderAttachDocumentItemBinding;
import com.life.myTipOff.model.AttachItem;
import com.life.myTipOff.ui.send_mail.viewholder.AttachViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AttachAdapter extends RecyclerView.Adapter<AttachViewHolder> {
    private ArrayList<AttachItem> items = new ArrayList<AttachItem>();
    private AttachListener listener;

    public void setOnAttachListener(AttachListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AttachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttachViewHolder(
                ViewholderAttachDocumentItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AttachViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(AttachItem item) {
        items.add(item);
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    public List<AttachItem> getItems() {
        return items;
    }
}

