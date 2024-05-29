package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWithSubKpiWeightages;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KpiServiceListener {
    @GET("KPI/GetKPIs")
    Call<List<KPI>> getKpis();
    @GET("Kpi/GetSessionKpis")
    Call<List<GroupKpiDetails>> getSessionKpis(@Query("sessionID") int sessionID);
    @GET("KPI/GetKpiGroup")
    Call<List<KPI>> getKpiGroup(@Query("groupID") int groupID, @Query("sessionID") int sessionID);
    @POST("KPI/PostGeneralKpi")
    Call<KPI> postGeneralKpi(@Body KpiWithSubKpiWeightages kpiWithSubKpiWeightages);
}
