package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EvaluatorServiceListener {
    @GET("Evaluator/GetEvaluatees")
    Call<List<Employee>> getEvaluatees(@Query("evaluatorID") int evaluatorID, @Query("sessionID") int sessionID);
}
