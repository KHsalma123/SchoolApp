package com.schoolapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.schoolapp.R;
import com.schoolapp.activities.ChildProfileActivity;
import com.schoolapp.activities.LoginActivity;
import com.schoolapp.activities.MapActivity;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.Student;
import com.schoolapp.utils.PrefsManager;
import java.util.List;
import android.view.View;

public class SettingsFragment extends Fragment {

    private SwitchMaterial switchNotifGrades, switchNotifAbsences, switchNotifMessages;
    private LinearLayout layoutChildren;
    private TextView tvUserName, tvUserEmail, tvUserRole;
    private MaterialButton btnLogout, btnMap, btnDocuments;
    private RadioGroup rgTheme;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName          = view.findViewById(R.id.tv_user_name);
        tvUserEmail         = view.findViewById(R.id.tv_user_email);
        tvUserRole          = view.findViewById(R.id.tv_user_role);
        switchNotifGrades   = view.findViewById(R.id.switch_notif_grades);
        switchNotifAbsences = view.findViewById(R.id.switch_notif_absences);
        switchNotifMessages = view.findViewById(R.id.switch_notif_messages);
        layoutChildren      = view.findViewById(R.id.layout_children);
        btnLogout           = view.findViewById(R.id.btn_logout);
        btnMap              = view.findViewById(R.id.btn_map);
        btnDocuments        = view.findViewById(R.id.btn_documents);
        rgTheme             = view.findViewById(R.id.rg_theme);

        loadSettings();
        loadChildren();
        setupListeners();

        // Masquer section enfants pour Élève
        PrefsManager prefs = PrefsManager.getInstance(requireContext());
        if (!prefs.isParent()) {
            layoutChildren.setVisibility(View.GONE);
        }
    }

    private void loadSettings() {
        PrefsManager prefs = PrefsManager.getInstance(requireContext());

        tvUserName.setText(prefs.getUserName());
        tvUserEmail.setText(prefs.getUserEmail());
        tvUserRole.setText(prefs.isParent() ? "Parent" : "Élève");

        switchNotifGrades.setChecked(prefs.isNotifGrades());
        switchNotifAbsences.setChecked(prefs.isNotifAbsences());
        switchNotifMessages.setChecked(prefs.isNotifMessages());

        String theme = prefs.getThemeMode();
        if (PrefsManager.THEME_DARK.equals(theme)) {
            rgTheme.check(R.id.rb_dark);
        } else if (PrefsManager.THEME_SYSTEM.equals(theme)) {
            rgTheme.check(R.id.rb_system);
        } else {
            rgTheme.check(R.id.rb_light);
        }
    }

    private void loadChildren() {
        new Thread(() -> {
            List<Student> students = DatabaseHelper.getInstance(requireContext()).getAllStudents();
            int selectedId = PrefsManager.getInstance(requireContext()).getSelectedChildId();

            requireActivity().runOnUiThread(() -> {
                if (!isAdded()) return;
                layoutChildren.removeAllViews();
                for (Student s : students) {
                    View childView = LayoutInflater.from(getContext())
                            .inflate(R.layout.item_child_selector, layoutChildren, false);

                    TextView tvName    = childView.findViewById(R.id.tv_child_name);
                    TextView tvClass   = childView.findViewById(R.id.tv_child_class);
                    TextView tvInit    = childView.findViewById(R.id.tv_child_initial);
                    ImageView ivCheck  = childView.findViewById(R.id.iv_check);

                    tvName.setText(s.getFullName());
                    tvClass.setText(s.getClassName() + " • " + s.getSchoolName());
                    tvInit.setText(s.getInitials());
                    ivCheck.setVisibility(s.getId() == selectedId ? View.VISIBLE : View.GONE);

                    childView.setOnClickListener(v -> {
                        PrefsManager.getInstance(requireContext()).setSelectedChild(s.getId());
                        loadChildren(); // refresh check
                    });

                    childView.setOnLongClickListener(v -> {
                        PrefsManager.getInstance(requireContext()).setSelectedChild(s.getId());
                        Intent intent = new Intent(getActivity(), ChildProfileActivity.class);
                        startActivity(intent);
                        return true;
                    });

                    layoutChildren.addView(childView);
                }
            });
        }).start();
    }

    private void setupListeners() {
        PrefsManager prefs = PrefsManager.getInstance(requireContext());

        switchNotifGrades.setOnCheckedChangeListener((b, checked) ->
                prefs.setNotifGrades(checked));
        switchNotifAbsences.setOnCheckedChangeListener((b, checked) ->
                prefs.setNotifAbsences(checked));
        switchNotifMessages.setOnCheckedChangeListener((b, checked) ->
                prefs.setNotifMessages(checked));

        rgTheme.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_dark) {
                prefs.setThemeMode(PrefsManager.THEME_DARK);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (checkedId == R.id.rb_system) {
                prefs.setThemeMode(PrefsManager.THEME_SYSTEM);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            } else {
                prefs.setThemeMode(PrefsManager.THEME_LIGHT);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        btnMap.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), MapActivity.class)));

        btnLogout.setOnClickListener(v -> {
            prefs.clearSession();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
