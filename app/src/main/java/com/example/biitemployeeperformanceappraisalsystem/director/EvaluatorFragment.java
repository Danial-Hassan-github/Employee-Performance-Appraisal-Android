package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluatorEvaluatess;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluatorService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

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
    Spinner evaluatorSpinner,evaluateeSpinner;
    Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_evaluator, container, false);
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
                    employeeService.populateEmployeesSpinner(employeeList,evaluatorSpinner);
                    List<String> evaluateeList=new ArrayList<>();
                    evaluateeList.add("Select All");
                    evaluateeList.addAll(employeeService.getEmployeeNames(employeeList));
                    customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, evaluateeList);

                    evaluateeSpinner.setAdapter(customSpinnerAdapter);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, employeeService.getEmployeeNames(employeeList));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    evaluatorSpinner.setAdapter(adapter);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

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
}