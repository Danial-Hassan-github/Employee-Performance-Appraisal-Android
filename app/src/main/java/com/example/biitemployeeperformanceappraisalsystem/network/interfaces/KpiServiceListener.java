package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KpiServiceListener {
    @GET("Kpi/GetSessionKpis")
    Call<List<GroupKpiDetails>> getSessionKpis(@Query("sessionID") int sessionID);
}
