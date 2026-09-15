package com.schoolapp.adapters;

import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.schoolapp.R;
import com.schoolapp.models.Document;
import java.util.List;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.VH> {

    private final List<Document> documents;

    public DocumentsAdapter(List<Document> documents) { this.documents = documents; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Document d = documents.get(pos);
        h.tvName.setText(d.getName());
        h.tvDate.setText(d.getDate());
        h.tvSize.setText(d.getFormattedSize());
        h.tvType.setText(d.getType());

        if (d.getFilePath() != null && !d.getFilePath().isEmpty()) {
            Glide.with(h.itemView.getContext())
                    .load(d.getFilePath())
                    .centerCrop()
                    .placeholder(R.drawable.ic_document)
                    .into(h.ivThumb);
        } else {
            h.ivThumb.setImageResource(R.drawable.ic_document);
        }
    }

    @Override
    public int getItemCount() { return documents.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvName, tvDate, tvSize, tvType;
        VH(View v) {
            super(v);
            ivThumb = v.findViewById(R.id.iv_thumb);
            tvName  = v.findViewById(R.id.tv_name);
            tvDate  = v.findViewById(R.id.tv_date);
            tvSize  = v.findViewById(R.id.tv_size);
            tvType  = v.findViewById(R.id.tv_type);
        }
    }
}
