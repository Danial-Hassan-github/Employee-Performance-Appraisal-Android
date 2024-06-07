package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionScore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EmployeeQuestionScoreServiceListener {
    @GET("QuestionsScores/GetQuestionsScoresByEvaluationId")
    Call<List<EmployeeQuestionScore>> getQuestionsScoresByEvaluationId(@Query("employeeID") int employeeID, @Query("sessionID") int sessionID, @Query("evaluationTypeID") int evaluationTypeID);
}
