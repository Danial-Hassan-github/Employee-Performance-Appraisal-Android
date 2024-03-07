package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginServiceListener {
    @GET("Login/Login")
    Call<Student> LoginStudent(@Query("emailOrAridNo") String emailOrAridNo, @Query("password") String password);

    @GET("Login/Login")
    Call<EmployeeDetails> LoginEmployee(@Query("emailOrAridNo") String emailOrAridNo, @Query("password") String password);
}
