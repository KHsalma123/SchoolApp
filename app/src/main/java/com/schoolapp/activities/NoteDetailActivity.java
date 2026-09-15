package com.schoolapp.activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.schoolapp.R;
import com.schoolapp.utils.SubjectColorUtils;

public class NoteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        String subject     = getIntent().getStringExtra("subject");
        String description = getIntent().getStringExtra("description");
        String date        = getIntent().getStringExtra("date");
        String teacher     = getIntent().getStringExtra("teacher");
        float  value       = getIntent().getFloatExtra("value", 0f);
        float  maxValue    = getIntent().getFloatExtra("max_value", 20f);
        float  classAvg    = getIntent().getFloatExtra("class_average", 0f);
        float  coef        = getIntent().getFloatExtra("coefficient", 1f);

        if (subject == null) subject = "Note";

        TextView tvSubject    = findViewById(R.id.tv_subject);
        TextView tvGrade      = findViewById(R.id.tv_grade);
        TextView tvDescription= findViewById(R.id.tv_description);
        TextView tvDate       = findViewById(R.id.tv_date);
        TextView tvTeacher    = findViewById(R.id.tv_teacher);
        TextView tvClassAvg   = findViewById(R.id.tv_class_avg);
        TextView tvCoef       = findViewById(R.id.tv_coef);
        TextView tvInitial    = findViewById(R.id.tv_initial);
        ImageButton btnBack   = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        tvSubject.setText(subject);
        tvGrade.setText(String.format("%.1f / %.0f", value, maxValue));
        tvDescription.setText(description);
        tvDate.setText(date);
        tvTeacher.setText(teacher);
        tvClassAvg.setText(String.format("Moyenne de classe : %.1f / 20", classAvg));
        tvCoef.setText("Coefficient : " + (int) coef);
        tvInitial.setText(subject.substring(0, 1));

        int color = SubjectColorUtils.getColor(this, subject);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(color);
        tvInitial.setBackground(circle);

        int gradeColor = SubjectColorUtils.getGradeColor(this, value, maxValue);
        tvGrade.setTextColor(gradeColor);
    }
}
