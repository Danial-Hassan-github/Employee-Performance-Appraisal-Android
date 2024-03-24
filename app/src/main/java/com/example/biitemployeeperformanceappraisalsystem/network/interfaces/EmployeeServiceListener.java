package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EmployeeServiceListener {
    @GET("Employee/GetEmployees")
    Call<List<Employee>> getEmployees();

    @GET("Employee/GetEmployeesWithDetails")
    Call<List<EmployeeDetails>> getEmployeesWithDetails();

    @GET("Employee/GetEmployeesWithKpiScores")
    Call<List<EmployeeDetailsScore>> getEmployeesWithKpiScores();

    @GET("Employee/GetEmployeeTypes")
    Call<List<EmployeeType>> getEmployeeTypes();
}
