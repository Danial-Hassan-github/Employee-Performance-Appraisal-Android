package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.ApiRequestModels.EmployeeIdsWithSession;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EmployeeKpiScoreServiceListener {
    @GET("EmployeeKpiPerformance/GetKpiEmployeePerformance")
    Call<List<KpiScore>> getEmployeeKpiScore(@Query("employeeID") int employeeID, @Query("sessionID") int sessionID);
    @GET("EmployeeKpiPerformance/CompareSingleKpiEmployeePerformance")
    Call<List<EmployeeKpiScore>> compareEmployeeSingleKpiScore(@Query("employeeIDs") List<Integer> employeeIDs, @Query("session_id") int session_id, @Query("kpi_id") int kpi_id);
    @GET("EmployeeKpiPerformance/CompareMultiSessionKpiEmployeePerformance")
    Call<List<KpiScore>> compareMultiSessionKpiEmployeePerformance(@Query("sessionIDs") List<Integer> sessionIDs, @Query("employeeId") int employeeId, @Query("kpi_id") int kpi_id);
    @GET("EmployeeKpiPerformance/CompareKpiEmployeePerformanceYearly")
    Call<List<EmployeeKpiScore>> compareYearlyKpiEmployeePerformance(@Query("employeeIDs") List<Integer> employeeIDs, @Query("year") String year, @Query("kpi_id") int kpi_id);
    @GET("EmployeeKpiPerformance/CompareMultiSessionAllKpiEmployeePerformance")
    Call<List<EmployeeKpiScore>> compareMultiSessionAllKpiEmployeePerformance(@Query("sessionIDs") List<Integer> sessionIDs, @Query("employeeId") int employeeId);
    @POST("EmployeeKpiPerformance/CompareKpiEmployeePerformance")
    Call<List<EmployeeKpiScore>> compareEmployeeKpiScore(@Body EmployeeIdsWithSession employeeIdsWithSession);
    @GET("EmployeeKpiPerformance/GetKpiEmployeePerformanceMultiSession")
    Call<List<EmployeeKpiScoreMultiSession>> getEmployeeKpiScoreMultiSession(@Query("employeeID") int employeeID, @Query("startingSessionID") int startingSessionID, @Query("endingSessionID") int endingSessionID);
}
