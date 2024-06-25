package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.Evaluator;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluatorEvaluatess;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluatorResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EvaluatorServiceListener {
    @GET("Evaluator/GetEvaluatees")
    Call<List<Employee>> getEvaluatees(@Query("evaluatorID") int evaluatorID, @Query("sessionID") int sessionID);
    @POST("Evaluator/PostEvaluator")
    Call<List<Evaluator>> postEvaluator(@Body EvaluatorEvaluatess evaluatorEvaluatessList);
    @GET("Evaluator/GetEmployeeEvaluators")
    Call<EvaluatorResponse> getEmployeeEvaluators(@Query("employeeID") int employeeID,
                                                  @Query("evaluationTypeID") int evaluationTypeID,
                                                  @Query("sessionID") int sessionID,
                                                  @Query("courseID") int courseID);
}
