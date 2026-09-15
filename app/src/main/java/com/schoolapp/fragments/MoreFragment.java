package com.schoolapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.schoolapp.R;
import com.schoolapp.activities.*;
import com.schoolapp.utils.PrefsManager;
import android.view.View;

public class MoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PrefsManager prefs = PrefsManager.getInstance(requireContext());

        view.findViewById(R.id.card_map).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), MapActivity.class)));

        view.findViewById(R.id.card_documents).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), DocumentActivity.class)));

        view.findViewById(R.id.card_profile).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ChildProfileActivity.class)));

        view.findViewById(R.id.card_settings).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.fragment_container, new SettingsFragment(), "settings")
                        .addToBackStack(null)
                        .commit());

        view.findViewById(R.id.card_messages).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            intent.putExtra("sender_name", "M Gahi");
            startActivity(intent);
        });

        // Si Élève → cacher le profil multi-enfants
        if (!prefs.isParent()) {
            view.findViewById(R.id.card_profile).setVisibility(View.GONE);
        }
    }




}

