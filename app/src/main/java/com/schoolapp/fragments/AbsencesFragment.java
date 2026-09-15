package com.schoolapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.adapters.AbsencesAdapter;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.Absence;
import com.schoolapp.utils.PrefsManager;
import java.util.List;

public class AbsencesFragment extends Fragment {

    private RecyclerView rvAbsences;
    private TextView tvTotalAbs, tvTotalLate, tvEmpty;
    private ProgressBar pbLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_absences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAbsences  = view.findViewById(R.id.rv_absences);
        tvTotalAbs  = view.findViewById(R.id.tv_total_absences);
        tvTotalLate = view.findViewById(R.id.tv_total_late);
        tvEmpty     = view.findViewById(R.id.tv_empty);
        pbLoading   = view.findViewById(R.id.pb_loading);

        rvAbsences.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAbsences();
    }

    private void loadAbsences() {
        pbLoading.setVisibility(View.VISIBLE);

        new Thread(() -> {
            int studentId = PrefsManager.getInstance(requireContext()).getSelectedChildId();
            List<Absence> absences = DatabaseHelper.getInstance(requireContext())
                    .getAbsencesForStudent(studentId);

            int countAbs = 0, countLate = 0;
            for (Absence a : absences) {
                if (a.isLate()) countLate++; else countAbs++;
            }
            final int fAbs = countAbs, fLate = countLate;

            new Handler(Looper.getMainLooper()).post(() -> {
                if (!isAdded()) return;
                pbLoading.setVisibility(View.GONE);
                tvTotalAbs.setText(String.valueOf(fAbs));
                tvTotalLate.setText(String.valueOf(fLate));

                if (absences.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    rvAbsences.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    rvAbsences.setVisibility(View.VISIBLE);
                    rvAbsences.setAdapter(new AbsencesAdapter(absences, getContext()));
                }
            });
        }).start();
    }
}
