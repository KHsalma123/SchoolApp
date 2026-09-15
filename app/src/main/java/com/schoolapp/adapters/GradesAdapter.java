package com.schoolapp.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.models.Grade;
import com.schoolapp.utils.SubjectColorUtils;
import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.VH> {

    private final List<Grade> grades;

    public GradesAdapter(List<Grade> grades) { this.grades = grades; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Grade g = grades.get(pos);
        h.tvSubject.setText(g.getSubject());
        h.tvDescription.setText(g.getDescription());
        h.tvDate.setText(g.getDate());
        h.tvTeacher.setText(g.getTeacher());
        h.tvGrade.setText(g.getFormattedGrade());
        h.tvCoef.setText("Coef. " + (int)g.getCoefficient());
        h.tvClassAvg.setText("Moy. classe : " + g.getClassAverage() + "/20");

        // Grade color
        int gradeColor = SubjectColorUtils.getGradeColor(h.itemView.getContext(),
                g.getValue(), g.getMaxValue());
        h.tvGrade.setTextColor(gradeColor);

        // Subject colored bar
        int subjectColor = SubjectColorUtils.getColor(h.itemView.getContext(), g.getSubject());
        GradientDrawable bar = new GradientDrawable();
        bar.setShape(GradientDrawable.RECTANGLE);
        bar.setCornerRadius(8f);
        bar.setColor(subjectColor);
        h.viewColorBar.setBackground(bar);

        // Initial
        h.tvInitial.setText(g.getSubject().substring(0, 1));
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        int alpha = Color.argb(30,
                Color.red(subjectColor), Color.green(subjectColor), Color.blue(subjectColor));
        circle.setColor(alpha);
        h.tvInitial.setBackground(circle);
        h.tvInitial.setTextColor(subjectColor);

        // Category chip
        String cat = g.getGradeCategory();
        h.tvCategory.setText(cat);
        GradientDrawable chip = new GradientDrawable();
        chip.setCornerRadius(100f);
        chip.setColor(gradeColor + 0x30000000);
        h.tvCategory.setTextColor(gradeColor);
    }

    @Override
    public int getItemCount() { return grades.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSubject, tvDescription, tvDate, tvTeacher,
                 tvGrade, tvCoef, tvClassAvg, tvInitial, tvCategory;
        View viewColorBar;
        VH(View v) {
            super(v);
            tvSubject     = v.findViewById(R.id.tv_subject);
            tvDescription = v.findViewById(R.id.tv_description);
            tvDate        = v.findViewById(R.id.tv_date);
            tvTeacher     = v.findViewById(R.id.tv_teacher);
            tvGrade       = v.findViewById(R.id.tv_grade);
            tvCoef        = v.findViewById(R.id.tv_coef);
            tvClassAvg    = v.findViewById(R.id.tv_class_avg);
            tvInitial     = v.findViewById(R.id.tv_initial);
            tvCategory    = v.findViewById(R.id.tv_category);
            viewColorBar  = v.findViewById(R.id.view_color_bar);
        }
    }
}
