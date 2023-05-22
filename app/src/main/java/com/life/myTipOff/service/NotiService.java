package com.life.myTipOff.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.life.myTipOff.R;
import com.life.myTipOff.ui.MainActivity;

public class NotiService extends Service {
    public static final String CHANNEL_ID = "379";
    public static final int NOTIFICATION_ID = 1000;

    public static View overlayView;
    public static Boolean isShowOverlayView = false;

    private int dx = 0;
    private int dy = 0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            if (overlayView == null) {
                setOverlayView(windowManager);
            }

            if (intent.getAction().equals("start")) {
                showOverlayView(windowManager);

                createNotificationChannel(this);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                new Intent(this, MainActivity.class),
                                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
                        );

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT);
                builder.setContentTitle("앱 실행중");
                builder.setContentText("현재 앱이 실행중입니다.");
                builder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
                builder.setDefaults(NotificationCompat.FLAG_FOREGROUND_SERVICE);
                builder.setContentIntent(pendingIntent);
                startForeground(NOTIFICATION_ID, builder.build());
            } else if (intent.getAction().equals("stop")){
                hideOverlayView(windowManager);

                stopForeground(true);
                stopSelf();
            } else {
                startActivity();
            }
        }
        return START_STICKY;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOverlayView(WindowManager windowManager) {
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_view, null);
        overlayView.setOnTouchListener((v, event) -> {
            try {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) NotiService.overlayView.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = (int) (layoutParams.x - event.getRawX());
                        dy = (int) (layoutParams.y - event.getRawY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = (int) (dx + event.getRawX());
                        layoutParams.y = (int) (dy + event.getRawY());

                        windowManager.updateViewLayout(NotiService.overlayView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                hideOverlayView(windowManager);
            }

            return false;
        });
        overlayView.setOnClickListener(v -> {
            startActivity();
        });
    }

    private void showOverlayView(WindowManager windowManager) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // 다른 앱 위에 그리기 권한이 필요한 유형
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        windowManager.addView(NotiService.overlayView, params); // 뷰 추가
        isShowOverlayView = true;
    }


    private void hideOverlayView(WindowManager windowManager) {
        try {
            windowManager.removeView(NotiService.overlayView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void createNotificationChannel(Context context) {
        String name = "notification alarm";
        String description = "notification alarm";

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(description);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

}
