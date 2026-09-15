package com.schoolapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {
    private static final String PREFS_NAME = "schoolapp_prefs";

    // Keys
    public static final String KEY_USER_ID         = "user_id";
    public static final String KEY_USER_NAME       = "user_name";
    public static final String KEY_USER_EMAIL      = "user_email";
    public static final String KEY_USER_ROLE       = "user_role";
    public static final String KEY_USER_TOKEN      = "user_token";
    public static final String KEY_IS_LOGGED_IN    = "is_logged_in";
    public static final String KEY_SELECTED_CHILD  = "selected_child_id";
    public static final String KEY_THEME_MODE      = "theme_mode";
    public static final String KEY_NOTIF_GRADES    = "notif_grades";
    public static final String KEY_NOTIF_ABSENCES  = "notif_absences";
    public static final String KEY_NOTIF_MESSAGES  = "notif_messages";
    public static final String KEY_LANGUAGE        = "language";
    public static final String KEY_FIRST_LAUNCH    = "first_launch";
    public static final String KEY_SCHOOL_LAT      = "school_lat";
    public static final String KEY_SCHOOL_LNG      = "school_lng";
    public static final String KEY_SCHOOL_NAME     = "school_name";
    public static final String KEY_SCHOOL_ADDRESS  = "school_address";

    // Roles
    public static final String ROLE_PARENT  = "PARENT";
    public static final String ROLE_STUDENT = "STUDENT";

    // Themes
    public static final String THEME_LIGHT  = "LIGHT";
    public static final String THEME_DARK   = "DARK";
    public static final String THEME_SYSTEM = "SYSTEM";

    private static PrefsManager instance;
    private final SharedPreferences prefs;

    public static synchronized PrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrefsManager(context.getApplicationContext());
        }
        return instance;
    }

    private PrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // ─── Auth ──────────────────────────────────────────────────────────────────
    public void saveUserSession(int userId, String name, String email, String role, String token) {
        prefs.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_USER_ROLE, role)
                .putString(KEY_USER_TOKEN, token)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }

    public void clearSession() {
        prefs.edit()
                .remove(KEY_USER_ID)
                .remove(KEY_USER_NAME)
                .remove(KEY_USER_EMAIL)
                .remove(KEY_USER_ROLE)
                .remove(KEY_USER_TOKEN)
                .putBoolean(KEY_IS_LOGGED_IN, false)
                .apply();
    }

    public boolean isLoggedIn()        { return prefs.getBoolean(KEY_IS_LOGGED_IN, false); }
    public int    getUserId()          { return prefs.getInt(KEY_USER_ID, 1); }
    public String getUserName()        { return prefs.getString(KEY_USER_NAME, ""); }
    public String getUserEmail()       { return prefs.getString(KEY_USER_EMAIL, ""); }
    public String getUserRole()        { return prefs.getString(KEY_USER_ROLE, ROLE_PARENT); }
    public String getUserToken()       { return prefs.getString(KEY_USER_TOKEN, ""); }
    public boolean isParent()          { return ROLE_PARENT.equals(getUserRole()); }

    // ─── Selected Child ────────────────────────────────────────────────────────
    public void setSelectedChild(int childId) {
        prefs.edit().putInt(KEY_SELECTED_CHILD, childId).apply();
    }
    public int getSelectedChildId() { return prefs.getInt(KEY_SELECTED_CHILD, 1); }

    // ─── Settings ──────────────────────────────────────────────────────────────
    public void setThemeMode(String mode) {
        prefs.edit().putString(KEY_THEME_MODE, mode).apply();
    }
    public String getThemeMode()     { return prefs.getString(KEY_THEME_MODE, THEME_LIGHT); }

    public void setNotifGrades(boolean v)   { prefs.edit().putBoolean(KEY_NOTIF_GRADES, v).apply(); }
    public boolean isNotifGrades()          { return prefs.getBoolean(KEY_NOTIF_GRADES, true); }
    public void setNotifAbsences(boolean v) { prefs.edit().putBoolean(KEY_NOTIF_ABSENCES, v).apply(); }
    public boolean isNotifAbsences()        { return prefs.getBoolean(KEY_NOTIF_ABSENCES, true); }
    public void setNotifMessages(boolean v) { prefs.edit().putBoolean(KEY_NOTIF_MESSAGES, v).apply(); }
    public boolean isNotifMessages()        { return prefs.getBoolean(KEY_NOTIF_MESSAGES, true); }

    public void setLanguage(String lang) { prefs.edit().putString(KEY_LANGUAGE, lang).apply(); }
    public String getLanguage()          { return prefs.getString(KEY_LANGUAGE, "fr"); }

    public boolean isFirstLaunch()       { return prefs.getBoolean(KEY_FIRST_LAUNCH, true); }
    public void setFirstLaunch(boolean v){ prefs.edit().putBoolean(KEY_FIRST_LAUNCH, v).apply(); }

    // ─── School Location ───────────────────────────────────────────────────────
    public void saveSchoolLocation(double lat, double lng, String name, String address) {
        prefs.edit()
                .putFloat(KEY_SCHOOL_LAT, (float) lat)
                .putFloat(KEY_SCHOOL_LNG, (float) lng)
                .putString(KEY_SCHOOL_NAME, name)
                .putString(KEY_SCHOOL_ADDRESS, address)
                .apply();
    }
    public double getSchoolLat()     { return prefs.getFloat(KEY_SCHOOL_LAT, 33.9989f); }
    public double getSchoolLng()     { return prefs.getFloat(KEY_SCHOOL_LNG, -6.8520f); }
    public String getSchoolName()    { return prefs.getString(KEY_SCHOOL_NAME, "Lycée Averroès"); }
    public String getSchoolAddress() { return prefs.getString(KEY_SCHOOL_ADDRESS, "Rabat, Maroc"); }

    // ─── Effacer toutes les données (reset complet) ────────────────────────────
    public void clearAll() {
        prefs.edit().clear().apply();
    }
}