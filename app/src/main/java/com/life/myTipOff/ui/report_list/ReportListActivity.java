package com.life.myTipOff.ui.report_list;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.life.myTipOff.R;
import com.life.myTipOff.databinding.ActivityReportListBinding;
import com.life.myTipOff.db.AppDatabase;
import com.life.myTipOff.model.Report;
import com.life.myTipOff.ui.report_list.adapter.ReportListAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportListActivity extends AppCompatActivity {
    private ActivityReportListBinding binding;
    private AppDatabase db;
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_list);
        binding.setActivity(this);

        init();
        setReportListRecyclerView();
    }

    private void init() {
        db = AppDatabase.getInstance(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setReportListRecyclerView() {
        binding.rvReport.setAdapter(new ReportListAdapter(report -> {

        }));

        executorService.execute(() -> {
            List<Report> reportList = db.reportDao().getAll();

            for (Report report : reportList) {
                Log.e("kkh","content : " + report.getContent() + "date : " + report.getDate());
            }

            runOnUiThread(() -> {
                Log.e("kkh","333");
                ((ReportListAdapter) binding.rvReport.getAdapter()).setItems(reportList);
                binding.rvReport.getAdapter().notifyDataSetChanged();
            });
        });
    }
}
