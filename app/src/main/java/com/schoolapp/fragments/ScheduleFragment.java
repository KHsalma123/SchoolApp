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
import com.google.android.material.tabs.TabLayout;
import com.schoolapp.R;
import com.schoolapp.adapters.ScheduleAdapter;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.ScheduleSlot;
import com.schoolapp.network.RetrofitClient;
import com.schoolapp.utils.PrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.*;

/**
 * ── DÉMO RETROFIT / API REST ─────────────────────────────────────────────
 * L'emploi du temps est désormais chargé depuis MockAPI.io via Retrofit
 * (voir RetrofitClient.java + ScheduleApiService.java) au lieu de SQLite.
 *
 * Si l'appel réseau échoue (pas de connexion, MockAPI indisponible, etc.),
 * on retombe automatiquement sur les données locales SQLite — ça évite
 * un écran vide pendant la démonstration si le réseau de la salle pose
 * problème, tout en montrant clairement l'usage de Retrofit au prof
 * (visible dans Logcat via le HttpLoggingInterceptor déjà configuré).
 */
public class ScheduleFragment extends Fragment {

    private TabLayout tabDays;
    private RecyclerView rvSchedule;
    private TextView tvEmpty;
    private ProgressBar pbLoading;

    private final String[] DAYS = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"};
    private int currentDayIndex = 0;

    // Cache local de tout l'emploi du temps récupéré depuis MockAPI,
    // pour éviter de refaire un appel réseau à chaque changement d'onglet.
    private List<ScheduleSlot> allSlotsFromApi = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabDays   = view.findViewById(R.id.tab_days);
        rvSchedule= view.findViewById(R.id.rv_schedule);
        tvEmpty   = view.findViewById(R.id.tv_empty);
        pbLoading = view.findViewById(R.id.pb_loading);

        rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));

        // Build day tabs
        for (String day : DAYS) {
            tabDays.addTab(tabDays.newTab().setText(day));
        }

        // Default: today
        Calendar cal = Calendar.getInstance();
        int todayIdx = cal.get(Calendar.DAY_OF_WEEK) - 2;
        currentDayIndex = (todayIdx >= 0 && todayIdx < 6) ? todayIdx : 0;
        tabDays.selectTab(tabDays.getTabAt(currentDayIndex));

        // Premier chargement : appel réseau MockAPI
        fetchScheduleFromApi();

        tabDays.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentDayIndex = tab.getPosition();
                if (allSlotsFromApi != null) {
                    // On a déjà les données en cache, on filtre juste localement
                    displayDay(currentDayIndex);
                } else {
                    // Pas encore de données (ex: premier appel encore en cours
                    // ou a échoué) → on retente l'appel réseau
                    fetchScheduleFromApi();
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /**
     * Appel Retrofit vers MockAPI : GET /schedule → renvoie TOUT
     * l'emploi du temps (tous élèves, tous jours), qu'on filtre ensuite
     * localement par élève sélectionné et par jour.
     */
    private void fetchScheduleFromApi() {
        if (!isAdded()) return;
        pbLoading.setVisibility(View.VISIBLE);
        rvSchedule.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);

        Call<List<ScheduleSlot>> call = RetrofitClient.getInstance()
                .getScheduleApi()
                .getAllSchedule();

        call.enqueue(new Callback<List<ScheduleSlot>>() {
            @Override
            public void onResponse(Call<List<ScheduleSlot>> call, Response<List<ScheduleSlot>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    allSlotsFromApi = response.body();
                    displayDay(currentDayIndex);
                } else {
                    // Réponse serveur invalide → fallback SQLite
                    loadScheduleFromSqliteFallback(currentDayIndex);
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleSlot>> call, Throwable t) {
                // Pas de réseau / MockAPI indisponible → fallback SQLite
                if (!isAdded()) return;
                loadScheduleFromSqliteFallback(currentDayIndex);
            }
        });
    }

    /**
     * Filtre les données déjà récupérées de l'API (en cache) pour
     * n'afficher que celles de l'élève sélectionné et du jour choisi.
     */
    private void displayDay(int dayIndex) {
        if (!isAdded() || allSlotsFromApi == null) return;

        int studentId = PrefsManager.getInstance(requireContext()).getSelectedChildId();
        List<ScheduleSlot> filtered = new ArrayList<>();
        for (ScheduleSlot slot : allSlotsFromApi) {
            if (slot.getStudentId() == studentId && slot.getDayIndex() == dayIndex) {
                filtered.add(slot);
            }
        }
        renderSlots(filtered);
    }

    /**
     * Filet de sécurité : si MockAPI est inaccessible, on retombe sur
     * les données locales SQLite pour ne jamais laisser un écran vide.
     */
    private void loadScheduleFromSqliteFallback(int dayIndex) {
        new Thread(() -> {
            int studentId = PrefsManager.getInstance(requireContext()).getSelectedChildId();
            List<ScheduleSlot> slots = DatabaseHelper.getInstance(requireContext())
                    .getScheduleForStudentAndDay(studentId, dayIndex);

            new Handler(Looper.getMainLooper()).post(() -> {
                if (!isAdded()) return;
                renderSlots(slots);
                Toast.makeText(getContext(),
                        "Hors-ligne : affichage des données locales",
                        Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private void renderSlots(List<ScheduleSlot> slots) {
        if (!isAdded()) return;
        pbLoading.setVisibility(View.GONE);
        if (slots.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvSchedule.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvSchedule.setVisibility(View.VISIBLE);
            rvSchedule.setAdapter(new ScheduleAdapter(slots));
        }
    }
}