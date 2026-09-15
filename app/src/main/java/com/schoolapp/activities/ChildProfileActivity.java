package com.schoolapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.schoolapp.R;
import com.schoolapp.adapters.GradesAdapter;
import com.schoolapp.adapters.AbsencesAdapter;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.*;
import com.schoolapp.utils.PrefsManager;
import com.schoolapp.utils.SubjectColorUtils;
import java.util.*;

public class ChildProfileActivity extends AppCompatActivity {

    private TextView tvName, tvClass, tvSchool, tvAverage, tvAbsences, tvLate, tvStudentNum;
    private TextView tvInitial;
    private RecyclerView rvGrades;
    private MaterialButton btnMap, btnDocuments, btnMessages;
    private ImageButton btnBack;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);

        tvName       = findViewById(R.id.tv_name);
        tvClass      = findViewById(R.id.tv_class);
        tvSchool     = findViewById(R.id.tv_school);
        tvAverage    = findViewById(R.id.tv_average);
        tvAbsences   = findViewById(R.id.tv_absences);
        tvLate       = findViewById(R.id.tv_late);
        tvStudentNum = findViewById(R.id.tv_student_num);
        tvInitial    = findViewById(R.id.tv_initial);
        rvGrades     = findViewById(R.id.rv_grades);
        btnMap       = findViewById(R.id.btn_map);
        btnDocuments = findViewById(R.id.btn_documents);
        btnMessages  = findViewById(R.id.btn_messages);
        btnBack      = findViewById(R.id.btn_back);
        pbLoading    = findViewById(R.id.pb_loading);

        rvGrades.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        btnMap.setOnClickListener(v ->
                startActivity(new Intent(this, MapActivity.class)));

        btnDocuments.setOnClickListener(v ->
                startActivity(new Intent(this, DocumentActivity.class)));

        btnMessages.setOnClickListener(v ->
                startActivity(new Intent(this, MessageActivity.class)));

        loadProfile();
    }

    private void loadProfile() {
        pbLoading.setVisibility(android.view.View.VISIBLE);

        new Thread(() -> {
            int studentId = PrefsManager.getInstance(this).getSelectedChildId();
            DatabaseHelper db = DatabaseHelper.getInstance(this);

            Student student = db.getStudent(studentId);
            List<Grade> grades = db.getGradesForStudent(studentId);
            List<Absence> absences = db.getAbsencesForStudent(studentId);

            // Compute average
            float avg = 0f;
            if (!grades.isEmpty()) {
                float total = 0f, coef = 0f;
                for (Grade g : grades) {
                    total += (g.getValue() / g.getMaxValue()) * 20f * g.getCoefficient();
                    coef  += g.getCoefficient();
                }
                avg = coef > 0 ? total / coef : 0f;
            }

            int countAbs = 0, countLate = 0;
            for (Absence a : absences) {
                if (a.isLate()) countLate++; else countAbs++;
            }

            final float finalAvg = avg;
            final int fAbs = countAbs, fLate = countLate;
            final List<Grade> top = grades.size() > 6 ? grades.subList(0, 6) : grades;

            new Handler(Looper.getMainLooper()).post(() -> {
                pbLoading.setVisibility(android.view.View.GONE);
                if (student == null) return;

                tvName.setText(student.getFullName());
                tvClass.setText(student.getClassName());
                tvSchool.setText(student.getSchoolName());
                tvAverage.setText(String.format(Locale.FRENCH, "%.2f", finalAvg));
                tvAbsences.setText(String.valueOf(fAbs));
                tvLate.setText(String.valueOf(fLate));
                tvStudentNum.setText("N° " + student.getStudentNumber());
                tvInitial.setText(student.getInitials());

                // Avatar color from first letter
                int color = SubjectColorUtils.getColor(this, student.getFirstName());
                android.graphics.drawable.GradientDrawable circle =
                        new android.graphics.drawable.GradientDrawable();
                circle.setShape(android.graphics.drawable.GradientDrawable.OVAL);
                circle.setColor(color);
                tvInitial.setBackground(circle);

                rvGrades.setAdapter(new GradesAdapter(top));
            });
        }).start();
    }
}
