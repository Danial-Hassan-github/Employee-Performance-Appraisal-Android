package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EvaluationPin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EvaluationPinServiceListener {
    @POST("EvaluationPin/PostConfidentialEvaluationPin")
    Call<EvaluationPin> postConfidentialEvaluationPin(@Body EvaluationPin evaluationPin);
}
