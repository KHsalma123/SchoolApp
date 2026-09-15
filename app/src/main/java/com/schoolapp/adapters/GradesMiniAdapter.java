package com.schoolapp.adapters;

import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.models.Grade;
import com.schoolapp.utils.SubjectColorUtils;
import java.util.List;

public class GradesMiniAdapter extends RecyclerView.Adapter<GradesMiniAdapter.VH> {

    private final List<Grade> grades;

    public GradesMiniAdapter(List<Grade> grades) { this.grades = grades; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade_mini, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Grade g = grades.get(pos);
        h.tvSubject.setText(g.getSubject());
        h.tvGrade.setText(String.format("%.1f", g.getValue()));
        h.tvMax.setText("/" + (int)g.getMaxValue());

        int color = SubjectColorUtils.getColor(h.itemView.getContext(), g.getSubject());
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(56f);
        bg.setColor(color);
        h.tvInitial.setBackground(bg);
        h.tvInitial.setText(g.getSubject().substring(0, 1));

        int gradeColor = SubjectColorUtils.getGradeColor(
                h.itemView.getContext(), g.getValue(), g.getMaxValue());
        h.tvGrade.setTextColor(gradeColor);
    }

    @Override
    public int getItemCount() { return grades.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSubject, tvGrade, tvMax, tvInitial;
        VH(View v) {
            super(v);
            tvSubject = v.findViewById(R.id.tv_subject);
            tvGrade   = v.findViewById(R.id.tv_grade);
            tvMax     = v.findViewById(R.id.tv_max);
            tvInitial = v.findViewById(R.id.tv_initial);
        }
    }
}
