package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface EmployeeServiceListener {
    @GET("Employee/GetEmployees")
    Call<List<Employee>> getEmployees();

    @GET("Employee/GetEmployeesWithDetails")
    Call<List<EmployeeDetails>> getEmployeesWithDetails();

    @GET("Employee/GetEmployeesWithKpiScores")
    Call<List<EmployeeDetailsScore>> getEmployeesWithKpiScores(@Query("sessionID") int sessionID);

    @GET("Employee/GetEmployeeTypes")
    Call<List<EmployeeType>> getEmployeeTypes();
    @POST("Employee/PostEmployee")
    Call<Employee> postEmployee(@Body Employee employee);
    @PUT("Employee/PutEmployee")
    Call<Employee> putEmployee(@Body Employee employee);
    @DELETE("Employee/DeleteEmployee")
    Call<Employee> deleteEmployee(@Query("id") int id);
}
