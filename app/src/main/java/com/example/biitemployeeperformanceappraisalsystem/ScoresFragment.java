package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.director.QuestionScoreFragment;
import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionsScores;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluatorResponse;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionScore;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeQuestionScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluatorService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoresFragment extends Fragment {
    int employeeId = 0;
    int sessionId = 0;
    int evaluationTypeId = 0;
    EvaluatorResponse evaluatorResponse;
    List<Session> sessionList;
    List<Employee> employeeList;
    List<Course> courseList;
    Course course;
    List<QuestionnaireType> questionnaireTypeList;
    List<QuestionScore> employeeQuestionScoreList;
    ListView employeeScoreListView;
    Spinner sessionSpinner, employeeSpinner, questionnaireTypeSpinner, courseSpinner;
    LinearLayout employeeSpinnerLayout;
    QuestionnaireService questionnaireService;
    SessionService sessionService;
    EmployeeService employeeService;
    CourseService courseService;
    EvaluatorService evaluatorService;
    private List<String> evaluatorNames;
    private ArrayAdapter<String> adapter;
    private ListView evaluatorListView;

    public ScoresFragment() {
        // Required empty public constructor
    }

    public static ScoresFragment newInstance(String param1, String param2) {
        ScoresFragment fragment = new ScoresFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_scores, container, false);

        employeeScoreListView = view.findViewById(R.id.employee_questions_scores);
        sessionSpinner = view.findViewById(R.id.spinner_session);
        employeeSpinner = view.findViewById(R.id.spinner_employee);
        questionnaireTypeSpinner = view.findViewById(R.id.spinner_questionnaire_type);
        courseSpinner = view.findViewById(R.id.spinner_course);
        employeeSpinnerLayout = view.findViewById(R.id.employee_spinner_layout);

        evaluatorListView = view.findViewById(R.id.evaluatorListView);
        evaluatorNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, evaluatorNames);
        evaluatorListView.setAdapter(adapter);

        evaluatorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // QuestionScore selectedScore = employeeQuestionScoreList.get(position);
                Employee employee = new Employee();
                Student student = new Student();
                int evaluatorId = 0;
                if (evaluatorResponse.getEmployees().isEmpty()){
                    student = evaluatorResponse.getStudents().get(position);
                    evaluatorId = student.getId();
                }else {
                    employee = evaluatorResponse.getEmployees().get(position);
                    evaluatorId = employee.getId();
                }

                QuestionScoreFragment questionScoreFragment = new QuestionScoreFragment(evaluatorId, employeeId, sessionId, evaluationTypeId, course.getId());

//                Bundle bundle = new Bundle();
//                bundle.putString("employeeName", selectedScore.getQuestion().getQuestion());
//                bundle.putInt("employeeId", selectedScore.getEmployee().getId());
//                newFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, questionScoreFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        if (getActivity() instanceof FacultyMain || getActivity() instanceof HodMainActivity){
            employeeSpinnerLayout.setVisibility(View.GONE);
        }

        sessionService = new SessionService(view.getContext());
        employeeService = new EmployeeService(view.getContext());
        questionnaireService = new QuestionnaireService(getContext());
        courseService = new CourseService(getContext());
        evaluatorService = new EvaluatorService(getContext());

        employeeService.getEmployees(
                employees -> {
                    employeeList = employees;
                    employeeService.populateEmployeesSpinner(employees, employeeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
        sessionService.getSessions(sessions -> {
                    // Handle the list of sessions here
                    sessionList = sessions;
                    // Populate the spinner with session titles
                    sessionService.populateSpinner(sessions,sessionSpinner);
                },
                // onFailure callback
                errorMessage -> {
                    // Handle failure
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });
        questionnaireService.getQuestionnaireType(
                questionnaireTypes -> {
                    questionnaireTypeList = questionnaireTypes;
                    questionnaireService.populateSpinner(questionnaireTypeList, questionnaireTypeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        courseService.getCourses(
                courses -> {
                    courseList = courses;
                    course = courseList.get(courseList.size()-1);
                    List<String> coursesName = new ArrayList<>();
                    for (Course c: courseList) {
                        coursesName.add(c.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, coursesName);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courseSpinner.setAdapter(adapter);
                },
                errorMessage -> {

                }
        );

        // Add OnItemSelectedListener to sessionSpinner
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSpinnerSelectionChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

        // Add OnItemSelectedListener to employeeSpinner
        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSpinnerSelectionChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = courseList.get(position);
                handleSpinnerSelectionChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        questionnaireTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSpinnerSelectionChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void handleSpinnerSelectionChange() {
        try {
            if (sessionSpinner != null && questionnaireTypeSpinner != null) {
                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getContext());
                // Retrieve the selected session and employee IDs
                sessionId = sessionList.get(sessionSpinner.getSelectedItemPosition()).getId();
                // int courseId = courseList.get(courseSpinner.getSelectedItemPosition()).getId();
                if (getActivity() instanceof DirectorMainActivity){
                    employeeId = employeeList.get(employeeSpinner.getSelectedItemPosition()).getId();
                }else {
                    employeeId = sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId();
                }

                // Set the evaluation type ID (you might want to change this according to your logic)
                evaluationTypeId = questionnaireTypeList.get(questionnaireTypeSpinner.getSelectedItemPosition()).getId();

                evaluatorService.getEmployeeEvaluators(
                        employeeId,
                        evaluationTypeId,
                        sessionId,
                        course.getId(),
                        response -> {
                            evaluatorResponse = response;
                            if (evaluatorResponse.getStudents() != null && !evaluatorResponse.getStudents().isEmpty()) {
                                for (Student student : evaluatorResponse.getStudents()) {
                                    evaluatorNames.add(student.getName());
                                }
                            } else if (evaluatorResponse.getEmployees() != null && !evaluatorResponse.getEmployees().isEmpty()) {
                                for (Employee employee : evaluatorResponse.getEmployees()) {
                                    evaluatorNames.add(employee.getName());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });

                // Call the service to get employee question scores based on the selected session, employee, and evaluation type
//                EmployeeQuestionScoreService employeeQuestionScoreService = new EmployeeQuestionScoreService(getContext());
//                employeeQuestionScoreService.getEmployeeQuestionScore(
//                        employeeId,
//                        sessionId,
//                        evaluationTypeId,
//                        employeeQuestionScores -> {
//                            // Handle the retrieved employee question scores
//                            employeeQuestionScoreList = employeeQuestionScores;
//                            String[] scoresData = new String[employeeQuestionScoreList.size()];
//
//                            // Populate the array with the data from employeeScoreList
//                            for (int i = 0; i < employeeQuestionScoreList.size(); i++) {
//                                QuestionScore score = employeeQuestionScoreList.get(i);
//                                // Assuming you have some method to format EmployeeQuestionScore to String
//                                String scoreString = score != null ? score.getQuestion().getQuestion().toString() + "\n" + Math.round(score.getAverage())+"%" : ""; // Handle null case
//                                scoresData[i] = scoreString;
//                            }
//                            // Set the adapter for the ListView
//                            employeeScoreListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, scoresData));
//                        },
//                        errorMessage -> {
//                            // Handle the error message
//                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                        });
            } else {
                // Handle the case where either spinner is null
                Toast.makeText(getContext(), "Spinners are not initialized", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Log.e("","",ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}