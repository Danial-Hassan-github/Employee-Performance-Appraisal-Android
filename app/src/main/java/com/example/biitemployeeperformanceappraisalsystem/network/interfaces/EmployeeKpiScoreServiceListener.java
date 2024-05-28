package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmployeeKpiScoreServiceListener {
    @GET("EmployeeKpiPerformance/GetKpiEmployeePerformance")
    Call<List<EmployeeKpiScore>> getEmployeeKpiScore(@Query("employeeID") int employeeID, @Query("sessionID") int sessionID);
    @GET("EmployeeKpiPerformance/CompareKpiEmployeePerformance")
    Call<List<List<EmployeeKpiScore>>> compareEmployeeKpiScore(@Query("employeeID1") int employeeID1, @Query("employeeID2") int employeeID2, @Query("sessionID") int sessionID);
    @GET("EmployeeKpiPerformance/GetKpiEmployeePerformanceMultiSession")
    Call<List<EmployeeKpiScoreMultiSession>> getEmployeeKpiScoreMultiSession(@Query("employeeID") int employeeID, @Query("startingSessionID") int startingSessionID, @Query("endingSessionID") int endingSessionID);
}
