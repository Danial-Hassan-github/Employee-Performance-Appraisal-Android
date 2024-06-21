package com.example.biitemployeeperformanceappraisalsystem.faculty;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.EvaluationQuestionnaireFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CourseListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;
import com.example.biitemployeeperformanceappraisalsystem.student.CourseTeacherFragment;
import com.example.biitemployeeperformanceappraisalsystem.student.StudentMainActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherCoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherCoursesFragment extends Fragment {
    int teacherId;
    List<Course> teacherCourseList;
    View fragmentContainer;
    public TeacherCoursesFragment(){

    }

    public TeacherCoursesFragment(int teacherId){
        this.teacherId = teacherId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student_courses, container, false);


        FacultyMain facultyMain = (FacultyMain) getActivity();

        fragmentContainer = facultyMain.findViewById(R.id.fragment_container);
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
        courseService.getTeacherCourses(
                teacherId,
                sharedPreferencesManager.getSessionId(),
                courses -> {
                    teacherCourseList = courses;
                    CourseListAdapter adapter = new CourseListAdapter(getContext(), R.layout.courses_list_view_layout, teacherCourseList);
                    listView.setAdapter(adapter);
                },
                errorMessage -> {
                    Toast.makeText(facultyMain, errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course clickedCourse = teacherCourseList.get(position);
                int courseID = clickedCourse.getId();
                facultyMain.replaceFragment(new EvaluationQuestionnaireFragment(teacherId, courseID, "Peer", fragmentContainer.getId()));
            }
        });

        return rootView;
    }
}