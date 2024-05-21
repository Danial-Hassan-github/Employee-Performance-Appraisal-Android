package com.example.biitemployeeperformanceappraisalsystem.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.EvaluationQuestionnaireFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.FragmentUtils;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationTimeService;
import com.google.android.material.tabs.TabLayout;

public class StudentMainActivity extends AppCompatActivity {
    int studentID;
    EditText editTextPin;
    Button btnSubmitPin;
    FragmentUtils fragmentUtils;
    EvaluationTimeService evaluationTimeService;
    SharedPreferencesManager sharedPreferencesManager;
    TabLayout tabLayout;
    View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        fragmentContainer = findViewById(R.id.fragment_container);
        fragmentUtils = new FragmentUtils();

        editTextPin = findViewById(R.id.editText_pin);
        btnSubmitPin = findViewById(R.id.btn_pin_submit);
        tabLayout = findViewById(R.id.tab_layout_student);

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        evaluationTimeService = new EvaluationTimeService(getApplicationContext());

        studentID = sharedPreferencesManager.getStudentUserObject().getId();
        replaceFragment(new StudentCoursesFragment());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        replaceFragment(new StudentCoursesFragment());
                        break;
                    case 1:
                        evaluationTimeService.checkDegreeExitEligibility(
                                studentID,
                                studentSupervisor -> {
                                    replaceFragment(new EvaluationQuestionnaireFragment(studentSupervisor.getSupervisorId(), "degree exit", fragmentContainer.getId()));
                                },
                                errorMessage -> {
                                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnSubmitPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluationTimeService.checkConfidentialPin(
                        sharedPreferencesManager.getSessionId(),
                        editTextPin.getText().toString(),
                        isConfidential -> {
                            sharedPreferencesManager.setKeyIsConfidential(isConfidential);
                        },
                        errorMessage -> {
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
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