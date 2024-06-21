package com.example.biitemployeeperformanceappraisalsystem.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.biitemployeeperformanceappraisalsystem.EmployeeListFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {
    private TextView topText;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;

    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesManager.logoutUser();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        topText = findViewById(R.id.txt_top);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentContainer = findViewById(R.id.fragment_container);

        replaceFragment(new EmployeeListFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.navigation_employees==item.getItemId()) {
                    // Replace content with the Performance layout
                    topText.setText("Employees");
                    replaceFragment(new EmployeeListFragment());
                    return true;
                }
                else if (R.id.navigation_course_assignment==item.getItemId()) {
                    topText.setText("Assign Course");
                    replaceFragment(new UploadExcelFileFragment(false));
                    return true;
                }else if (R.id.navigation_chr==item.getItemId()) {
                    // Handle "Tasks" click
                    topText.setText("CHR");
                    replaceFragment(new UploadExcelFileFragment(true));
                    return true;
                } else if (R.id.navigation_settings==item.getItemId()) {
                    // Handle "Settings" click
                    topText.setText("Setting");
                    replaceFragment(new AdminSettingFragment());
                    return true;

                }
                return false;
            }
        });
    }

    // Method to replace the content of the FrameLayout with a new fragment
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}