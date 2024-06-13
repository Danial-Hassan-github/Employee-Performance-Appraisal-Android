package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeSubKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpiScore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmployeeSubKpiScoreServiceListener {
    @GET("EmployeeSubKpiPerformance/GetSubKpiEmployeePerformance")
    Call<List<SubKpiScore>> getSubKpiEmployeePerformance(@Query("employeeID") int employeeID, @Query("sessionID") int sessionID);
    @GET("EmployeeSubKpiPerformance/GetSubKpiMultiEmployeePerformance")
    Call<List<EmployeeSubKpiScore>> getSubKpiMultiEmployeePerformance(@Query("employeeIDs") List<Integer> employeeIDs, @Query("sessionID") int sessionID);
}
