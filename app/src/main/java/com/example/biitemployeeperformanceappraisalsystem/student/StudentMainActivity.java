package com.example.biitemployeeperformanceappraisalsystem.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.biitemployeeperformanceappraisalsystem.R;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        replaceFragment(new StudentCoursesFragment());

    }

    // Method to replace the content of the FrameLayout with a new fragment
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}