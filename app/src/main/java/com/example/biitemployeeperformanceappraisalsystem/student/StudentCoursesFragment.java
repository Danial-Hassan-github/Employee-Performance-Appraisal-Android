package com.example.biitemployeeperformanceappraisalsystem.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.biitemployeeperformanceappraisalsystem.adapter.CourseListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;

import java.util.ArrayList;
import java.util.List;

public class StudentCoursesFragment extends Fragment {
    List<Course> studentCourseList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student_courses, container, false);


        StudentMainActivity studentMainActivity=(StudentMainActivity) getActivity();
        ListView listView = rootView.findViewById(R.id.courses_list_view);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getContext());

        // Sample data for demonstration
//        ArrayList<Course> courses = new ArrayList<>();
//        courses.add(new Course("Web Engineering", "CS-592"));
//        courses.add(new Course("Visual Programming", "CS-693"));
//        courses.add(new Course("Professional Practices", "CS-203"));
//        courses.add(new Course("Islamiat", "CS-211"));
//        courses.add(new Course("Numerical Analysis", "CS-492"));

        CourseService courseService=new CourseService(getContext());
        courseService.getStudentCourses(
                sharedPreferencesManager.getStudentUserObject().getId(),
                sharedPreferencesManager.getSessionId(),
                courses -> {
                    studentCourseList = courses;
                    CourseListAdapter adapter = new CourseListAdapter(getContext(), R.layout.courses_list_view_layout, studentCourseList);
                    listView.setAdapter(adapter);
                },
                errorMessage -> {
                    Toast.makeText(studentMainActivity, errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentMainActivity.replaceFragment(new CourseTeacherFragment());
            }
        });

        return rootView;
    }
}
