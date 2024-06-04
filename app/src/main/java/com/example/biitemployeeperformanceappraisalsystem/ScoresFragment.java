package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionScore;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeQuestionScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoresFragment extends Fragment {
    List<Session> sessionList;
    List<Employee> employeeList;
    List<QuestionnaireType> questionnaireTypeList;
    List<EmployeeQuestionScore> employeeQuestionScoreList;
    ListView employeeScoreListView;
    Spinner sessionSpinner, employeeSpinner, questionnaireTypeSpinner;
    LinearLayout employeeSpinnerLayout;
    QuestionnaireService questionnaireService;
    SessionService sessionService;
    EmployeeService employeeService;

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
        employeeSpinnerLayout = view.findViewById(R.id.employee_spinner_layout);

        if (getActivity() instanceof FacultyMain || getActivity() instanceof HodMainActivity){
            employeeSpinnerLayout.setVisibility(View.GONE);
        }

        sessionService = new SessionService(view.getContext());
        employeeService = new EmployeeService(view.getContext());
        questionnaireService = new QuestionnaireService(getContext());

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
            if (employeeSpinner.getAdapter().isEmpty() && sessionSpinner.getAdapter().isEmpty() && questionnaireTypeSpinner.getAdapter().isEmpty()) {
                return;
            }
            if (employeeSpinner != null && sessionSpinner != null && questionnaireTypeSpinner != null) {
                // Retrieve the selected session and employee IDs
                int sessionId = sessionList.get(sessionSpinner.getSelectedItemPosition()).getId();
                int employeeId = employeeList.get(employeeSpinner.getSelectedItemPosition()).getId();

                // Set the evaluation type ID (you might want to change this according to your logic)
                int evaluationTypeId = questionnaireTypeList.get(questionnaireTypeSpinner.getSelectedItemPosition()).getId();

                // Call the service to get employee question scores based on the selected session, employee, and evaluation type
                EmployeeQuestionScoreService employeeQuestionScoreService = new EmployeeQuestionScoreService(getContext());
                employeeQuestionScoreService.getEmployeeQuestionScore(
                        employeeId,
                        sessionId,
                        evaluationTypeId,
                        employeeQuestionScores -> {
                            // Handle the retrieved employee question scores
                            employeeQuestionScoreList = employeeQuestionScores;
                            String[] scoresData = new String[employeeQuestionScoreList.size()];

                            // Populate the array with the data from employeeScoreList
                            for (int i = 0; i < employeeQuestionScoreList.size(); i++) {
                                EmployeeQuestionScore score = employeeQuestionScoreList.get(i);
                                // Assuming you have some method to format EmployeeQuestionScore to String
                                String scoreString = score != null ? score.getQuestion().getQuestion().toString() + "\n" + score.getObtainedScore() + "/" + score.getTotalScore() : ""; // Handle null case
                                scoresData[i] = scoreString;
                            }
                            // Set the adapter for the ListView
                            employeeScoreListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, scoresData));
                        },
                        errorMessage -> {
                            // Handle the error message
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Handle the case where either spinner is null
                Toast.makeText(getContext(), "Spinners are not initialized", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}