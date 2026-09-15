package com.schoolapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.schoolapp.R;
import com.schoolapp.activities.MainActivity;

public class NotificationHelper {

    public static final String CHANNEL_GRADES    = "channel_grades";
    public static final String CHANNEL_ABSENCES  = "channel_absences";
    public static final String CHANNEL_MESSAGES  = "channel_messages";

    private static final int NOTIF_ID_GRADE   = 1001;
    private static final int NOTIF_ID_ABSENCE = 1002;
    private static final int NOTIF_ID_MESSAGE = 1003;

    public static void createChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = context.getSystemService(NotificationManager.class);

            nm.createNotificationChannel(new NotificationChannel(
                    CHANNEL_GRADES,
                    context.getString(R.string.notif_channel_grades),
                    NotificationManager.IMPORTANCE_HIGH));

            nm.createNotificationChannel(new NotificationChannel(
                    CHANNEL_ABSENCES,
                    context.getString(R.string.notif_channel_absences),
                    NotificationManager.IMPORTANCE_HIGH));

            nm.createNotificationChannel(new NotificationChannel(
                    CHANNEL_MESSAGES,
                    context.getString(R.string.notif_channel_messages),
                    NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    public static void showGradeNotification(Context context, String studentName,
                                              String subject, float grade) {
        PrefsManager prefs = PrefsManager.getInstance(context);
        if (!prefs.isNotifGrades()) return;

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tab", "grades");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_GRADES)
                .setSmallIcon(R.drawable.ic_grade)
                .setContentTitle("Nouvelle note — " + studentName)
                .setContentText(subject + " : " + String.format("%.1f/20", grade))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pi);

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_ID_GRADE, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void showAbsenceNotification(Context context, String studentName,
                                                String subject, String date) {
        PrefsManager prefs = PrefsManager.getInstance(context);
        if (!prefs.isNotifAbsences()) return;

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tab", "absences");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ABSENCES)
                .setSmallIcon(R.drawable.ic_absence)
                .setContentTitle("Absence signalée — " + studentName)
                .setContentText(subject + " le " + date)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pi);

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_ID_ABSENCE, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void showMessageNotification(Context context, String senderName, String preview) {
        PrefsManager prefs = PrefsManager.getInstance(context);
        if (!prefs.isNotifMessages()) return;

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("tab", "messages");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_MESSAGES)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Message de " + senderName)
                .setContentText(preview)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pi);

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_ID_MESSAGE, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
