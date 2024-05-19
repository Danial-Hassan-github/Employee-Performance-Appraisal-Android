package com.example.biitemployeeperformanceappraisalsystem.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.EvaluationQuestionnaireFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationTimeService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseTeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseTeacherFragment extends Fragment {

    int studentID, courseID, sessionID;
    boolean isConfidential = false;
    SharedPreferencesManager sharedPreferencesManager;
    List<Employee> teacherList;
    EvaluationService evaluationService;
    EvaluationTimeService evaluationTimeService;
    View fragmentContainer;
    RadioGroup evaluationTypeRadioGroup;

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
        evaluationTypeRadioGroup = view.findViewById(R.id.evaluation_type_radio_group);
        TextView txt_teacher1=view.findViewById(R.id.text_teacher1);
        TextView txt_teacher2=view.findViewById(R.id.text_teacher2);
        sharedPreferencesManager=new SharedPreferencesManager(getContext());
        evaluationService = new EvaluationService(getContext());
        evaluationTimeService = new EvaluationTimeService(getContext());
        studentID = sharedPreferencesManager.getStudentUserObject().getId();
        sessionID = sharedPreferencesManager.getSessionId();

        evaluationTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle radio button selection changes here
                if (checkedId == R.id.rd_student){
                    isConfidential = false;
                }else {
                    isConfidential = true;
                }
            }
        });

        CourseService courseService=new CourseService(getContext());
        courseService.getCourseTeachers(
                studentID,
                courseID,
                sessionID,
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
        fragmentContainer = studentMainActivity.findViewById(R.id.fragment_container);
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
                evaluationService.IsEvaluated(
                        studentID,
                        teacherId,
                        courseID,
                        sessionID,
                        isConfidential ? "Confidential" : "Student",
                        result -> {
                            boolean check = result;
                            if (check){
                                Toast.makeText(getContext(), "You have already Evaluated this teacher", Toast.LENGTH_SHORT).show();
                            }else {
                                String evaluationType = isConfidential ? "Confidential" : "Student";
                                evaluationTimeService.isEvaluationTime(
                                        sessionID,
                                        evaluationType,
                                        isTime -> {
                                            boolean flag = isTime;
                                            if (flag){
                                                studentMainActivity.replaceFragment(new EvaluationQuestionnaireFragment(teacherId, courseID, isConfidential?"Confidential":"Student", fragmentContainer.getId()));
                                            }else {
                                                Toast.makeText(getContext(), "Evaluation not opened", Toast.LENGTH_SHORT).show();
                                            }
                                        },
                                        errorMessage -> {
                                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                        }
                                        );
                            }
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show();
                        });
            }
        };

        // Set the same OnClickListener for both text views
        txt_teacher1.setOnClickListener(teacherClickListener);
        txt_teacher2.setOnClickListener(teacherClickListener);
        // Inflate the layout for this fragment
        return view;
    }
}