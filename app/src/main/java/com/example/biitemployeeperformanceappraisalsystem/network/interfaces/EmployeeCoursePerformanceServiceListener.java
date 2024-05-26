package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCourseScore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmployeeCoursePerformanceServiceListener {
    @GET("EmployeeCoursePerformance/GetEmployeeCoursePerformance")
    Call<EmployeeCourseScore> getEmployeeCoursePerformance(@Query("employeeID") int employeeID, @Query("sessionID") int sessionID, @Query("courseID") int courseID);
    @GET("EmployeeCoursePerformance/CompareEmployeeCoursePerformance")
    Call<List<EmployeeCourseScore>> compareEmployeeCoursePerformance(@Query("employeeID1") int employeeID1, @Query("employeeID2") int employeeID2, @Query("sessionID") int sessionID, @Query("courseID") int courseID);

}
