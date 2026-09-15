package com.schoolapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Firebase Cloud Messaging stub.
 * Replace this with FirebaseMessagingService if you add Firebase to the project.
 * Add to build.gradle: implementation 'com.google.firebase:firebase-messaging:24.0.0'
 */
public class SchoolMessagingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Handle FCM push payload here when Firebase is integrated
        // Example: show grade/absence/message notification
        return START_NOT_STICKY;
    }
}
