package com.schoolapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.schoolapp.models.*;
import com.schoolapp.utils.PrefsManager;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "schoolapp.db";
    private static final int DB_VERSION = 6; // ← incrémenté pour forcer onUpgrade

    // Tables
    public static final String TABLE_STUDENTS   = "students";
    public static final String TABLE_GRADES     = "grades";
    public static final String TABLE_SCHEDULE   = "schedule";
    public static final String TABLE_ABSENCES   = "absences";
    public static final String TABLE_MESSAGES   = "messages";
    public static final String TABLE_DOCUMENTS  = "documents";
    public static final String TABLE_FIELDTRIPS = "field_trips";
    public static final String TABLE_USERS      = "users";

    public static final String COL_ID = "id";

    // Students
    public static final String COL_FIRST_NAME    = "first_name";
    public static final String COL_LAST_NAME     = "last_name";
    public static final String COL_CLASS_NAME    = "class_name";
    public static final String COL_SCHOOL_NAME   = "school_name";
    public static final String COL_AVATAR_URL    = "avatar_url";
    public static final String COL_STUDENT_NUM   = "student_number";
    public static final String COL_GEN_AVERAGE   = "general_average";
    public static final String COL_TOTAL_ABS     = "total_absences";
    public static final String COL_TOTAL_LATE    = "total_late";

    // Grades
    public static final String COL_STUDENT_ID    = "student_id";
    public static final String COL_SUBJECT       = "subject";
    public static final String COL_VALUE         = "value";
    public static final String COL_MAX_VALUE     = "max_value";
    public static final String COL_COEFFICIENT   = "coefficient";
    public static final String COL_CLASS_AVG     = "class_average";
    public static final String COL_DESCRIPTION   = "description";
    public static final String COL_DATE          = "date";
    public static final String COL_TRIMESTER     = "trimester";
    public static final String COL_TEACHER       = "teacher";

    // Schedule
    public static final String COL_ROOM          = "room";
    public static final String COL_START_TIME    = "start_time";
    public static final String COL_END_TIME      = "end_time";
    public static final String COL_DAY_OF_WEEK   = "day_of_week";
    public static final String COL_DAY_INDEX     = "day_index";
    public static final String COL_IS_CANCELLED  = "is_cancelled";
    public static final String COL_COLOR_RES     = "color_res";

    // Absences
    public static final String COL_TYPE          = "type";
    public static final String COL_STATUS        = "status";
    public static final String COL_REASON        = "reason";

    // Messages
    public static final String COL_SENDER_ID     = "sender_id";
    public static final String COL_RECEIVER_ID   = "receiver_id";
    public static final String COL_SENDER_NAME   = "sender_name";
    public static final String COL_RECEIVER_NAME = "receiver_name";
    public static final String COL_CONTENT       = "content";
    public static final String COL_TIMESTAMP     = "timestamp";
    public static final String COL_IS_READ       = "is_read";
    public static final String COL_IS_SENT       = "is_sent_by_me";
    public static final String COL_MSG_SUBJECT   = "msg_subject";

    // Documents
    public static final String COL_NAME          = "name";
    public static final String COL_FILE_PATH     = "file_path";
    public static final String COL_FILE_SIZE     = "file_size";

    // FieldTrips
    public static final String COL_LATITUDE      = "latitude";
    public static final String COL_LONGITUDE     = "longitude";
    public static final String COL_ADDRESS       = "address";
    public static final String COL_ORGANIZER     = "organizer";

    // Users
    public static final String COL_EMAIL              = "email";
    public static final String COL_PASSWORD           = "password";
    public static final String COL_ROLE               = "role";
    public static final String COL_LINKED_STUDENT_ID  = "linked_student_id";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FIRST_NAME + " TEXT, " + COL_LAST_NAME + " TEXT, " +
                COL_CLASS_NAME + " TEXT, " + COL_SCHOOL_NAME + " TEXT, " +
                COL_AVATAR_URL + " TEXT, " + COL_STUDENT_NUM + " TEXT, " +
                COL_GEN_AVERAGE + " REAL, " +
                COL_TOTAL_ABS + " INTEGER DEFAULT 0, " +
                COL_TOTAL_LATE + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + TABLE_GRADES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_ID + " INTEGER, " + COL_SUBJECT + " TEXT, " +
                COL_VALUE + " REAL, " + COL_MAX_VALUE + " REAL, " +
                COL_COEFFICIENT + " REAL, " + COL_CLASS_AVG + " REAL, " +
                COL_DESCRIPTION + " TEXT, " + COL_DATE + " TEXT, " +
                COL_TRIMESTER + " TEXT, " + COL_TEACHER + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_SCHEDULE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_ID + " INTEGER, " + COL_SUBJECT + " TEXT, " +
                COL_TEACHER + " TEXT, " + COL_ROOM + " TEXT, " +
                COL_START_TIME + " TEXT, " + COL_END_TIME + " TEXT, " +
                COL_DAY_OF_WEEK + " TEXT, " + COL_DAY_INDEX + " INTEGER, " +
                COL_COLOR_RES + " INTEGER, " +
                COL_IS_CANCELLED + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + TABLE_ABSENCES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_ID + " INTEGER, " + COL_TYPE + " TEXT, " +
                COL_STATUS + " TEXT, " + COL_DATE + " TEXT, " +
                COL_START_TIME + " TEXT, " + COL_END_TIME + " TEXT, " +
                COL_SUBJECT + " TEXT, " + COL_REASON + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_MESSAGES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SENDER_ID + " INTEGER, " + COL_RECEIVER_ID + " INTEGER, " +
                COL_SENDER_NAME + " TEXT, " + COL_RECEIVER_NAME + " TEXT, " +
                COL_CONTENT + " TEXT, " + COL_TIMESTAMP + " TEXT, " +
                COL_IS_READ + " INTEGER DEFAULT 0, " +
                COL_IS_SENT + " INTEGER DEFAULT 0, " +
                COL_MSG_SUBJECT + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_DOCUMENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_ID + " INTEGER, " + COL_NAME + " TEXT, " +
                COL_TYPE + " TEXT, " + COL_FILE_PATH + " TEXT, " +
                COL_DATE + " TEXT, " + COL_FILE_SIZE + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_FIELDTRIPS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_ID + " INTEGER, " + COL_NAME + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " + COL_DATE + " TEXT, " +
                COL_LATITUDE + " REAL, " + COL_LONGITUDE + " REAL, " +
                COL_ADDRESS + " TEXT, " + COL_ORGANIZER + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_ROLE + " TEXT NOT NULL, " +
                COL_LINKED_STUDENT_ID + " INTEGER)");

        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSENCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDTRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void seedData(SQLiteDatabase db) {
        // ── Élèves ────────────────────────────────────────────────────────────
        insertStudentRaw(db, "Salma",  "Khaliqi",  "3ème A", "Lycée Averroès",         "", "20240001");
        insertStudentRaw(db, "Hind",   "Elkhalfi", "6ème B", "Collège Al Khawarizmi",  "", "20240002");

        // ── Notes Salma (id=1) ────────────────────────────────────────────────
        insertGradeRaw(db, 1, "Mathématiques", 16.5f, 20, 3, 13.2f, "Contrôle algèbre",      "2025-05-10", "T3", "Mme Benali");
        insertGradeRaw(db, 1, "Français",      14.0f, 20, 4, 11.5f, "Dissertation",           "2025-05-08", "T3", "M. Idrissi");
        insertGradeRaw(db, 1, "Histoire",      13.5f, 20, 2, 12.0f, "Contrôle indépendance",  "2025-05-06", "T3", "M. Chraibi");
        insertGradeRaw(db, 1, "Anglais",       18.0f, 20, 3, 14.5f, "Oral expression",        "2025-05-04", "T3", "Mme Tahiri");
        insertGradeRaw(db, 1, "Physique",      12.0f, 20, 3, 11.0f, "TP Optique",             "2025-04-28", "T3", "M. Bakkali");
        insertGradeRaw(db, 1, "SVT",           15.5f, 20, 2, 13.8f, "Génétique",              "2025-04-25", "T3", "Mme Zahraoui");

        // T1
        insertGradeRaw(db, 1, "Mathématiques", 14.0f, 20, 3, 12.0f, "Contrôle T1", "2025-01-10", "T1", "Mme Benali");
        insertGradeRaw(db, 1, "Français",      12.5f, 20, 4, 11.0f, "Rédaction T1","2025-01-15", "T1", "M. Idrissi");
        insertGradeRaw(db, 1, "Anglais",       16.0f, 20, 3, 13.0f, "Oral T1",     "2025-01-20", "T1", "Mme Tahiri");

        // T2
        insertGradeRaw(db, 1, "Mathématiques", 15.0f, 20, 3, 12.5f, "Contrôle T2", "2025-03-10", "T2", "Mme Benali");
        insertGradeRaw(db, 1, "Physique",      13.5f, 20, 3, 11.5f, "TP T2",       "2025-03-15", "T2", "M. Bakkali");
        insertGradeRaw(db, 1, "SVT",           14.5f, 20, 2, 13.0f, "Génétique T2","2025-03-20", "T2", "Mme Zahraoui");

        // ── Notes Hind (id=2) ─────────────────────────────────────────────────
        insertGradeRaw(db, 2, "Mathématiques", 11.0f, 20, 3, 12.0f, "Fractions", "2025-05-09", "T3", "M. Ouali");
        insertGradeRaw(db, 2, "Français",      13.5f, 20, 4, 11.0f, "Rédaction", "2025-05-07", "T3", "Mme Sabiri");



        // ── Emploi du temps Salma ─────────────────────────────────────────────
        insertScheduleRaw(db, 1, "Mathématiques",   "Mme Benali",   "Salle 201",    "08:00", "09:00", "Lundi",    0);
        insertScheduleRaw(db, 1, "Anglais",         "Mme Tahiri",   "Salle 105",    "09:00", "10:00", "Lundi",    0);
        insertScheduleRaw(db, 1, "Histoire",        "M. Chraibi",   "Salle 307",    "10:15", "11:15", "Lundi",    0);
        insertScheduleRaw(db, 1, "Physique",        "M. Bakkali",   "Labo 1",       "11:15", "12:15", "Lundi",    0);
        insertScheduleRaw(db, 1, "Français",        "M. Idrissi",   "Salle 202",    "13:30", "14:30", "Lundi",    0);
        insertScheduleRaw(db, 1, "SVT",             "Mme Zahraoui", "Labo 2",       "14:30", "15:30", "Lundi",    0);
        insertScheduleRaw(db, 1, "Mathématiques",   "Mme Benali",   "Salle 201",    "08:00", "09:00", "Mardi",    1);
        insertScheduleRaw(db, 1, "Français",        "M. Idrissi",   "Salle 202",    "09:00", "10:00", "Mardi",    1);
        insertScheduleRaw(db, 1, "EPS",             "M. Nassiri",   "Gymnase",      "10:15", "12:15", "Mardi",    1);
        insertScheduleRaw(db, 1, "Anglais",         "Mme Tahiri",   "Salle 105",    "13:30", "14:30", "Mercredi", 2);
        insertScheduleRaw(db, 1, "Arts Plastiques", "Mme Fatihi",   "Atelier",      "14:30", "15:30", "Mercredi", 2);
        insertScheduleRaw(db, 1, "Histoire",        "M. Chraibi",   "Salle 307",    "08:00", "09:00", "Jeudi",    3);
        insertScheduleRaw(db, 1, "Mathématiques",   "Mme Benali",   "Salle 201",    "09:00", "10:00", "Jeudi",    3);
        insertScheduleRaw(db, 1, "Physique",        "M. Bakkali",   "Labo 1",       "10:15", "12:15", "Jeudi",    3);
        insertScheduleRaw(db, 1, "Musique",         "M. Amine",     "Salle Musique","13:30", "14:30", "Vendredi", 4);
        insertScheduleRaw(db, 1, "Français",        "M. Idrissi",   "Salle 202",    "14:30", "15:30", "Vendredi", 4);

        // ── Absences ──────────────────────────────────────────────────────────
        insertAbsenceRaw(db, 1, "ABSENCE", "JUSTIFIED",   "2025-05-02", "08:00", "17:00", "Toute la journée", "Maladie");
        insertAbsenceRaw(db, 1, "LATE",    "UNJUSTIFIED", "2025-04-28", "08:00", "08:15", "Mathématiques",    "");
        insertAbsenceRaw(db, 1, "ABSENCE", "UNJUSTIFIED", "2025-04-15", "13:30", "15:30", "SVT",              "");

        // ── Messages ──────────────────────────────────────────────────────────
        insertMessageRaw(db, 2, 1, "Mme Benali",    "Parent Khaliqi",
                "Bonjour, Salma a réalisé d'excellents progrès ce trimestre. Félicitations !",
                "2025-05-12 14:30", 1, 0);
        insertMessageRaw(db, 1, 2, "Parent Khaliqi", "Mme Benali",
                "Merci pour votre retour. Salma travaille beaucoup.",
                "2025-05-12 15:45", 1, 1);
        insertMessageRaw(db, 3, 1, "M. Idrissi",    "Parent Khaliqi",
                "Concernant la dissertation du 8 mai, quelques points à améliorer.",
                "2025-05-09 10:00", 0, 0);

        // ── Sorties scolaires — Rabat ✅ ──────────────────────────────────────
        insertFieldTripRaw(db, 1,
                "Tour Hassan",
                "Sortie Histoire",
                "2025-06-03",
                34.0247, -6.8228,
                "Boulevard Mohammed V, Rabat");

        insertFieldTripRaw(db, 1,
                "Musée Mohammed VI",
                "Sortie Art",
                "2025-05-20",
                33.9916, -6.8498,
                "Avenue Moulay Hassan, Rabat");

        // ── Utilisateurs ──────────────────────────────────────────────────────
        insertUserRaw(db, "Parent Khaliqi",  "parent@school.com",      "Pass123", PrefsManager.ROLE_PARENT,  1);
        insertUserRaw(db, "Salma Khaliqi",   "salma@school.com",       "Pass123", PrefsManager.ROLE_STUDENT, 1);
        insertUserRaw(db, "Parent Elkhalfi", "hind.parent@school.com", "Pass123", PrefsManager.ROLE_PARENT,  2);
    }

    // ─── RAW INSERTS ──────────────────────────────────────────────────────────
    private void insertStudentRaw(SQLiteDatabase db, String fn, String ln, String cls,
                                  String school, String avatar, String num) {
        ContentValues cv = new ContentValues();
        cv.put(COL_FIRST_NAME, fn); cv.put(COL_LAST_NAME, ln);
        cv.put(COL_CLASS_NAME, cls); cv.put(COL_SCHOOL_NAME, school);
        cv.put(COL_AVATAR_URL, avatar); cv.put(COL_STUDENT_NUM, num);
        cv.put(COL_GEN_AVERAGE, 0f); cv.put(COL_TOTAL_ABS, 0); cv.put(COL_TOTAL_LATE, 0);
        db.insert(TABLE_STUDENTS, null, cv);
    }

    private void insertGradeRaw(SQLiteDatabase db, int sid, String subject, float val, float max,
                                float coef, float classAvg, String desc, String date, String tri, String teacher) {
        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_ID, sid); cv.put(COL_SUBJECT, subject);
        cv.put(COL_VALUE, val); cv.put(COL_MAX_VALUE, max);
        cv.put(COL_COEFFICIENT, coef); cv.put(COL_CLASS_AVG, classAvg);
        cv.put(COL_DESCRIPTION, desc); cv.put(COL_DATE, date);
        cv.put(COL_TRIMESTER, tri); cv.put(COL_TEACHER, teacher);
        db.insert(TABLE_GRADES, null, cv);
    }

    private void insertScheduleRaw(SQLiteDatabase db, int sid, String subject, String teacher,
                                   String room, String start, String end, String day, int dayIdx) {
        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_ID, sid); cv.put(COL_SUBJECT, subject);
        cv.put(COL_TEACHER, teacher); cv.put(COL_ROOM, room);
        cv.put(COL_START_TIME, start); cv.put(COL_END_TIME, end);
        cv.put(COL_DAY_OF_WEEK, day); cv.put(COL_DAY_INDEX, dayIdx);
        cv.put(COL_IS_CANCELLED, 0);
        db.insert(TABLE_SCHEDULE, null, cv);
    }

    private void insertAbsenceRaw(SQLiteDatabase db, int sid, String type, String status,
                                  String date, String start, String end, String subject, String reason) {
        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_ID, sid); cv.put(COL_TYPE, type);
        cv.put(COL_STATUS, status); cv.put(COL_DATE, date);
        cv.put(COL_START_TIME, start); cv.put(COL_END_TIME, end);
        cv.put(COL_SUBJECT, subject); cv.put(COL_REASON, reason);
        db.insert(TABLE_ABSENCES, null, cv);
    }

    private void insertMessageRaw(SQLiteDatabase db, int senderId, int receiverId,
                                  String senderName, String receiverName, String content,
                                  String timestamp, int isRead, int isSent) {
        ContentValues cv = new ContentValues();
        cv.put(COL_SENDER_ID, senderId); cv.put(COL_RECEIVER_ID, receiverId);
        cv.put(COL_SENDER_NAME, senderName); cv.put(COL_RECEIVER_NAME, receiverName);
        cv.put(COL_CONTENT, content); cv.put(COL_TIMESTAMP, timestamp);
        cv.put(COL_IS_READ, isRead); cv.put(COL_IS_SENT, isSent);
        db.insert(TABLE_MESSAGES, null, cv);
    }

    private void insertFieldTripRaw(SQLiteDatabase db, int sid, String name, String desc,
                                    String date, double lat, double lng, String address) {
        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_ID, sid); cv.put(COL_NAME, name);
        cv.put(COL_DESCRIPTION, desc); cv.put(COL_DATE, date);
        cv.put(COL_LATITUDE, lat); cv.put(COL_LONGITUDE, lng);
        cv.put(COL_ADDRESS, address);
        db.insert(TABLE_FIELDTRIPS, null, cv);
    }

    private void insertUserRaw(SQLiteDatabase db, String name, String email, String password,
                               String role, int linkedStudentId) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name); cv.put(COL_EMAIL, email);
        cv.put(COL_PASSWORD, password); cv.put(COL_ROLE, role);
        cv.put(COL_LINKED_STUDENT_ID, linkedStudentId);
        db.insert(TABLE_USERS, null, cv);
    }

    // ─── PUBLIC QUERIES ───────────────────────────────────────────────────────

    public User checkLogin(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, null,
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        User user = null;
        if (c.moveToFirst()) {
            user = new User();
            user.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            user.setName(c.getString(c.getColumnIndexOrThrow(COL_NAME)));
            user.setEmail(c.getString(c.getColumnIndexOrThrow(COL_EMAIL)));
            user.setRole(c.getString(c.getColumnIndexOrThrow(COL_ROLE)));
            user.setLinkedStudentId(c.getInt(c.getColumnIndexOrThrow(COL_LINKED_STUDENT_ID)));
        }
        c.close();
        return user;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_STUDENTS, null, null, null, null, null, COL_FIRST_NAME);
        while (c.moveToNext()) {
            Student s = new Student();
            s.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            s.setFirstName(c.getString(c.getColumnIndexOrThrow(COL_FIRST_NAME)));
            s.setLastName(c.getString(c.getColumnIndexOrThrow(COL_LAST_NAME)));
            s.setClassName(c.getString(c.getColumnIndexOrThrow(COL_CLASS_NAME)));
            s.setSchoolName(c.getString(c.getColumnIndexOrThrow(COL_SCHOOL_NAME)));
            s.setAvatarUrl(c.getString(c.getColumnIndexOrThrow(COL_AVATAR_URL)));
            s.setStudentNumber(c.getString(c.getColumnIndexOrThrow(COL_STUDENT_NUM)));
            s.setGeneralAverage(c.getFloat(c.getColumnIndexOrThrow(COL_GEN_AVERAGE)));
            s.setTotalAbsences(c.getInt(c.getColumnIndexOrThrow(COL_TOTAL_ABS)));
            s.setTotalLate(c.getInt(c.getColumnIndexOrThrow(COL_TOTAL_LATE)));
            list.add(s);
        }
        c.close();
        return list;
    }

    public Student getStudent(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_STUDENTS, null, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        Student s = null;
        if (c.moveToFirst()) {
            s = new Student();
            s.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            s.setFirstName(c.getString(c.getColumnIndexOrThrow(COL_FIRST_NAME)));
            s.setLastName(c.getString(c.getColumnIndexOrThrow(COL_LAST_NAME)));
            s.setClassName(c.getString(c.getColumnIndexOrThrow(COL_CLASS_NAME)));
            s.setSchoolName(c.getString(c.getColumnIndexOrThrow(COL_SCHOOL_NAME)));
            s.setAvatarUrl(c.getString(c.getColumnIndexOrThrow(COL_AVATAR_URL)));
            s.setStudentNumber(c.getString(c.getColumnIndexOrThrow(COL_STUDENT_NUM)));
            s.setGeneralAverage(c.getFloat(c.getColumnIndexOrThrow(COL_GEN_AVERAGE)));
            s.setTotalAbsences(c.getInt(c.getColumnIndexOrThrow(COL_TOTAL_ABS)));
            s.setTotalLate(c.getInt(c.getColumnIndexOrThrow(COL_TOTAL_LATE)));
        }
        c.close();
        return s;
    }

    public List<Grade> getGradesForStudent(int studentId) {
        List<Grade> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_GRADES, null, COL_STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)}, null, null, COL_DATE + " DESC");
        while (c.moveToNext()) {
            Grade g = new Grade();
            g.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            g.setStudentId(c.getInt(c.getColumnIndexOrThrow(COL_STUDENT_ID)));
            g.setSubject(c.getString(c.getColumnIndexOrThrow(COL_SUBJECT)));
            g.setValue(c.getFloat(c.getColumnIndexOrThrow(COL_VALUE)));
            g.setMaxValue(c.getFloat(c.getColumnIndexOrThrow(COL_MAX_VALUE)));
            g.setCoefficient(c.getFloat(c.getColumnIndexOrThrow(COL_COEFFICIENT)));
            g.setClassAverage(c.getFloat(c.getColumnIndexOrThrow(COL_CLASS_AVG)));
            g.setDescription(c.getString(c.getColumnIndexOrThrow(COL_DESCRIPTION)));
            g.setDate(c.getString(c.getColumnIndexOrThrow(COL_DATE)));
            g.setTrimester(c.getString(c.getColumnIndexOrThrow(COL_TRIMESTER)));
            g.setTeacher(c.getString(c.getColumnIndexOrThrow(COL_TEACHER)));
            list.add(g);
        }
        c.close();
        return list;
    }

    public List<ScheduleSlot> getScheduleForStudentAndDay(int studentId, int dayIndex) {
        List<ScheduleSlot> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_SCHEDULE, null,
                COL_STUDENT_ID + "=? AND " + COL_DAY_INDEX + "=?",
                new String[]{String.valueOf(studentId), String.valueOf(dayIndex)},
                null, null, COL_START_TIME + " ASC");
        while (c.moveToNext()) {
            ScheduleSlot s = new ScheduleSlot();
            s.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            s.setStudentId(c.getInt(c.getColumnIndexOrThrow(COL_STUDENT_ID)));
            s.setSubject(c.getString(c.getColumnIndexOrThrow(COL_SUBJECT)));
            s.setTeacher(c.getString(c.getColumnIndexOrThrow(COL_TEACHER)));
            s.setRoom(c.getString(c.getColumnIndexOrThrow(COL_ROOM)));
            s.setStartTime(c.getString(c.getColumnIndexOrThrow(COL_START_TIME)));
            s.setEndTime(c.getString(c.getColumnIndexOrThrow(COL_END_TIME)));
            s.setDayOfWeek(c.getString(c.getColumnIndexOrThrow(COL_DAY_OF_WEEK))); // ✅ BUG CORRIGÉ
            s.setDayIndex(c.getInt(c.getColumnIndexOrThrow(COL_DAY_INDEX)));
            s.setCancelled(c.getInt(c.getColumnIndexOrThrow(COL_IS_CANCELLED)) == 1);
            list.add(s);
        }
        c.close();
        return list;
    }

    public List<Absence> getAbsencesForStudent(int studentId) {
        List<Absence> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_ABSENCES, null, COL_STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)}, null, null, COL_DATE + " DESC");
        while (c.moveToNext()) {
            Absence a = new Absence();
            a.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            a.setStudentId(c.getInt(c.getColumnIndexOrThrow(COL_STUDENT_ID)));
            a.setType(c.getString(c.getColumnIndexOrThrow(COL_TYPE)));
            a.setStatus(c.getString(c.getColumnIndexOrThrow(COL_STATUS)));
            a.setDate(c.getString(c.getColumnIndexOrThrow(COL_DATE)));
            a.setStartTime(c.getString(c.getColumnIndexOrThrow(COL_START_TIME)));
            a.setEndTime(c.getString(c.getColumnIndexOrThrow(COL_END_TIME)));
            a.setSubject(c.getString(c.getColumnIndexOrThrow(COL_SUBJECT)));
            a.setReason(c.getString(c.getColumnIndexOrThrow(COL_REASON)));
            list.add(a);
        }
        c.close();
        return list;
    }

    public List<Message> getMessagesForUser(int userId) {
        List<Message> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_MESSAGES, null,
                COL_SENDER_ID + "=? OR " + COL_RECEIVER_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(userId)},
                null, null, COL_TIMESTAMP + " ASC"); // ✅ ASC = plus ancien en premier
        while (c.moveToNext()) {
            Message m = new Message();
            m.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            m.setSenderId(c.getInt(c.getColumnIndexOrThrow(COL_SENDER_ID)));
            m.setReceiverId(c.getInt(c.getColumnIndexOrThrow(COL_RECEIVER_ID)));
            m.setSenderName(c.getString(c.getColumnIndexOrThrow(COL_SENDER_NAME)));
            m.setReceiverName(c.getString(c.getColumnIndexOrThrow(COL_RECEIVER_NAME)));
            m.setContent(c.getString(c.getColumnIndexOrThrow(COL_CONTENT)));
            m.setTimestamp(c.getString(c.getColumnIndexOrThrow(COL_TIMESTAMP)));
            m.setRead(c.getInt(c.getColumnIndexOrThrow(COL_IS_READ)) == 1);
            m.setSentByMe(c.getInt(c.getColumnIndexOrThrow(COL_IS_SENT)) == 1);
            list.add(m);
        }
        c.close();
        return list;
    }

    public List<Document> getDocumentsForStudent(int studentId) {
        List<Document> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_DOCUMENTS, null, COL_STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)}, null, null, COL_DATE + " DESC");
        while (c.moveToNext()) {
            Document d = new Document();
            d.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            d.setStudentId(c.getInt(c.getColumnIndexOrThrow(COL_STUDENT_ID)));
            d.setName(c.getString(c.getColumnIndexOrThrow(COL_NAME)));
            d.setType(c.getString(c.getColumnIndexOrThrow(COL_TYPE)));
            d.setFilePath(c.getString(c.getColumnIndexOrThrow(COL_FILE_PATH)));
            d.setDate(c.getString(c.getColumnIndexOrThrow(COL_DATE)));
            d.setFileSize(c.getLong(c.getColumnIndexOrThrow(COL_FILE_SIZE)));
            list.add(d);
        }
        c.close();
        return list;
    }

    public List<FieldTrip> getFieldTripsForStudent(int studentId) {
        List<FieldTrip> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_FIELDTRIPS, null, COL_STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)}, null, null, COL_DATE + " ASC");
        while (c.moveToNext()) {
            FieldTrip f = new FieldTrip();
            f.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            f.setStudentId(c.getInt(c.getColumnIndexOrThrow(COL_STUDENT_ID)));
            f.setName(c.getString(c.getColumnIndexOrThrow(COL_NAME)));
            f.setDescription(c.getString(c.getColumnIndexOrThrow(COL_DESCRIPTION)));
            f.setDate(c.getString(c.getColumnIndexOrThrow(COL_DATE)));
            f.setLatitude(c.getDouble(c.getColumnIndexOrThrow(COL_LATITUDE)));
            f.setLongitude(c.getDouble(c.getColumnIndexOrThrow(COL_LONGITUDE)));
            f.setAddress(c.getString(c.getColumnIndexOrThrow(COL_ADDRESS)));
            f.setOrganizer(c.getString(c.getColumnIndexOrThrow(COL_ORGANIZER)));
            list.add(f);
        }
        c.close();
        return list;
    }

    public long insertDocument(Document doc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_ID, doc.getStudentId());
        cv.put(COL_NAME, doc.getName());
        cv.put(COL_TYPE, doc.getType());
        cv.put(COL_FILE_PATH, doc.getFilePath());
        cv.put(COL_DATE, doc.getDate());
        cv.put(COL_FILE_SIZE, doc.getFileSize());
        return db.insert(TABLE_DOCUMENTS, null, cv);
    }

    public long insertMessage(Message msg) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SENDER_ID, msg.getSenderId());
        cv.put(COL_RECEIVER_ID, msg.getReceiverId());
        cv.put(COL_SENDER_NAME, msg.getSenderName());
        cv.put(COL_RECEIVER_NAME, msg.getReceiverName());
        cv.put(COL_CONTENT, msg.getContent());
        cv.put(COL_TIMESTAMP, msg.getTimestamp());
        cv.put(COL_IS_READ, 0);
        cv.put(COL_IS_SENT, msg.isSentByMe() ? 1 : 0);
        return db.insert(TABLE_MESSAGES, null, cv);
    }

    public void markMessageRead(int messageId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_IS_READ, 1);
        db.update(TABLE_MESSAGES, cv, COL_ID + "=?", new String[]{String.valueOf(messageId)});
    }

    public int getUnreadMessageCount(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_MESSAGES +
                        " WHERE " + COL_RECEIVER_ID + "=? AND " + COL_IS_READ + "=0",
                new String[]{String.valueOf(userId)});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }
}