package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiWithWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWithSubKpiWeightages;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface KpiServiceListener {
    @GET("KPI/GetKPIs")
    Call<List<KPI>> getKpis();
    @GET("KPI/GetSessionKpis")
    Call<List<GroupKpiDetails>> getSessionKpis(@Query("sessionID") int sessionID);
    @GET("KPI/GetKpiGroup")
    Call<List<KPI>> getKpiGroup(@Query("groupID") int groupID, @Query("sessionID") int sessionID);
    @GET("KPI/GetKpiGroupId")
    Call<Integer> getKpiGroupId(@Query("department_id") int department_id, @Query("designation_id") int designation_id, @Query("employee_type_id") int employee_type_id, @Query("employee_id") int employee_id);
    @POST("KPI/PostGeneralKpi")
    Call<KPI> postGeneralKpi(@Body KpiWithSubKpiWeightages kpiWithSubKpiWeightages);
    @POST("KPI/PostEmployeeKpi")
    Call<String> postEmployeeKpi(@Body EmployeeKpi employeeKpi);
    @POST("KPI/PostGroupKpi")
    Call<String> postGroupKpi(@Body GroupKpiWithWeightage groupKpiWithWeightage);
    @PUT("KPI/PutEmployeeKpi")
    Call<String> putEmployeeKpi(@Body List<KPI> kpiList);
    @PUT("KPI/PutGroupKpi")
    Call<String> putGroupKpi(@Body List<KPI> kpiList);
    @PUT("KPI/PutGeneralKpi")
    Call<String> putGeneralKpi(@Body List<KPI> kpiList);
}
