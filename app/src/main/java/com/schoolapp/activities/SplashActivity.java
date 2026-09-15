package com.schoolapp.activities;

import android.Manifest;
import android.animation.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.schoolapp.R;
import com.schoolapp.utils.NotificationHelper;
import com.schoolapp.utils.PrefsManager;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIF_PERMISSION = 200;
    private static final long SPLASH_DELAY = 2200;

    private Handler handler;
    private Runnable navigateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        NotificationHelper.createChannels(this);

        ImageView logo     = findViewById(R.id.img_logo);
        TextView  appName  = findViewById(R.id.tv_app_name);
        TextView  tagline  = findViewById(R.id.tv_tagline);
        View      dots     = findViewById(R.id.loading_dots);

        // Animate logo scale + fade
        logo.setAlpha(0f);
        logo.setScaleX(0.4f);
        logo.setScaleY(0.4f);
        logo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .start();

        // Animate app name
        appName.setAlpha(0f);
        appName.setTranslationY(30f);
        appName.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(400)
                .setDuration(500)
                .start();

        // Animate tagline
        tagline.setAlpha(0f);
        tagline.animate()
                .alpha(1f)
                .setStartDelay(700)
                .setDuration(400)
                .start();

        // Dots
        dots.setAlpha(0f);
        dots.animate()
                .alpha(1f)
                .setStartDelay(900)
                .setDuration(300)
                .start();

        handler = new Handler(Looper.getMainLooper());
        navigateRunnable = this::navigateNext;

        // ── Gestion de la permission notification (Android 13+) ────────────
        // Si la popup système doit s'afficher, on NE LANCE PAS le minuteur
        // du splash tout de suite : on attend la réponse de l'utilisateur
        // dans onRequestPermissionsResult() avant de continuer.
        // Si la permission est déjà accordée (ou sur API < 33), on lance
        // le minuteur normalement tout de suite.
        if (needsNotificationPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIF_PERMISSION);
            // Pas de postDelayed ici — on attend onRequestPermissionsResult()
        } else {
            handler.postDelayed(navigateRunnable, SPLASH_DELAY);
        }
    }

    private boolean needsNotificationPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU // API 33+
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIF_PERMISSION) {
            // Que l'utilisateur ait accepté ou refusé, on continue vers le
            // login/accueil — on ne bloque jamais l'app pour ça.
            handler.postDelayed(navigateRunnable, 300);
        }
    }

    private void navigateNext() {
        PrefsManager prefs = PrefsManager.getInstance(this);
        Intent intent;
        if (prefs.isLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && navigateRunnable != null) {
            handler.removeCallbacks(navigateRunnable);
        }
    }
}