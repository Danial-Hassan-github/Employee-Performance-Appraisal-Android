package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.ApiRequestModels.QuestionsScoresRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionsScores;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionScore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EmployeeQuestionScoreServiceListener {
    @GET("EvaluationScores/GetQuestionsScoresByEvaluationId")
    Call<List<QuestionScore>> getQuestionsScoresByEvaluationId(@Query("employeeID") int employeeID, @Query("sessionID") int sessionID, @Query("evaluationTypeID") int evaluationTypeID);
    @GET("EvaluationScores/GetQuestionsScores")
    Call<List<QuestionScore>> getQuestionsScores(
            @Query("employeeID1") int employeeID1,
            @Query("employeeID2") int employeeID2,
            @Query("sessionID") int sessionID,
            @Query("evaluationTypeID") int evaluationTypeID,
            @Query("courseID") int courseID
    );

    @POST("QuestionsScores/GetMultiEmployeeQuestionsScores")
    Call<List<EmployeeQuestionsScores>> getMultiEmployeeQuestionsScores(@Body QuestionsScoresRequest questionsScoresRequest);
}
