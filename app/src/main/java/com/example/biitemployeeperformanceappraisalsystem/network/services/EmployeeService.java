package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.Designation;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EmployeeServiceListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeService {
    EmployeeServiceListener employeeServiceListener;
    Context context;
    public EmployeeService(Context context){
        employeeServiceListener = RetrofitClient.getRetrofitInstance().create(EmployeeServiceListener.class);
        this.context=context;
    }

    public void getEmployees(final Consumer<List<Employee>> onSuccess, final Consumer<String> onFailure) {
        Call<List<Employee>> employees = employeeServiceListener.getEmployees();
        employees.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void getEmployeesWithDetails(final Consumer<List<EmployeeDetails>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeDetails>> employees = employeeServiceListener.getEmployeesWithDetails();
        employees.enqueue(new Callback<List<EmployeeDetails>>() {
            @Override
            public void onResponse(Call<List<EmployeeDetails>> call, Response<List<EmployeeDetails>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeDetails>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void getEmployeesWithKpiScores(final Consumer<List<EmployeeDetailsScore>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeDetailsScore>> employeeDetailsScoreList = employeeServiceListener.getEmployeesWithKpiScores();
        employeeDetailsScoreList.enqueue(new Callback<List<EmployeeDetailsScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeDetailsScore>> call, Response<List<EmployeeDetailsScore>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeDetailsScore>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void getEmployeeTypes(final Consumer<List<EmployeeType>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeType>> employeeTypes = employeeServiceListener.getEmployeeTypes();
        employeeTypes.enqueue(new Callback<List<EmployeeType>>() {
            @Override
            public void onResponse(Call<List<EmployeeType>> call, Response<List<EmployeeType>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeType>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employee types");
            }
        });
    }

    public void populateEmployeesSpinner(List<Employee> employeeList, Spinner spinner) {
        if (employeeList != null && !employeeList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getEmployeeNames(employeeList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Employee list is empty", Toast.LENGTH_LONG).show();
        }
    }

    public List<String> getEmployeeNames(List<Employee> employeeList) {
        List<String> names = new ArrayList<>(employeeList.size());
        for (int i = 0; i < employeeList.size(); i++) {
            names.add(employeeList.get(i).getName());
        }
        return names;
    }

    public void populateEmployeeTypeSpinner(List<EmployeeType> employeeTypeList, Spinner spinner) {
        if (employeeTypeList != null && !employeeTypeList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getEmployeeTypeTitles(employeeTypeList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Department list is empty", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getEmployeeTypeTitles(List<EmployeeType> employeeTypeList) {
        String[] titles = new String[employeeTypeList.size()];
        for (int i = 0; i < employeeTypeList.size(); i++) {
            titles[i] = employeeTypeList.get(i).getTitle();
        }
        return titles;
    }
}
