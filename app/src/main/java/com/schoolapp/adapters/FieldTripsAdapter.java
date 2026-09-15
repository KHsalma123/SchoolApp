package com.schoolapp.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.models.FieldTrip;
import java.util.List;

public class FieldTripsAdapter extends RecyclerView.Adapter<FieldTripsAdapter.VH> {

    private final List<FieldTrip> trips;

    public FieldTripsAdapter(List<FieldTrip> trips) { this.trips = trips; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_field_trip, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        FieldTrip t = trips.get(pos);
        h.tvName.setText(t.getName());
        h.tvDate.setText("📅 " + t.getDate());
        h.tvAddress.setText(t.getAddress());
        h.tvDescription.setText(t.getDescription());

        // Open Google Maps on click
        h.card.setOnClickListener(v -> {
            Uri geoUri = Uri.parse(
                    "geo:" + t.getLatitude() + "," + t.getLongitude()
                            + "?q=" + Uri.encode(t.getName()));
            Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                v.getContext().startActivity(intent);
            } else {
                Uri webUri = Uri.parse("https://maps.google.com/maps?q="
                        + t.getLatitude() + "," + t.getLongitude());
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, webUri));
            }
        });
    }

    @Override
    public int getItemCount() { return trips.size(); }

    static class VH extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvName, tvDate, tvAddress, tvDescription;

        VH(View v) {
            super(v);
            card          = (CardView) v;
            tvName        = v.findViewById(R.id.tv_name);
            tvDate        = v.findViewById(R.id.tv_date);
            tvAddress     = v.findViewById(R.id.tv_address);
            tvDescription = v.findViewById(R.id.tv_description);
        }
    }
}
