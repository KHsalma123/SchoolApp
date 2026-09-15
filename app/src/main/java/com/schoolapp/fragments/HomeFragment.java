package com.schoolapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.schoolapp.R;
import com.schoolapp.adapters.*;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.*;
import com.schoolapp.utils.PrefsManager;
import java.util.*;

public class HomeFragment extends Fragment {

    private TextView tvGreeting, tvUserName, tvStudentName, tvClassName, tvAverage;
    private TextView tvNextSubject, tvNextTime, tvNextRoom;
    private RecyclerView rvRecentGrades, rvAlerts;
    private View cardNextClass;
    private ProgressBar pbLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvGreeting     = view.findViewById(R.id.tv_greeting);
        tvUserName     = view.findViewById(R.id.tv_user_name);
        tvStudentName  = view.findViewById(R.id.tv_student_name);
        tvClassName    = view.findViewById(R.id.tv_class_name);
        tvAverage      = view.findViewById(R.id.tv_average);
        tvNextSubject  = view.findViewById(R.id.tv_next_subject);
        tvNextTime     = view.findViewById(R.id.tv_next_time);
        tvNextRoom     = view.findViewById(R.id.tv_next_room);
        cardNextClass  = view.findViewById(R.id.card_next_class);
        rvRecentGrades = view.findViewById(R.id.rv_recent_grades);
        pbLoading      = view.findViewById(R.id.pb_loading);

        rvRecentGrades.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        loadData();
    }

    private void loadData() {
        pbLoading.setVisibility(View.VISIBLE);

        // AsyncTask pattern using background thread
        new Thread(() -> {
            PrefsManager prefs = PrefsManager.getInstance(requireContext());
            DatabaseHelper db  = DatabaseHelper.getInstance(requireContext());

            int studentId = prefs.getSelectedChildId();
            Student student    = db.getStudent(studentId);
            List<Grade> grades = db.getGradesForStudent(studentId);

            // Get today's day index (0=Monday … 4=Friday)
            Calendar cal = Calendar.getInstance();
            int todayIdx = cal.get(Calendar.DAY_OF_WEEK) - 2; // Calendar.MONDAY=2
            if (todayIdx < 0 || todayIdx > 4) todayIdx = 0;
            List<ScheduleSlot> todaySlots = db.getScheduleForStudentAndDay(studentId, todayIdx);

            // Find next class
            ScheduleSlot nextSlot = findNextSlot(todaySlots);

            // Compute average
            float avg = 0f;
            if (!grades.isEmpty()) {
                float total = 0f, coefTotal = 0f;
                for (Grade g : grades) {
                    total     += (g.getValue() / g.getMaxValue()) * 20 * g.getCoefficient();
                    coefTotal += g.getCoefficient();
                }
                avg = coefTotal > 0 ? total / coefTotal : 0f;
            }

            final float finalAvg     = avg;
            final Student finalStudent = student;
            final List<Grade> recentGrades = grades.size() > 5 ? grades.subList(0, 5) : grades;
            final ScheduleSlot finalNext = nextSlot;
            final String userName = prefs.getUserName();

            new Handler(Looper.getMainLooper()).post(() -> {
                pbLoading.setVisibility(View.GONE);
                if (!isAdded()) return;

                // Greeting
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                String greeting = hour < 12 ? "Bonjour," : hour < 18 ? "Bon après-midi," : "Bonsoir,";
                tvGreeting.setText(greeting);
                tvUserName.setText(userName);

                if (finalStudent != null) {
                    tvStudentName.setText(finalStudent.getFullName());
                    tvClassName.setText(finalStudent.getClassName() + " • " + finalStudent.getSchoolName());
                    tvAverage.setText(String.format(Locale.FRENCH, "%.1f", finalAvg));
                }

                // Next class
                if (finalNext != null) {
                    tvNextSubject.setText(finalNext.getSubject());
                    tvNextTime.setText(finalNext.getStartTime() + " – " + finalNext.getEndTime());
                    tvNextRoom.setText("Salle " + finalNext.getRoom());
                    cardNextClass.setVisibility(View.VISIBLE);
                } else {
                    cardNextClass.setVisibility(View.GONE);
                }

                // Recent grades adapter
                GradesMiniAdapter adapter = new GradesMiniAdapter(recentGrades);
                rvRecentGrades.setAdapter(adapter);
            });
        }).start();
    }

    private ScheduleSlot findNextSlot(List<ScheduleSlot> slots) {
        if (slots == null || slots.isEmpty()) return null;
        String currentTime = String.format(Locale.FRENCH, "%02d:%02d",
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE));
        for (ScheduleSlot slot : slots) {
            if (slot.getStartTime().compareTo(currentTime) > 0) return slot;
        }
        return null;
    }
}
