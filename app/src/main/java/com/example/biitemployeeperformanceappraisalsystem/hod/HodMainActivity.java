package com.example.biitemployeeperformanceappraisalsystem.hod;

import static com.example.biitemployeeperformanceappraisalsystem.helper.FragmentUtils.replaceFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.biitemployeeperformanceappraisalsystem.EvaluateeListFragment;
import com.example.biitemployeeperformanceappraisalsystem.MyTasksFragment;
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
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HodMainActivity extends AppCompatActivity {
    SharedPreferencesManager sharedPreferencesManager;
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

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        int employeeID = sharedPreferencesManager.getEmployeeUserObject().getEmployee().getEmployeeTypeId();
        replaceFragment(getSupportFragmentManager(),new PerformanceFragment(employeeID),fragmentContainer.getId());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.navigation_performance==item.getItemId()) {
                    // Replace content with the Report layout
                    topText.setText("Performance");
                    replaceFragment(getSupportFragmentManager(),new PerformanceFragment(employeeID),fragmentContainer.getId());
                    return true;
                }else if (R.id.navigation_my_tasks==item.getItemId()) {
                    // Handle "Tasks" click
                    topText.setText("Tasks");
                    replaceFragment(getSupportFragmentManager(),new MyTasksFragment(),fragmentContainer.getId());
                    return true;
                }
                else if (R.id.navigation_evaluate==item.getItemId()) {
                    topText.setText("Evaluate");
                    replaceFragment(getSupportFragmentManager(),new EvaluateeListFragment(fragmentContainer.getId()),fragmentContainer.getId());
                    return true;
                } else if (R.id.navigation_score==item.getItemId()) {
                    // Handle "Settings" click
                    topText.setText("Scores");
                    replaceFragment(getSupportFragmentManager(),new ScoresFragment(),fragmentContainer.getId());
                    return true;

                } else if (R.id.navigation_more==item.getItemId()) {
                    showPopupMenu(bottomNavigationView);
                    return true;
                }
                return false;
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.RIGHT);
        popupMenu.getMenuInflater().inflate(R.menu.hod_popup_menu, popupMenu.getMenu());
        // Set up click listener for items in the PopupMenu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (R.id.navigation_questionnaire==item.getItemId()) {
                    // Handle "Questionnaire" click
                    replaceFragment(getSupportFragmentManager(),new QuestionnaireFragment(),fragmentContainer.getId());
                    topText.setText("Questionnaire");
                    return true;
                }
                else if (R.id.navigation_tasks==item.getItemId()) {
                    replaceFragment(getSupportFragmentManager(),new TaskFragment(),fragmentContainer.getId());
                    topText.setText("Tasks");
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}