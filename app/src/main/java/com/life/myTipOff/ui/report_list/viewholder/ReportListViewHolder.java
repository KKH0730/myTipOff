package com.life.myTipOff.ui.report_list.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.life.myTipOff.databinding.ViewholderReportListItemBinding;
import com.life.myTipOff.model.Report;
import com.life.myTipOff.ui.report_list.ReportListListener;

public class ReportListViewHolder extends RecyclerView.ViewHolder {
    private ViewholderReportListItemBinding binding;
    private ReportListListener listener;

    public ReportListViewHolder(ViewholderReportListItemBinding binding, ReportListListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public void bind(Report data) {
        binding.setData(data);
    }
}
