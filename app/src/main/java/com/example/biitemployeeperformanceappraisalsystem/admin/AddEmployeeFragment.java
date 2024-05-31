package com.example.biitemployeeperformanceappraisalsystem.admin;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Department;
import com.example.biitemployeeperformanceappraisalsystem.models.Designation;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DepartmentService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DesignationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEmployeeFragment extends Fragment {
    int departmentPosition, designationPosition, employeeTypePosition;
    EditText editTextEmployeeName, editTextEmployeeEmail, editTextEmployeePassword, editTextEmployeeSalary;
    Spinner employeeTypeSpinner, designationSpinner, departmentSpinner;
    EmployeeDetails employeeDetails;
    Employee employee;
    List<Designation> designationList;
    List<Department> departmentList;
    List<EmployeeType> employeeTypeList;
    EmployeeService employeeService;
    AppCompatButton btnSave;

    private boolean isEmployeeTypeLoaded = false;
    private boolean isDesignationLoaded = false;
    private boolean isDepartmentLoaded = false;

    public AddEmployeeFragment() {

    }

    public AddEmployeeFragment(EmployeeDetails employeeDetails) {
        this.employee = employeeDetails.getEmployee();
        this.employeeDetails = employeeDetails;
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
        View view = inflater.inflate(R.layout.fragment_add_employee, container, false);

        employeeService = new EmployeeService(getContext());

        editTextEmployeeEmail = view.findViewById(R.id.edit_text_employee_email);
        editTextEmployeeName = view.findViewById(R.id.edit_text_employee_name);
        editTextEmployeePassword = view.findViewById(R.id.edit_text_employee_password);
        editTextEmployeeSalary = view.findViewById(R.id.edit_text_employee_salary);

        btnSave = view.findViewById(R.id.btn_save_employee);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (employee != null) {
                        employee.setName(editTextEmployeeName.getText().toString());
                        employee.setEmail(editTextEmployeeEmail.getText().toString());
                        employee.setPassword(editTextEmployeePassword.getText().toString());
                        employee.setSalary(Double.parseDouble(editTextEmployeeSalary.getText().toString()));
                        employee.setDeleted(false);
                        employee.setDesignationId(designationList.get(designationSpinner.getSelectedItemPosition()).getId());
                        employee.setDepartmentId(departmentList.get(departmentSpinner.getSelectedItemPosition()).getId());
                        employee.setEmployeeTypeId(employeeTypeList.get(employeeTypeSpinner.getSelectedItemPosition()).getId());
                        employeeService.putEmployee(
                                employee,
                                employee1 -> {

                                },
                                errorMessage -> {
                                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                });
                    }
                    else {
                        employee = new Employee();
                        employee.setName(editTextEmployeeName.getText().toString());
                        employee.setEmail(editTextEmployeeEmail.getText().toString());
                        employee.setPassword(editTextEmployeePassword.getText().toString());
                        employee.setSalary(Double.parseDouble(editTextEmployeeSalary.getText().toString()));
                        employee.setDeleted(false);
                        employee.setDesignationId(designationList.get(designationSpinner.getSelectedItemPosition()).getId());
                        employee.setDepartmentId(departmentList.get(departmentSpinner.getSelectedItemPosition()).getId());
                        employee.setEmployeeTypeId(employeeTypeList.get(employeeTypeSpinner.getSelectedItemPosition()).getId());
                        employeeService.postEmployee(
                                employee,
                                employee1 -> {
                                },
                                errorMessage -> {
                                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                });
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        employeeTypeSpinner = view.findViewById(R.id.spinner_employee_type);
        designationSpinner = view.findViewById(R.id.spinner_designation);
        departmentSpinner = view.findViewById(R.id.spinner_department);

        DesignationService designationService = new DesignationService(view.getContext());
        DepartmentService departmentService = new DepartmentService(view.getContext());
        SessionService sessionService = new SessionService(getContext());
        EmployeeService employeeService = new EmployeeService(getContext());

        designationService.getDesignations(
                designations -> {
                    designationList = designations;
                    designationService.populateDesignationSpinner(designations, designationSpinner);
                    isDesignationLoaded = true;
                    checkAndLoadEmployeeDetails();
                }, errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        departmentService.getDepartments(
                departments -> {
                    departmentList = departments;
                    departmentService.populateDepartmentSpinner(departments, departmentSpinner);
                    isDepartmentLoaded = true;
                    checkAndLoadEmployeeDetails();
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        employeeService.getEmployeeTypes(
                employeeTypes -> {
                    employeeTypeList = employeeTypes;
                    employeeService.populateEmployeeTypeSpinner(employeeTypes, employeeTypeSpinner);
                    isEmployeeTypeLoaded = true;
                    checkAndLoadEmployeeDetails();
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        designationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designationPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        employeeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employeeTypePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void checkAndLoadEmployeeDetails() {
        if (isEmployeeTypeLoaded && isDesignationLoaded && isDepartmentLoaded) {
            if (employeeDetails != null) {
                // employee = employeeDetails.getEmployee();
                editTextEmployeeName.setText(employee.getName());
                editTextEmployeeEmail.setText(employee.getEmail());
                editTextEmployeePassword.setText(employee.getPassword());
                editTextEmployeeSalary.setText(employee.getSalary().toString());
                departmentPosition = departmentList.indexOf(employeeDetails.getDepartment());
                designationPosition = designationList.indexOf(employeeDetails.getDesignation());
                employeeTypePosition = employeeTypeList.indexOf(employeeDetails.getEmployeeType());
                employeeTypeSpinner.setSelection(employeeTypePosition);
                departmentSpinner.setSelection(departmentPosition);
                designationSpinner.setSelection(designationPosition);
            }
        }
    }
}
