package com.schoolapp.utils;

import android.content.Context;
import com.schoolapp.R;

public class SubjectColorUtils {

    public static int getColorRes(String subject) {
        if (subject == null) return R.color.subject_art;
        switch (subject.toLowerCase()) {
            case "mathématiques": case "maths": return R.color.subject_math;
            case "français":      return R.color.subject_french;
            case "physique":      case "physique-chimie": return R.color.subject_science;
            case "svt":           case "biologie": return R.color.subject_science;
            case "histoire":      case "histoire-géo": return R.color.subject_history;
            case "anglais":       case "espagnol": case "allemand": return R.color.subject_english;
            case "eps":           return R.color.subject_sport;
            case "arts plastiques": case "arts": return R.color.subject_art;
            case "musique":       return R.color.subject_music;
            default:              return R.color.primary_light;
        }
    }

    public static int getColor(Context context, String subject) {
        return context.getResources().getColor(getColorRes(subject), context.getTheme());
    }

    public static int getGradeColor(Context context, float value, float max) {
        float norm = (value / max) * 20;
        if (norm >= 16) return context.getColor(R.color.grade_excellent);
        if (norm >= 12) return context.getColor(R.color.grade_good);
        if (norm >= 10) return context.getColor(R.color.grade_average);
        return context.getColor(R.color.grade_poor);
    }
}
