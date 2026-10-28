package com.example.bulletinboard;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private boolean isUpdatingBottomNav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bnv_bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (isUpdatingBottomNav) {
                return true;
            }
            int itemId = item.getItemId();
            if (itemId == R.id.btn_events) {
                replaceFragment(new ListScreen());
                return true;
            } else if (itemId == R.id.btn_map) {
                replaceFragment(new MapScreen());
                return true;
            } else if (itemId == R.id.btn_account) {
                replaceFragment(new StudentAccountScreen());
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            Fragment initialFragment = new LoginScreen();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcv_fragmentContainer, initialFragment)
                    .commit();
            updateBottomNavForFragment(initialFragment);
        }
    }

    public void replaceFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fcv_fragmentContainer);
        if (currentFragment != null && currentFragment.getClass() == fragment.getClass()) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_fragmentContainer, fragment)
                .commit();

        updateBottomNavForFragment(fragment);
    }

    private void updateBottomNavForFragment(Fragment fragment) {
        if (bottomNavigationView == null) return;

        if (fragment instanceof ListScreen
                || fragment instanceof MapScreen
                || fragment instanceof StudentAccountScreen
                || fragment instanceof OrgAccountScreen) {

            bottomNavigationView.setVisibility(View.VISIBLE);

            isUpdatingBottomNav = true;
            if (fragment instanceof ListScreen) {
                bottomNavigationView.setSelectedItemId(R.id.btn_events);
            } else if (fragment instanceof MapScreen) {
                bottomNavigationView.setSelectedItemId(R.id.btn_map);
            } else {
                bottomNavigationView.setSelectedItemId(R.id.btn_account);
            }
            isUpdatingBottomNav = false;

        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }
}