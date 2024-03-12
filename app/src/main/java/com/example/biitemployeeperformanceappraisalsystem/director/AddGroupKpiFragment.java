package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Department;
import com.example.biitemployeeperformanceappraisalsystem.models.Designation;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DepartmentService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DesignationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGroupKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGroupKpiFragment extends Fragment {
    List<Session> sessionList;
    List<Designation> designationList;
    List<Department> departmentList;
    List<EmployeeType> employeeTypeList;
    Spinner sessionSpinner,designationSpinner,departmentSpinner,employeeTypeSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_group_kpi, container, false);
        sessionSpinner = view.findViewById(R.id.spinner_session);
        designationSpinner=view.findViewById(R.id.spinner_designation);
        departmentSpinner=view.findViewById(R.id.spinner_department);
        employeeTypeSpinner=view.findViewById(R.id.spinner_employee_type);

        DesignationService designationService=new DesignationService(view.getContext());
        DepartmentService departmentService=new DepartmentService(view.getContext());
        SessionService sessionService=new SessionService(getContext());
        EmployeeService employeeService=new EmployeeService(getContext());

        sessionService.getSessions(sessions -> {
                    sessionList = sessions;
                    sessionService.populateSpinner(sessions,sessionSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });


        designationService.getDesignations(
                designations -> {
                    designationList=designations;
                    designationService.populateDesignationSpinner(designations,designationSpinner);
                },errorMessage ->{
                    Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT);
                }
        );

        departmentService.getDepartments(
                departments -> {
                    departmentList=departments;
                    departmentService.populateDepartmentSpinner(departments,departmentSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT);
                }
        );

        employeeService.getEmployeeTypes(
                employeeTypes -> {
                    employeeTypeList=employeeTypes;
                    employeeService.populateEmployeeTypeSpinner(employeeTypes,employeeTypeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT);
                }
        );

        // Inflate the layout for this fragment
        return view;
    }
}