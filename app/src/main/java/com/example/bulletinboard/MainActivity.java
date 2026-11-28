package com.example.bulletinboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.config.IConfigurationProvider;
import java.io.File;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private boolean isUpdatingBottomNav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(
                getApplicationContext(),
                getSharedPreferences("osmdroid", MODE_PRIVATE)
        );

        Configuration.getInstance().setUserAgentValue(
                getPackageName() + "/1.0 (contact: YOUR_EMAIL)"
        );

        Configuration.getInstance().setDebugMapTileDownloader(true);

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

    private void configureOSMDroid()
    {
        IConfigurationProvider config = Configuration.getInstance();

        config.load(
                getApplicationContext(),
                getSharedPreferences("osmdroid", MODE_PRIVATE)
        );

        config.setUserAgentValue(
                getPackageName() + "/1.0 (contact: haydeneicke@hotmail.com)"
        );

        // Persistent tile storage
        File basePath = new File(getFilesDir(), "osmdroid");
        File tileCache = new File(basePath, "tiles");

        basePath.mkdirs();
        tileCache.mkdirs();

        config.setOsmdroidBasePath(basePath);
        config.setOsmdroidTileCache(tileCache);

        // Cache size: 100 MB maximum, trim to 80 MB
        config.setTileFileSystemCacheMaxBytes(100L * 1024 * 1024);
        config.setTileFileSystemCacheTrimBytes(80L * 1024 * 1024);

        // Limit concurrent downloads
        config.setTileDownloadThreads((short) 2);
        config.setTileDownloadMaxQueueSize((short) 40);

        // Debugging
        config.setDebugMode(false);
        config.setDebugMapTileDownloader(true);
        config.setDebugTileProviders(false);
    }
}