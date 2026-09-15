package com.schoolapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.schoolapp.R;
import com.schoolapp.activities.MainActivity;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.*;
import com.schoolapp.utils.PrefsManager;
import java.util.*;

public class SchoolWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] widgetIds) {
        for (int widgetId : widgetIds) {
            updateWidget(context, manager, widgetId);
        }
    }

    static void updateWidget(Context context, AppWidgetManager manager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_school);

        PrefsManager prefs = PrefsManager.getInstance(context);
        DatabaseHelper db  = DatabaseHelper.getInstance(context);

        int studentId = prefs.getSelectedChildId();
        Student student = db.getStudent(studentId);

        if (student != null) {
            views.setTextViewText(R.id.widget_student_name, student.getFullName());

            // Compute average
            List<Grade> grades = db.getGradesForStudent(studentId);
            float avg = 0f;
            if (!grades.isEmpty()) {
                float total = 0f, coef = 0f;
                for (Grade g : grades) {
                    total += (g.getValue() / g.getMaxValue()) * 20 * g.getCoefficient();
                    coef  += g.getCoefficient();
                }
                avg = coef > 0 ? total / coef : 0f;
            }
            views.setTextViewText(R.id.widget_average,
                    String.format(Locale.FRENCH, "%.1f", avg));

            // Next class today
            Calendar cal = Calendar.getInstance();
            int todayIdx = cal.get(Calendar.DAY_OF_WEEK) - 2;
            if (todayIdx < 0 || todayIdx > 4) todayIdx = 0;
            List<ScheduleSlot> slots = db.getScheduleForStudentAndDay(studentId, todayIdx);
            String nextClass = "—";
            String currentTime = String.format(Locale.FRENCH, "%02d:%02d",
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
            for (ScheduleSlot s : slots) {
                if (s.getStartTime().compareTo(currentTime) > 0) {
                    nextClass = s.getSubject() + " " + s.getStartTime();
                    break;
                }
            }
            views.setTextViewText(R.id.widget_next_class, nextClass);
        }

        // Date
        String today = new java.text.SimpleDateFormat("EEE dd MMM", Locale.FRENCH)
                .format(new Date());
        views.setTextViewText(R.id.widget_date, today);

        // Tap → open app
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_student_name, pi);

        manager.updateAppWidget(widgetId, views);
    }
}
