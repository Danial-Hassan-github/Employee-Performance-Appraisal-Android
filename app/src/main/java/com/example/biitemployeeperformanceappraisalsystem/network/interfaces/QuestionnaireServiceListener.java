package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuestionnaireServiceListener {
    @GET("Questionnaire/GetConfidentialQuestions")
    Call<List<Question>> GetConfidentialQuestions();
    @GET("Questionnaire/GetQuestionnaireTypes")
    Call<List<QuestionnaireType>> getQuestionnaireTypes();
    @GET("Questionnaire/GetQuestionnaireByType")
    Call<List<Question>> getQuestionnaireByType(@Query("questionnaireTypeId") int questionnaireTypeId);
}
