package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.KpiService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIndividualKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIndividualKpiFragment extends Fragment {
    KPI kpi;
    Employee employee;
    SharedPreferencesManager sharedPreferencesManager;
    List<KPI> kpiList;
    List<Employee> employeeList;
    EmployeeService employeeService;
    KpiService kpiService;
    Spinner employeeSpinner,kpiSpinner;
    Button btnSave;
    EditText editTextWeightage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_individual_kpi, container, false);
        employeeSpinner = view.findViewById(R.id.spinner_employee);
        kpiSpinner = view.findViewById(R.id.spinner_kpi);
        btnSave = view.findViewById(R.id.btn_save_kpi);
        editTextWeightage = view.findViewById(R.id.text_weightage);

        kpiService = new KpiService(getContext());
        employeeService = new EmployeeService(getContext());
        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        kpiService.getKpis(
                kpis -> {
                    kpiList = kpis;
                    kpiService.populateSpinner(kpiList, kpiSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        employeeService.getEmployees(
                employeeList1 -> {
                    employeeList = employeeList1;
                    employeeService.populateEmployeesSpinner(employeeList, employeeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    employee = employeeList.get(position);
                }catch (Exception e){
                    Toast.makeText(getContext(), "fetching employee...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kpiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    kpi = kpiList.get(position);
                }catch (Exception e){
                    Toast.makeText(getContext(), "fetching kpi's...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeKpi employeeKpi = new EmployeeKpi();
                employeeKpi.setKpi(kpi);
                employeeKpi.setEmployee(employee);
                int weightage = 0;
                try {
                    weightage = Integer.parseInt(editTextWeightage.getText().toString());
                }catch (Exception e){
                    Toast.makeText(getContext(), "Try entering correct weightage value in numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
                employeeKpi.setWeightage(weightage);
                employeeKpi.setSession_id(sharedPreferencesManager.getSessionId());
                kpiService.postEmployeeKpi(
                        employeeKpi,
                        k -> {
                            Toast.makeText(getContext(), "Kpi Added Successfully", Toast.LENGTH_SHORT).show();
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return view;
    }
}