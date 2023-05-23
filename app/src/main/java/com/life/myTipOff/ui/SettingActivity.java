package com.life.myTipOff.ui;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.databinding.DataBindingUtil;

import com.life.myTipOff.R;
import com.life.myTipOff.databinding.ActivitySettingBinding;
import com.life.myTipOff.service.NotiService;
import com.life.myTipOff.ui.report_list.ReportListActivity;

import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;

    private ActivityResultLauncher<Intent> overlayActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (Settings.canDrawOverlays(this)) {
            binding.flPermissionButton.setVisibility(View.GONE);
            binding.swBackground.setChecked(true);
        } else {
            binding.flPermissionButton.setVisibility(View.VISIBLE);
            binding.swBackground.setChecked(false);
            Toast.makeText(this, getString(R.string.setting_use_background_permission_guide), Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setActivity(this);

        init();
        setVersionInfo();
        setSwitchListener();

        binding.flReportListButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ReportListActivity.class));
        });
    }

    @SuppressLint("ClickableViewAccessibility")

    private void init() {
        if (Settings.canDrawOverlays(this)) {
            binding.flPermissionButton.setVisibility(View.GONE);
            binding.swBackground.setEnabled(true);
            binding.swBackground.setChecked(NotiService.isShowOverlayView);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setVersionInfo() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            String versionName = packageInfo.versionName;
            binding.tvVersion.setText("v "  + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setSwitchListener() {
        binding.swBackground.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotiService.isShowOverlayView = isChecked;

            if (isChecked) {
                startForegroundService();
            } else {
                stopForegroundService();
            }
        });
    }

    private void reqPermission() {
        if (Settings.canDrawOverlays(this)) {
            startForegroundService();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            overlayActivityResult.launch(intent);
        }
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, NotiService.class);
        serviceIntent.setAction("start");
        startForegroundService(serviceIntent);
    }

    private void stopForegroundService() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NotiService.NOTIFICATION_ID);

        Intent intent = new Intent(this, NotiService.class);
        intent.setAction("stop");
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        if (binding != null) {
            binding.unbind();
            binding = null;
        }
        super.onDestroy();
    }

    public void clickBack() {
        finish();
    }

    public void clickPermissionButton() {
        reqPermission();
    }
}
