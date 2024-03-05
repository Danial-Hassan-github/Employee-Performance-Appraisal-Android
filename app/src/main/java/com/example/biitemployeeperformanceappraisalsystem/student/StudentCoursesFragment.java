package com.example.biitemployeeperformanceappraisalsystem.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import com.example.biitemployeeperformanceappraisalsystem.CourseListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;

import java.util.ArrayList;

public class StudentCoursesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student_courses, container, false);

        ListView listView = rootView.findViewById(R.id.courses_list_view);

        // Sample data for demonstration
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("Web Engineering", "CS-211"));
        courses.add(new Course("Visual Programming", "CS-102"));
        courses.add(new Course("Professional Practices", "CS-103"));
        courses.add(new Course("Islamiat", "CS-101"));
        courses.add(new Course("Numerical Analysis", "CS-102"));

        CourseListAdapter adapter = new CourseListAdapter(getContext(), courses);
        listView.setAdapter(adapter);

        return rootView;
    }
}
