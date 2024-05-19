package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EvaluationTime;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface EvaluationTimeServiceListener {
    @GET("EvaluationTime/IsEvaluationTime")
    Call<Boolean> isEvaluationTime(@Query("sessionID") int sessionID, @Query("evaluationType") String evalutionType);
    @POST("EvaluationTime/PostEvaluationTime")
    Call<EvaluationTime> postEvaluationTime(@Body EvaluationTime evaluationTime);
    @PUT("EvaluationTime/PutEvaluationTime")
    Call<EvaluationTime> putEvaluationTime(@Body EvaluationTime evaluationTime);
}
