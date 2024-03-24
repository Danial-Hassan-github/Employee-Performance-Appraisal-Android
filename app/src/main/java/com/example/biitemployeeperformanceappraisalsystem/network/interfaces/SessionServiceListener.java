package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SessionServiceListener {
    @GET("Session/GetSessions")
    Call<List<Session>> getSessions();
    @GET("Session/GetCurrentSession")
    Call<Session> getCurrentSession();
}
