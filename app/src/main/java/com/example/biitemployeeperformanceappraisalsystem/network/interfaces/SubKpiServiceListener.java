package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SubKpiServiceListener {
    @GET("SubKpi/GetSubKPIs")
    Call<List<SubKpi>> getSubKPIs(@Query("sessionID") int sessionID);
    @GET("SubKpi/GetSubKPIsOfKpi")
    Call<List<SubKpi>> getSubKPIsOfKpi(@Query("kpi_id") int kpi_id, @Query("sessionID") int sessionID);
}
