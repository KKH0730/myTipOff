package com.life.myTipOff.ui;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.life.myTipOff.R;
import com.life.myTipOff.databinding.ActivityMainBinding;
import com.life.myTipOff.databinding.DialogMedaiListBinding;
import com.life.myTipOff.ui.send_mail.SendEmailActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private ActivityMainBinding binding;
    private AlertDialog mediaListDialog;

//    public static Boolean isShowOverlayView = false;
//
//    private int dx = 0;
//    private int dy = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);


        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTasks) {
            if (appTask.getTaskInfo().topActivity.getPackageName().equals(PACKAGE_NAME)) {
                appTask.finishAndRemoveTask();
            }
        }

        setBackgroundImage();
    }

    private void setBackgroundImage() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.ivBackground.setImageResource(R.drawable.img_dark_mode_background);
        } else {
            binding.ivBackground.setImageResource(R.drawable.img_light_mode_background);
        }
    }

    private void showMediaListDialog() {
        if (mediaListDialog == null) {
            DialogMedaiListBinding dialogMedaiListBinding = DialogMedaiListBinding.inflate(LayoutInflater.from(getApplicationContext()));

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(dialogMedaiListBinding.getRoot());
            mediaListDialog = builder.create();
        }
        mediaListDialog.show();

        mediaListDialog.findViewById(R.id.clKbs).setOnClickListener(v -> {
            Intent intent = new Intent(this, SendEmailActivity.class);
            intent.putExtra(SendEmailActivity.PARAM_MEDIA, SendEmailActivity.PARAM_KBS);
            startActivity(intent);
        });

        mediaListDialog.findViewById(R.id.clMbc).setOnClickListener(v -> {
            Intent intent = new Intent(this, SendEmailActivity.class);
            intent.putExtra(SendEmailActivity.PARAM_MEDIA, SendEmailActivity.PARAM_MBC);
            startActivity(intent);
        });

        mediaListDialog.findViewById(R.id.clSbs).setOnClickListener(v -> {
            Intent intent = new Intent(this, SendEmailActivity.class);
            intent.putExtra(SendEmailActivity.PARAM_MEDIA, SendEmailActivity.PARAM_SBS);
            startActivity(intent);
        });

        mediaListDialog.findViewById(R.id.clJtbc).setOnClickListener(v -> {
            Intent intent = new Intent(this, SendEmailActivity.class);
            intent.putExtra(SendEmailActivity.PARAM_MEDIA, SendEmailActivity.PARAM_JTBC);
            startActivity(intent);
        });

        mediaListDialog.findViewById(R.id.clYtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, SendEmailActivity.class);
            intent.putExtra(SendEmailActivity.PARAM_MEDIA, SendEmailActivity.PARAM_YTN);
            startActivity(intent);
        });

        mediaListDialog.findViewById(R.id.clYonhap).setOnClickListener(v -> {
            Intent intent = new Intent(this, SendEmailActivity.class);
            intent.putExtra(SendEmailActivity.PARAM_MEDIA, SendEmailActivity.PARAM_YONHAP);
            startActivity(intent);
        });
    }

    public void clickReport() {
        showMediaListDialog();
    }

    public void clickSetting() {
        if (mediaListDialog != null && mediaListDialog.isShowing()) {
            mediaListDialog.dismiss();
        }

        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    protected void onDestroy() {
        if (binding != null) {
            binding.unbind();
            binding = null;
        }
        super.onDestroy();
    }
}
