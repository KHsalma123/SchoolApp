package com.schoolapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.schoolapp.R;
import com.schoolapp.activities.DocumentActivity;
import com.schoolapp.adapters.GradesAdapter;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.Grade;
import com.schoolapp.utils.PrefsManager;
import java.util.*;

public class GradesFragment extends Fragment {

    private RecyclerView rvGrades;
    private TextView tvAverage, tvAverageLabel, tvEmpty;
    private ChipGroup chipGroupTrimester;
    private FloatingActionButton fabScan;
    private ProgressBar pbLoading;

    private List<Grade> allGrades = new ArrayList<>();
    private String selectedTrimester = "all";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGrades          = view.findViewById(R.id.rv_grades);
        tvAverage         = view.findViewById(R.id.tv_average_value);
        tvAverageLabel    = view.findViewById(R.id.tv_average_label);
        chipGroupTrimester= view.findViewById(R.id.chip_group_trimester);
        fabScan           = view.findViewById(R.id.fab_scan);
        tvEmpty           = view.findViewById(R.id.tv_empty);
        pbLoading         = view.findViewById(R.id.pb_loading);

        rvGrades.setLayoutManager(new LinearLayoutManager(getContext()));

        // Chip filter
        chipGroupTrimester.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            Chip chip = group.findViewById(checkedIds.get(0));
            if (chip != null) {
                String tag = (String) chip.getTag();
                selectedTrimester = tag != null ? tag : "all";
                filterGrades();
            }
        });

        // FAB → Camera to scan bulletin
        fabScan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DocumentActivity.class);
            startActivity(intent);
        });

        loadGrades();
    }

    private void loadGrades() {
        pbLoading.setVisibility(View.VISIBLE);

        new Thread(() -> {
            int studentId = PrefsManager.getInstance(requireContext()).getSelectedChildId();
            allGrades = DatabaseHelper.getInstance(requireContext()).getGradesForStudent(studentId);

            new Handler(Looper.getMainLooper()).post(() -> {
                if (!isAdded()) return;
                pbLoading.setVisibility(View.GONE);
                filterGrades();
            });
        }).start();
    }

    private void filterGrades() {
        List<Grade> filtered;
        if ("all".equals(selectedTrimester)) {
            filtered = new ArrayList<>(allGrades);
        } else {
            filtered = new ArrayList<>();
            for (Grade g : allGrades) {
                if (selectedTrimester.equals(g.getTrimester())) filtered.add(g);
            }
        }

        if (filtered.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvGrades.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvGrades.setVisibility(View.VISIBLE);
            rvGrades.setAdapter(new GradesAdapter(filtered));
        }

        // Compute average
        if (!filtered.isEmpty()) {
            float total = 0f, coefTotal = 0f;
            for (Grade g : filtered) {
                total     += (g.getValue() / g.getMaxValue()) * 20 * g.getCoefficient();
                coefTotal += g.getCoefficient();
            }
            float avg = coefTotal > 0 ? total / coefTotal : 0f;
            tvAverage.setText(String.format(Locale.FRENCH, "%.2f", avg));
        } else {
            tvAverage.setText("--");
        }
    }
}
