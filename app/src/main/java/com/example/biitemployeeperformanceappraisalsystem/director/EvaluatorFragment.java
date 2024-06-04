package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluatorEvaluatess;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluatorService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluatorFragment extends Fragment {
    SharedPreferencesManager sharedPreferencesManager;
    EvaluatorService evaluatorService;
    CustomSpinnerAdapter customSpinnerAdapter;
    List<Employee> employeeList;
    int sessionID, evaluatorID;
    Spinner evaluatorSpinner, evaluateeSpinner;
    Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluator, container, false);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        sessionID = sharedPreferencesManager.getSessionId();
        evaluatorSpinner = view.findViewById(R.id.spinner_evaluator);
        evaluateeSpinner = view.findViewById(R.id.spinner_evaluatee);
        btnSave = view.findViewById(R.id.evaluator_save_button);

        evaluatorService = new EvaluatorService(getContext());

        EmployeeService employeeService = new EmployeeService(getContext());
        employeeService.getEmployees(
                employees -> {
                    employeeList = employees;
                    employeeService.populateEmployeesSpinner(employeeList, evaluatorSpinner);
                    updateEvaluateeSpinnerContents(0); // Initially update evaluatee spinner based on the first evaluator
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        // Set an OnItemSelectedListener on the evaluatorSpinner
        evaluatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update the evaluatee spinner contents when an item is selected in the evaluator spinner
                updateEvaluateeSpinnerContents(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluatorID = employeeList.get(evaluatorSpinner.getSelectedItemPosition()).getId();
                sessionID = sharedPreferencesManager.getSessionId();
                EvaluatorEvaluatess evaluatorEvaluatess = new EvaluatorEvaluatess();
                evaluatorEvaluatess.setEvaluator_id(evaluatorID);
                evaluatorEvaluatess.setSession_id(sessionID);
                evaluatorEvaluatess.setEvaluatee_ids(customSpinnerAdapter.getSelectedEmployeeIds());
                evaluatorService.postEvaluator(
                        evaluatorEvaluatess,
                        evaluatorEvaluateesList -> {
                            // TODO:
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
            }
        });
        return view;
    }

    private void updateEvaluateeSpinnerContents(int evaluatorPosition) {
        // Get the ID of the selected evaluator
        int evaluatorId = employeeList.get(evaluatorPosition).getId();

        // Create a new list to hold the updated items for the evaluatee spinner
        List<Employee> updatedEvaluateeList = new ArrayList<>();

        // Add the default "Select All" item to the list
        // updatedEvaluateeList.add("Select All");
        // Add the default "Select All" item to the list
        Employee employee = new Employee();
        employee.setName("Select All");
        updatedEvaluateeList.add(employee);

        // Add all employees to the list initially
        updatedEvaluateeList.addAll(getUpdatedEmployeeList(evaluatorId));

        // Update the evaluatee spinner with the updated list
        customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, updatedEvaluateeList);
        evaluateeSpinner.setAdapter(customSpinnerAdapter);
    }

    private List<Employee> getUpdatedEmployeeList(int evaluatorId) {
        List<Employee> updatedEmployeeList = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getId() != evaluatorId) {
                updatedEmployeeList.add(employee);
            }
        }
        return updatedEmployeeList;
    }
}
