package com.schoolapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.utils.NotificationHelper;
import com.schoolapp.utils.PrefsManager;

public class NotificationService extends Service {

    private Handler handler;
    private Runnable checkRunnable;
    private static final long POLL_INTERVAL = 30_000L; // 30s

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        checkRunnable = new Runnable() {
            @Override
            public void run() {
                checkForUpdates();
                handler.postDelayed(this, POLL_INTERVAL);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(checkRunnable);
        return START_STICKY;
    }

    private void checkForUpdates() {
        PrefsManager prefs = PrefsManager.getInstance(this);
        if (!prefs.isLoggedIn()) return;

        int unread = DatabaseHelper.getInstance(this)
                .getUnreadMessageCount(prefs.getUserId());
        if (unread > 0 && prefs.isNotifMessages()) {
            // Notifications for unread messages already handled at message insert
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }
}
