package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
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
    List<KPI> kpiList;
    List<Employee> employeeList;
    EmployeeService employeeService;
    KpiService kpiService;
    Spinner employeeSpinner,kpiSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_individual_kpi, container, false);
        employeeSpinner = view.findViewById(R.id.spinner_employee);
        kpiSpinner = view.findViewById(R.id.spinner_kpi);

        kpiService = new KpiService(getContext());
        employeeService = new EmployeeService(getContext());

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

        return view;
    }
}