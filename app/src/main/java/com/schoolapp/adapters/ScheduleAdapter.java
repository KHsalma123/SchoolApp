package com.schoolapp.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.models.ScheduleSlot;
import com.schoolapp.utils.SubjectColorUtils;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.VH> {

    private final List<ScheduleSlot> slots;

    public ScheduleAdapter(List<ScheduleSlot> slots) { this.slots = slots; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        ScheduleSlot s = slots.get(pos);
        h.tvSubject.setText(s.getSubject());
        h.tvTeacher.setText(s.getTeacher());
        h.tvRoom.setText(s.getRoom());
        h.tvTime.setText(s.getTimeRange());
        h.tvInitial.setText(s.getInitial());

        int color = SubjectColorUtils.getColor(h.itemView.getContext(), s.getSubject());

        // Colored left border
        GradientDrawable bar = new GradientDrawable();
        bar.setCornerRadius(12f);
        bar.setColor(color);
        h.viewBar.setBackground(bar);

        // Initial circle
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        int alphaColor = Color.argb(25,
                Color.red(color), Color.green(color), Color.blue(color));
        circle.setColor(alphaColor);
        h.tvInitial.setBackground(circle);
        h.tvInitial.setTextColor(color);

        // Strikethrough if cancelled
        if (s.isCancelled()) {
            h.tvSubject.setPaintFlags(
                    h.tvSubject.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            h.itemView.setAlpha(0.5f);
        } else {
            h.tvSubject.setPaintFlags(
                    h.tvSubject.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
            h.itemView.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() { return slots.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSubject, tvTeacher, tvRoom, tvTime, tvInitial;
        View viewBar;
        VH(View v) {
            super(v);
            tvSubject = v.findViewById(R.id.tv_subject);
            tvTeacher = v.findViewById(R.id.tv_teacher);
            tvRoom    = v.findViewById(R.id.tv_room);
            tvTime    = v.findViewById(R.id.tv_time);
            tvInitial = v.findViewById(R.id.tv_initial);
            viewBar   = v.findViewById(R.id.view_color_bar);
        }
    }
}
