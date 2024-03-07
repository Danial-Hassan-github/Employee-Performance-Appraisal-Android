package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuestionnaireServiceListener {
    @GET("Questionaire/GetConfidentialQuestions")
    Call<List<Question>> GetConfidentialQuestions();
    @GET("Questionaire/GetQuestionnaireTypes")
    Call<List<QuestionnaireType>> getQuestionnaireTypes();
}
