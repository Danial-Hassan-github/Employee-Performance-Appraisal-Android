package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCourseScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCoursesPerformanceRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.MultiEmployeeCoursePerformanceRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EmployeeCoursePerformanceServiceListener {
    @GET("EmployeeCoursePerformance/GetEmployeeCoursePerformance")
    Call<EmployeeCourseScore> getEmployeeCoursePerformance(@Query("employeeID") int employeeID, @Query("sessionID") int sessionID, @Query("courseID") int courseID);
    @GET("EmployeeCoursePerformance/CompareEmployeeCoursePerformance")
    Call<List<EmployeeCourseScore>> compareEmployeeCoursePerformance(@Query("employeeID1") int employeeID1, @Query("employeeID2") int employeeID2, @Query("sessionID") int sessionID, @Query("courseID") int courseID);
    @GET("EmployeeCoursePerformance/GetEmployeeCoursesPerformance")
    Call<List<EmployeeCourseScore>> getEmployeeCoursesPerformance(@Query("teacherID") int teacherID, @Query("sessionID") int sessionID);
    @POST("EmployeeCoursePerformance/GetMultiEmployeeCoursePerformance")
    Call<List<EmployeeCourseScore>> getMultiEmployeeCoursePerformance(@Body MultiEmployeeCoursePerformanceRequest multiEmployeeCoursePerformanceRequest);
}
