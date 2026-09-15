package com.schoolapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.schoolapp.R;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.fragments.*;
import com.schoolapp.utils.PrefsManager;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if      (id == R.id.nav_home)     showFragment(new HomeFragment(),     "home");
            else if (id == R.id.nav_schedule) showFragment(new ScheduleFragment(), "schedule");
            else if (id == R.id.nav_grades)   showFragment(new GradesFragment(),   "grades");
            else if (id == R.id.nav_absences) showFragment(new AbsencesFragment(), "absences");
            else if (id == R.id.nav_more)     showFragment(new MoreFragment(),     "more");
            return true;
        });

        String tab = getIntent().getStringExtra("tab");
        if      ("grades".equals(tab))   bottomNav.setSelectedItemId(R.id.nav_grades);
        else if ("absences".equals(tab)) bottomNav.setSelectedItemId(R.id.nav_absences);
        else if ("messages".equals(tab)) bottomNav.setSelectedItemId(R.id.nav_more);
        else                             bottomNav.setSelectedItemId(R.id.nav_home);

        updateMessageBadge();
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.fragment_container, fragment, tag);
        ft.commit();
    }

    public void updateMessageBadge() {
        int userId = PrefsManager.getInstance(this).getUserId();
        int unread = DatabaseHelper.getInstance(this).getUnreadMessageCount(userId);
        BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_more);
        badge.setVisible(unread > 0);
        if (unread > 0) badge.setNumber(unread);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String tab = intent.getStringExtra("tab");
        if      ("grades".equals(tab))   bottomNav.setSelectedItemId(R.id.nav_grades);
        else if ("absences".equals(tab)) bottomNav.setSelectedItemId(R.id.nav_absences);
        else if ("messages".equals(tab)) bottomNav.setSelectedItemId(R.id.nav_more);
    }


}
