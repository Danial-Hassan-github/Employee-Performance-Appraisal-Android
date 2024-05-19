package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KpiServiceListener {
    @GET("Kpi/GetSessionKpis")
    Call<List<GroupKpiDetails>> getSessionKpis(@Query("sessionID") int sessionID);
    @GET("KPI/GetKpiGroup")
    Call<List<KPI>> getKpiGroup(@Query("groupID") int groupID);
}
