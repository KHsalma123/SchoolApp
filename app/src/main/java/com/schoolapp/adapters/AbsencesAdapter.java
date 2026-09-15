package com.schoolapp.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.models.Absence;
import java.util.List;

public class AbsencesAdapter extends RecyclerView.Adapter<AbsencesAdapter.VH> {

    private final List<Absence> absences;
    private final Context context;

    public AbsencesAdapter(List<Absence> absences, Context context) {
        this.absences = absences;
        this.context  = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_absence, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Absence a = absences.get(pos);

        h.tvDate.setText(a.getDate());
        h.tvSubject.setText(a.getSubject());
        h.tvDuration.setText(a.getDuration());

        // Type badge
        if (a.isLate()) {
            h.tvType.setText("RETARD");
            h.tvType.setTextColor(context.getColor(R.color.warning));
            setBadgeColor(h.tvType, R.color.warning_light);
        } else {
            h.tvType.setText("ABSENCE");
            h.tvType.setTextColor(context.getColor(R.color.danger));
            setBadgeColor(h.tvType, R.color.danger_light);
        }

        // Status badge
        if (a.isJustified()) {
            h.tvStatus.setText("Justifiée");
            h.tvStatus.setTextColor(context.getColor(R.color.success));
            setBadgeColor(h.tvStatus, R.color.success_light);
        } else {
            h.tvStatus.setText("Non justifiée");
            h.tvStatus.setTextColor(context.getColor(R.color.danger));
            setBadgeColor(h.tvStatus, R.color.danger_light);
        }
    }

    private void setBadgeColor(TextView tv, int colorRes) {
        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(100f);
        bg.setColor(context.getColor(colorRes));
        tv.setBackground(bg);
    }

    @Override
    public int getItemCount() { return absences.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvDate, tvSubject, tvDuration, tvType, tvStatus;
        VH(View v) {
            super(v);
            tvDate     = v.findViewById(R.id.tv_date);
            tvSubject  = v.findViewById(R.id.tv_subject);
            tvDuration = v.findViewById(R.id.tv_duration);
            tvType     = v.findViewById(R.id.tv_type);
            tvStatus   = v.findViewById(R.id.tv_status);
        }
    }
}
