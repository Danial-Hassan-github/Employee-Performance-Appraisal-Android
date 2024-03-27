package com.example.biitemployeeperformanceappraisalsystem.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.EvaluationQuestionnaireFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseTeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseTeacherFragment extends Fragment {

    int studentID, courseID, sessionID;
    SharedPreferencesManager sharedPreferencesManager;
    List<Employee> teacherList;

    public CourseTeacherFragment(int courseID){
        this.courseID=courseID;
    }
    public static CourseTeacherFragment newInstance(int studentID, int courseID, int sessionID) {
        CourseTeacherFragment fragment = new CourseTeacherFragment(courseID);
        Bundle args = new Bundle();
        args.putInt("studentID", studentID);
        args.putInt("courseID", courseID);
        args.putInt("sessionID", sessionID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentID = getArguments().getInt("studentID");
            courseID = getArguments().getInt("courseID");
            sessionID = getArguments().getInt("sessionID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_teacher, container, false);
        TextView txt_teacher1=view.findViewById(R.id.text_teacher1);
        TextView txt_teacher2=view.findViewById(R.id.text_teacher2);
        sharedPreferencesManager=new SharedPreferencesManager(getContext());

        CourseService courseService=new CourseService(getContext());
        courseService.getCourseTeachers(
                sharedPreferencesManager.getStudentUserObject().getId(),
                1,
                sharedPreferencesManager.getSessionId(),
                teachers -> {
                    teacherList = teachers;
                    txt_teacher1.setVisibility(View.VISIBLE);
                    txt_teacher1.setText(teachers.get(0).getName());
                    if (teachers.size()>1){
                        txt_teacher2.setVisibility(View.VISIBLE);
                        txt_teacher2.setText(teachers.get(1).getName());
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
                }
        );

        StudentMainActivity studentMainActivity=(StudentMainActivity) getActivity();
        TextView textView1=studentMainActivity.findViewById(R.id.txt_top);
        textView1.setText("Teachers");
        // Define a common OnClickListener for both text views
        View.OnClickListener teacherClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the index of the clicked teacher
                int index = (v == txt_teacher1) ? 0 : 1;
                // Pass the ID of the clicked teacher
                int teacherId = teacherList.get(index).getId();
                textView1.setText("Evaluate");
                studentMainActivity.replaceFragment(new EvaluationQuestionnaireFragment(teacherId,courseID));
            }
        };

        // Set the same OnClickListener for both text views
        txt_teacher1.setOnClickListener(teacherClickListener);
        txt_teacher2.setOnClickListener(teacherClickListener);
        // Inflate the layout for this fragment
        return view;
    }
}