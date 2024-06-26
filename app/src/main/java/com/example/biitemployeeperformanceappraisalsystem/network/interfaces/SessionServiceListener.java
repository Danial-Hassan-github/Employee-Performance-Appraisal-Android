package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SessionServiceListener {
    @GET("Session/GetSessions")
    Call<List<Session>> getSessions();
    @GET("Session/GetYears")
    Call<List<String>> getYears();
    @GET("Session/GetCurrentSession")
    Call<Session> getCurrentSession();
    @POST("Session/PostSession")
    Call<Session> postSession(@Body Session session);
}
