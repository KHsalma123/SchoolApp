package com.schoolapp.adapters;

import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.models.Message;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.VH> {

    public interface OnMessageClick { void onClick(Message message); }

    private final List<Message> messages;
    private final OnMessageClick listener;

    public MessagesAdapter(List<Message> messages, OnMessageClick listener) {
        this.messages = messages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Message m = messages.get(pos);
        h.tvName.setText(m.isSentByMe() ? "Vous → " + m.getReceiverName() : m.getSenderName());
        h.tvPreview.setText(m.getContent());
        h.tvTime.setText(m.getTimestamp());
        h.tvInitials.setText(m.getInitials());

        // Unread indicator
        h.tvUnread.setVisibility((!m.isRead() && !m.isSentByMe()) ? View.VISIBLE : View.GONE);

        // Avatar color
        int[] colors = {0xFF6C63FF, 0xFFFF6B6B, 0xFF4ECDC4, 0xFFF7B731, 0xFF26de81};
        int c = colors[pos % colors.length];
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(c);
        h.tvInitials.setBackground(bg);

        h.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(m); });
    }

    @Override
    public int getItemCount() { return messages.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPreview, tvTime, tvInitials, tvUnread;
        VH(View v) {
            super(v);
            tvName     = v.findViewById(R.id.tv_name);
            tvPreview  = v.findViewById(R.id.tv_preview);
            tvTime     = v.findViewById(R.id.tv_time);
            tvInitials = v.findViewById(R.id.tv_initials);
            tvUnread   = v.findViewById(R.id.tv_unread);
        }
    }
}
