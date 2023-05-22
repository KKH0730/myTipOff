package com.life.myTipOff.ui.send_mail.viewholder;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.life.myTipOff.databinding.ViewholderAttachDocumentItemBinding;
import com.life.myTipOff.model.AttachItem;
import com.life.myTipOff.ui.send_mail.AttachListener;

public class AttachViewHolder extends RecyclerView.ViewHolder {
    private ViewholderAttachDocumentItemBinding binding;
    private AttachListener listener;

    public AttachViewHolder(ViewholderAttachDocumentItemBinding binding, AttachListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public void bind(AttachItem data) {
        Log.e("kkh", "data : " + data);
        binding.ivClose.setOnClickListener(v -> listener.removeAttachFile(getAdapterPosition()));
    }
}
