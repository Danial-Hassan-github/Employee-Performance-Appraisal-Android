package com.example.biitemployeeperformanceappraisalsystem.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomStudentSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.ConfidentialEvaluatorStudent;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.example.biitemployeeperformanceappraisalsystem.network.services.StudentService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectStudentFragment extends Fragment {
    StudentService studentService;
    SharedPreferencesManager sharedPreferencesManager;
    EditText editTextSemester, editTextSection;
    Button btnSave;
    Spinner spinnerStudent;
    List<Student> studentList;
    List<ConfidentialEvaluatorStudent> confidentialEvaluatorStudentList;
    CustomStudentSpinnerAdapter customStudentSpinnerAdapter;
    public SelectStudentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectStudentFragment newInstance(String param1, String param2) {
        SelectStudentFragment fragment = new SelectStudentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_student, container, false);
        // initialize items
        editTextSemester = view.findViewById(R.id.edit_text_semester);
        editTextSemester = view.findViewById(R.id.edit_text_section);
        spinnerStudent = view.findViewById(R.id.spinner_student);
        btnSave = view.findViewById(R.id.btn_save);

        // initialize services
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        studentService = new StudentService(getContext());
        confidentialEvaluatorStudentList = new ArrayList<>();

        int semester = 8;
        String section = "A";
        try{
            semester = Integer.parseInt(editTextSemester.getText().toString());
            section = editTextSection.getText().toString();
        }catch (Exception ex){

        }

        studentService.getStudentsBySection(
                semester,
                section,
                students -> {
//                    studentList = new ArrayList<>();
//                    Student student = new Student();
//                    student.setName("Select All");
//                    studentList.add(student);
                    studentList = students;

                    customStudentSpinnerAdapter = new CustomStudentSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, studentList);
                    spinnerStudent.setAdapter(customStudentSpinnerAdapter);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> selectedStudentIds = customStudentSpinnerAdapter.getSelectedStudentIds();
                for (int i = 0; i < selectedStudentIds.size(); i++) {
                    ConfidentialEvaluatorStudent confidentialEvaluatorStudent = new ConfidentialEvaluatorStudent();
                    confidentialEvaluatorStudent.setStudent_id(selectedStudentIds.get(i));
                    confidentialEvaluatorStudent.setSession_id(sharedPreferencesManager.getSessionId());
                    confidentialEvaluatorStudentList.add(confidentialEvaluatorStudent);
                }

                studentService.postConfidentialEvaluatorStudents(
                        confidentialEvaluatorStudentList,
                        response -> {
                            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return view;
    }
}