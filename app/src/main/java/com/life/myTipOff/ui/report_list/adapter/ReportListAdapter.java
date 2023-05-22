package com.life.myTipOff.ui.report_list.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.life.myTipOff.databinding.ViewholderReportListItemBinding;
import com.life.myTipOff.model.Report;
import com.life.myTipOff.ui.report_list.ReportListListener;
import com.life.myTipOff.ui.report_list.viewholder.ReportListViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListViewHolder> {
    private List<Report> items = new ArrayList();
    private ReportListListener listener;

    public ReportListAdapter(ReportListListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReportListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReportListViewHolder(
                ViewholderReportListItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ReportListViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Report> items) {
        this.items = items;
    }
}
