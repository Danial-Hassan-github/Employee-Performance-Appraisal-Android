package com.example.biitemployeeperformanceappraisalsystem.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEmployeeFragment extends Fragment {
    Spinner employeeTypeSpinner,designationSpinner,departmentSpinner;
    Employee employee;
    public AddEmployeeFragment() {

    }
    public AddEmployeeFragment(Employee employee) {
        this.employee = employee;
    }

    public static AddEmployeeFragment newInstance(String param1, String param2) {
        AddEmployeeFragment fragment = new AddEmployeeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_employee, container, false);

        employeeTypeSpinner = view.findViewById(R.id.spinner_employee_type);
        designationSpinner = view.findViewById(R.id.spinner_designation);
        departmentSpinner = view.findViewById(R.id.spinner_department);

        CommonData data=new CommonData(getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateEmployeeTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeTypeSpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateDepartments());
        departmentSpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateDesignations());
        designationSpinner.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}