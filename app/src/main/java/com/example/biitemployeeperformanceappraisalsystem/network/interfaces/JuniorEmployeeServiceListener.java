package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.TeacherJunior;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JuniorEmployeeServiceListener {
    @GET("JuniorEmployee/GetTeacherJuniors")
    Call<List<TeacherJunior>> getTeacherJuniors(@Query("teacherID") int teacherID, @Query("sessionID") int sessionID);
}
