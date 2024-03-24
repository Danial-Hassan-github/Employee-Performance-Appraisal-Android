package com.example.biitemployeeperformanceappraisalsystem.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.biitemployeeperformanceappraisalsystem.EvaluationQuestionnaireFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseTeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseTeacherFragment extends Fragment {

    int studentID, courseID, sessionID;
    public static CourseTeacherFragment newInstance(int studentID, int courseID, int sessionID) {
        CourseTeacherFragment fragment = new CourseTeacherFragment();
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

        StudentMainActivity studentMainActivity=(StudentMainActivity) getActivity();
        TextView textView1=studentMainActivity.findViewById(R.id.txt_top);
        textView1.setText("Teachers");
        // Define a common OnClickListener for both text views
        View.OnClickListener teacherClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("Evaluate");
                studentMainActivity.replaceFragment(new EvaluationQuestionnaireFragment());
            }
        };

        // Set the same OnClickListener for both text views
        txt_teacher1.setOnClickListener(teacherClickListener);
        txt_teacher2.setOnClickListener(teacherClickListener);
        // Inflate the layout for this fragment
        return view;
    }
}