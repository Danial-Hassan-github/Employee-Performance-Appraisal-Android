package com.example.biitemployeeperformanceappraisalsystem.hod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.biitemployeeperformanceappraisalsystem.PerformanceFragment;
import com.example.biitemployeeperformanceappraisalsystem.QuestionnaireFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.ScoresFragment;
import com.example.biitemployeeperformanceappraisalsystem.TaskFragment;
import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.director.DirectorReportFragment;
import com.example.biitemployeeperformanceappraisalsystem.director.EvaluationFragment;
import com.example.biitemployeeperformanceappraisalsystem.director.EvaluatorFragment;
import com.example.biitemployeeperformanceappraisalsystem.director.KpiFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HodMainActivity extends AppCompatActivity {
    private TextView topText;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_main);

        topText = findViewById(R.id.txt_top);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentContainer = findViewById(R.id.fragment_container);

        // DirectorMainActivity directorMainActivity = new DirectorMainActivity();

        replaceFragment(new PerformanceFragment());

        // Set up click listener for the "Settings" menu item
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.navigation_performance==item.getItemId()) {
                    // Replace content with the Report layout
                    topText.setText("Performance");
                    replaceFragment(new PerformanceFragment());
                    return true;
                }
                else if (R.id.navigation_tasks==item.getItemId()) {
                    // Handle "Tasks" click
                    topText.setText("Tasks");
                    replaceFragment(new TaskFragment());
                    return true;
                } else if (R.id.navigation_evaluate==item.getItemId()) {
                    topText.setText("Evaluate");
                    replaceFragment(new EvaluatorFragment());
                    return true;
                } else if (R.id.navigation_score==item.getItemId()) {
                    // Handle "Settings" click
                    topText.setText("Scores");
                    replaceFragment(new ScoresFragment());
                    return true;

                } else if (R.id.navigation_questionnaire==item.getItemId()) {
                    topText.setText("Questionnaire");
                    replaceFragment(new QuestionnaireFragment());
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
                .commit();
    }
}