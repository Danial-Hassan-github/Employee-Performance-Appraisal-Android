package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EmployeeServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeService {
    EmployeeServiceListener employeeServiceListener;
    Context context;
    public EmployeeService(Context context){
        employeeServiceListener= RetrofitClient.getRetrofitInstance().create(employeeServiceListener.getClass());
        this.context=context;
    }
    public void getEmployees(final Consumer<List<EmployeeDetails>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeDetails>> employees = employeeServiceListener.getEmployees();
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

    public void GetEmployeesWithKpiScores(final Consumer<List<EmployeeDetailsScore>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeDetailsScore>> employeeDetailsScoreList = employeeServiceListener.GetEmployeesWithKpiScores();
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
}
