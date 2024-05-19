package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.SubKpiListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.Department;
import com.example.biitemployeeperformanceappraisalsystem.models.Designation;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DepartmentService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DesignationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SubKpiService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGroupKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGroupKpiFragment extends Fragment {
    SubKpiService subKpiService;
    List<SubKpi> subKpiList;
    List<SubKpi> subKpiAdapterList;
    SubKpiListAdapter subKpiListAdapter;
    ListView subKpiListView;
    //List<Session> sessionList;
    List<Designation> designationList;
    List<Department> departmentList;
    List<EmployeeType> employeeTypeList;
    Spinner designationSpinner,departmentSpinner,employeeTypeSpinner,subKpiSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_group_kpi, container, false);
        // sessionSpinner = view.findViewById(R.id.spinner_session);
        designationSpinner=view.findViewById(R.id.spinner_designation);
        departmentSpinner=view.findViewById(R.id.spinner_department);
        employeeTypeSpinner=view.findViewById(R.id.spinner_employee_type);
        subKpiSpinner = view.findViewById(R.id.spinner_sub_kpi);
        subKpiListView = view.findViewById(R.id.list_view_subKpi);

        DesignationService designationService=new DesignationService(view.getContext());
        DepartmentService departmentService=new DepartmentService(view.getContext());
        SessionService sessionService=new SessionService(getContext());
        EmployeeService employeeService=new EmployeeService(getContext());
        subKpiService = new SubKpiService(getContext());

        subKpiList = new ArrayList<>();
        subKpiAdapterList = new ArrayList<>();

        subKpiService.getSubKPIs(
                10,
                subKpiList1 -> {
                    if (subKpiList1 != null) {
                        subKpiList = subKpiList1;
                        String subKpiTitles[] = subKpiService.getSubKpiTitles(subKpiList);
                        if (subKpiTitles != null) {
                            subKpiService.populateSpinner(subKpiList,subKpiSpinner);
                        } else {
                            Toast.makeText(getContext(), "No SubKPI titles available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch SubKPIs", Toast.LENGTH_SHORT).show();
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        // Initialize the subKpiList and the adapter
        subKpiListAdapter = new SubKpiListAdapter(getContext(), R.layout.sub_kpi_list_item_view, subKpiAdapterList);
        subKpiListView.setAdapter(subKpiListAdapter);

        // Set the spinner item selected listener
        subKpiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subKpiAdapterList.add(subKpiList.get(position));
                // Notify the adapter to refresh the ListView
                subKpiListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
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

        CommonData data = new CommonData(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.getSubKPITypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subKpiSpinner.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}