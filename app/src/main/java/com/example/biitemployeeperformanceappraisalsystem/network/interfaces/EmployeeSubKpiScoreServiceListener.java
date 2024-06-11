package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeSubKpiScore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmployeeSubKpiScoreServiceListener {
    @GET("EmployeeSubKpiPerformance/GetSubKpiEmployeePerformance")
    Call<List<EmployeeSubKpiScore>> getSubKpiEmployeePerformance(@Query("employeeID") int employeeID, @Query("sessionID") int sessionID);
}
