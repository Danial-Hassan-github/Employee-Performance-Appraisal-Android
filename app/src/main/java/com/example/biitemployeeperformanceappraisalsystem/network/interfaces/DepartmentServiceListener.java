package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Department;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DepartmentServiceListener {
    @GET("Department/GetDepartments")
    Call<List<Department>> getDepartments();
}
