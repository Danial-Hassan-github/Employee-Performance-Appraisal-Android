package com.example.biitemployeeperformanceappraisalsystem.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import com.example.biitemployeeperformanceappraisalsystem.adapter.CourseListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;

import java.util.ArrayList;

public class StudentCoursesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student_courses, container, false);


        StudentMainActivity studentMainActivity=(StudentMainActivity) getActivity();
        ListView listView = rootView.findViewById(R.id.courses_list_view);

        // Sample data for demonstration
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("Web Engineering", "CS-592"));
        courses.add(new Course("Visual Programming", "CS-693"));
        courses.add(new Course("Professional Practices", "CS-203"));
        courses.add(new Course("Islamiat", "CS-211"));
        courses.add(new Course("Numerical Analysis", "CS-492"));

        CourseListAdapter adapter = new CourseListAdapter(getContext(), courses);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentMainActivity.replaceFragment(new CourseTeacherFragment());
            }
        });

        return rootView;
    }
}
