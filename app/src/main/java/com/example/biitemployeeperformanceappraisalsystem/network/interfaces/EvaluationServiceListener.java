package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EvaluationServiceListener {
    @POST("Evaluation/PostStudentEvaluation")
    Call<String> postStudentEvaluation(@Body List<StudentEvaluation> sTUDENT_EVALUATIONs);
}
