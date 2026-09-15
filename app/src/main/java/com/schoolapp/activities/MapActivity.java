package com.schoolapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.schoolapp.R;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.FieldTrip;
import com.schoolapp.utils.PrefsManager;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private TextView tvLocationName, tvLocationAddress, tvLocationDate;
    private MaterialButton btnDirections;
    private ImageButton btnBack;
    private View bottomSheet;
    private BottomSheetBehavior<View> bsBehavior;
    private Chip chipSchool, chipTrips;

    private LatLng schoolLatLng;
    private String schoolName, schoolAddress;
    private List<FieldTrip> fieldTrips;
    private boolean showSchool = true, showTrips = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        tvLocationName    = findViewById(R.id.tv_location_name);
        tvLocationAddress = findViewById(R.id.tv_location_address);
        tvLocationDate    = findViewById(R.id.tv_location_date);
        btnDirections     = findViewById(R.id.btn_directions);
        btnBack           = findViewById(R.id.btn_back);
        bottomSheet       = findViewById(R.id.bottom_sheet);
        chipSchool        = findViewById(R.id.chip_school);
        chipTrips         = findViewById(R.id.chip_trips);

        bsBehavior = BottomSheetBehavior.from(bottomSheet);
        bsBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        PrefsManager prefs = PrefsManager.getInstance(this);
        schoolLatLng  = new LatLng(prefs.getSchoolLat(), prefs.getSchoolLng());
        schoolName    = prefs.getSchoolName();
        schoolAddress = prefs.getSchoolAddress();

        btnBack.setOnClickListener(v -> finish());

        chipSchool.setOnCheckedChangeListener((btn, checked) -> {
            showSchool = checked;
            refreshMarkers();
        });

        chipTrips.setOnCheckedChangeListener((btn, checked) -> {
            showTrips = checked;
            refreshMarkers();
        });

        // Load field trips
        new Thread(() -> {
            int studentId = PrefsManager.getInstance(this).getSelectedChildId();
            fieldTrips = DatabaseHelper.getInstance(this).getFieldTripsForStudent(studentId);
            runOnUiThread(this::refreshMarkers);
        }).start();

        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFrag != null) mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLatLng, 13));
        refreshMarkers();
    }

    private void refreshMarkers() {
        if (googleMap == null) return;
        googleMap.clear();

        if (showSchool) {
            Marker schoolMarker = googleMap.addMarker(new MarkerOptions()
                    .position(schoolLatLng)
                    .title(schoolName)
                    .snippet(schoolAddress)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            if (schoolMarker != null) schoolMarker.setTag("school");
        }

        if (showTrips && fieldTrips != null) {
            for (FieldTrip trip : fieldTrips) {
                LatLng pos = new LatLng(trip.getLatitude(), trip.getLongitude());
                Marker m = googleMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(trip.getName())
                        .snippet(trip.getDate())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                if (m != null) m.setTag(trip);
            }
        }

        googleMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if ("school".equals(tag)) {
                showInfoSheet(schoolName, schoolAddress, "", schoolLatLng);
            } else if (tag instanceof FieldTrip) {
                FieldTrip ft = (FieldTrip) tag;
                showInfoSheet(ft.getName(), ft.getAddress(), ft.getDate(),
                        new LatLng(ft.getLatitude(), ft.getLongitude()));
            }
            return true;
        });
    }

    private void showInfoSheet(String name, String address, String date, LatLng dest) {
        tvLocationName.setText(name);
        tvLocationAddress.setText(address);
        tvLocationDate.setText(date.isEmpty() ? "Établissement" : "Sortie le " + date);
        bsBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        btnDirections.setOnClickListener(v -> {
            // Implicit Intent → Google Maps navigation
            Uri geoUri = Uri.parse("google.navigation:q=" + dest.latitude + "," + dest.longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Fallback: browser maps
                Uri webUri = Uri.parse("https://maps.google.com/maps?daddr="
                        + dest.latitude + "," + dest.longitude);
                startActivity(new Intent(Intent.ACTION_VIEW, webUri));
            }
        });
    }
}
