package com.example.biitemployeeperformanceappraisalsystem.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.biitemployeeperformanceappraisalsystem.network.services.StudentService;

import java.util.ArrayList;
import java.util.List;

public class CourseTeacherFragment extends Fragment {

    int studentID, courseID, sessionID;
    boolean isConfidential = false;
    SharedPreferencesManager sharedPreferencesManager;
    List<Employee> teacherList;
    EvaluationService evaluationService;
    EvaluationTimeService evaluationTimeService;
    View fragmentContainer;
    ListView listView;
    EditText editTextPin;
    Button btnSubmitPin;
    // RadioGroup evaluationTypeRadioGroup;

    public CourseTeacherFragment() {
        isConfidential = true;
    }

    public CourseTeacherFragment(int courseID) {
        this.courseID = courseID;
        isConfidential = false;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_teacher, container, false);
        // evaluationTypeRadioGroup = view.findViewById(R.id.evaluation_type_radio_group);
        listView = view.findViewById(R.id.teachers_list_view);
        editTextPin = view.findViewById(R.id.editText_pin);
        btnSubmitPin = view.findViewById(R.id.btn_pin_submit);

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        evaluationService = new EvaluationService(getContext());
        evaluationTimeService = new EvaluationTimeService(getContext());
        studentID = sharedPreferencesManager.getStudentUserObject().getId();
        sessionID = sharedPreferencesManager.getSessionId();

        /*evaluationTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_student) {
                    isConfidential = false;
                } else {
                    isConfidential = true;
                }
            }
        });*/

        if (!sharedPreferencesManager.isConfidential() && isConfidential){
            listView.setVisibility(View.GONE);
        }else {
            editTextPin.setVisibility(View.GONE);
            btnSubmitPin.setVisibility(View.GONE);
        }

        CourseService courseService = new CourseService(getContext());
        StudentService studentService = new StudentService(getContext());
        if (isConfidential){
            studentService.getStudentSessionTeacher(
                    studentID,
                    sessionID,
                    teachers -> {
                        teacherList = teachers;
                        List<String> teacherNames = new ArrayList<>();
                        for (Employee teacher : teachers) {
                            teacherNames.add(teacher.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, teacherNames);
                        listView.setAdapter(adapter);
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }else {
            courseService.getCourseTeachers(
                    studentID,
                    courseID,
                    sessionID,
                    teachers -> {
                        teacherList = teachers;
                        List<String> teacherNames = new ArrayList<>();
                        for (Employee teacher : teachers) {
                            teacherNames.add(teacher.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, teacherNames);
                        listView.setAdapter(adapter);
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }

        btnSubmitPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluationTimeService.checkConfidentialPin(
                        sharedPreferencesManager.getSessionId(),
                        editTextPin.getText().toString(),
                        isConfidential -> {
                            sharedPreferencesManager.setKeyIsConfidential(isConfidential);
                            if (isConfidential) {
                                listView.setVisibility(View.VISIBLE);
                                editTextPin.setVisibility(View.GONE);
                                btnSubmitPin.setVisibility(View.GONE);
                            }
                            else{
                                listView.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Incorrect Pin", Toast.LENGTH_SHORT).show();
                            }
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });

        StudentMainActivity studentMainActivity = (StudentMainActivity) getActivity();
        fragmentContainer = studentMainActivity.findViewById(R.id.fragment_container);
        TextView textView1 = studentMainActivity.findViewById(R.id.txt_top);
        textView1.setText("Teachers");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee selectedTeacher = teacherList.get(position);
                int teacherId = selectedTeacher.getId();
                textView1.setText("Evaluate");
                evaluationService.IsEvaluated(
                        studentID,
                        teacherId,
                        courseID,
                        sessionID,
                        isConfidential ? "Confidential" : "Student",
                        result -> {
                            if (result) {
                                Toast.makeText(getContext(), "You have already Evaluated this teacher", Toast.LENGTH_SHORT).show();
                            } else {
                                String evaluationType = isConfidential ? "Confidential" : "Student";
                                evaluationTimeService.isEvaluationTime(
                                        sessionID,
                                        evaluationType,
                                        isTime -> {
                                            if (isTime) {
                                                if (isConfidential) {
                                                    studentMainActivity.replaceFragment(new EvaluationQuestionnaireFragment(teacherId, courseID, "Confidential", fragmentContainer.getId()));
                                                } else {
                                                    studentMainActivity.replaceFragment(new EvaluationQuestionnaireFragment(teacherId, courseID, "Student", fragmentContainer.getId()));
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Evaluation not opened", Toast.LENGTH_SHORT).show();
                                            }
                                        },
                                        errorMessage -> {
                                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                        });
                            }
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return view;
    }
}
