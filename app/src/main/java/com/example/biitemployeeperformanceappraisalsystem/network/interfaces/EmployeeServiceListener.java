package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EmployeeServiceListener {
    @GET("Employee/GetEmployees")
    Call<List<EmployeeDetails>> getEmployees();

    @GET("Employee/GetEmployeesWithKpiScores")
    Call<List<EmployeeDetailsScore>> GetEmployeesWithKpiScores();
}
