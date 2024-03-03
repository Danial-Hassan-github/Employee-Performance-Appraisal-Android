package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.biitemployeeperformanceappraisalsystem.PieChartPerformanceFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.TaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DirectorMainActivity extends AppCompatActivity {

    private TextView topText;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_main);

        topText = findViewById(R.id.txt_top);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentContainer = findViewById(R.id.fragment_container);

        replaceFragment(new DirectorReportFragment());

        // Set up click listener for the "Settings" menu item
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.navigation_report==item.getItemId()) {
                    // Handle "Report" click
                    // Replace content with the Report layout
                    topText.setText("Report");
                    replaceFragment(new DirectorReportFragment());
                    return true;
                }
                else if (R.id.navigation_tasks==item.getItemId()) {
                    // Handle "Tasks" click
                    topText.setText("Tasks");
                    replaceFragment(new TaskFragment());
                    return true;
                } else if (R.id.navigation_evaluators==item.getItemId()) {
                    topText.setText("Evaluators");
                    replaceFragment(new EvaluatorFragment());
                    return true;
                } else if (R.id.navigation_settings==item.getItemId()) {
                    // Handle "Settings" click
                    // Show PopupMenu for additional options
                    showPopupMenu(bottomNavigationView);
                    return true;

                }
                return false;
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.RIGHT);
        popupMenu.getMenuInflater().inflate(R.menu.director_popup_menu, popupMenu.getMenu());
        // Set up click listener for items in the PopupMenu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (R.id.navigation_questionnaire==item.getItemId()) {
                    // Handle "Questionnaire" click
                    replaceFragment(new KpiFragment());
                    topText.setText("Questionnaire");
                    return true;
                }
                else if (R.id.navigation_kpi==item.getItemId()) {
                    // Handle "KPI" click
                    replaceFragment(new KpiFragment());
                    topText.setText("KPI");
                    return true;
                }
                else if (R.id.navigation_evaluation==item.getItemId()) {
                    // Handle "Evaluation" click
                    replaceFragment(new EvaluationFragment());
                    topText.setText("Evaluation");
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    // Method to replace the content of the FrameLayout with a new fragment
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
