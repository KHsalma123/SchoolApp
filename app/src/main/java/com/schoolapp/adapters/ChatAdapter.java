package com.schoolapp.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.models.Message;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {

    private static final int VIEW_SENT     = 0;
    private static final int VIEW_RECEIVED = 1;

    private final List<Message> messages;

    public ChatAdapter(List<Message> messages) { this.messages = messages; }

    @Override
    public int getItemViewType(int pos) {
        return messages.get(pos).isSentByMe() ? VIEW_SENT : VIEW_RECEIVED;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (viewType == VIEW_SENT)
                ? R.layout.item_chat_sent
                : R.layout.item_chat_received;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Message m = messages.get(pos);
        h.tvContent.setText(m.getContent());
        h.tvTime.setText(m.getTimestamp());
    }

    @Override
    public int getItemCount() { return messages.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvContent, tvTime;
        VH(View v) {
            super(v);
            tvContent = v.findViewById(R.id.tv_content);
            tvTime    = v.findViewById(R.id.tv_time);
        }
    }
}
