package com.example.biitemployeeperformanceappraisalsystem.network;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiNetwork {
    @GET("Login/Login")
    Call<Student> LoginStudent(@Query("emailOrAridNo") String emailOrAridNo, @Query("password") String password);
    @GET("Login/Login")
    Call<EmployeeDetails> LoginEmployee(@Query("emailOrAridNo") String emailOrAridNo, @Query("password") String password);
    @GET("Session/GetSessions")
    Call<List<Session>> GetSession();
}
