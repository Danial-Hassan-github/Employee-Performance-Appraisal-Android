package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.OptionWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface QuestionnaireServiceListener {
    @POST("Questionnaire/PostQuestion")
    Call<Question> postQuestion(@Body Question question);
    @GET("Questionnaire/GetQuestionnaireTypes")
    Call<List<QuestionnaireType>> getQuestionnaireTypes();
    @GET("Questionnaire/GetQuestionnaireByTypeID")
    Call<List<Question>> getQuestionnaireByTypeID(@Query("questionnaireTypeId") int questionnaireTypeId);
    @GET("Questionnaire/GetQuestionnaireByType")
    Call<List<Question>> getQuestionnaireByType(@Query("questionnaireType") String questionnaireType);
    @GET("Questionnaire/GetConfidentialQuestions")
    Call<List<Question>> GetConfidentialQuestions();
    @GET("Questionnaire/GetStudentQuestions")
    Call<List<Question>> GetStudentQuestions();
    @GET("Questionnaire/GetPeerQuestions")
    Call<List<Question>> GetPeerQuestions();
    @GET("Questionnaire/GetSeniorQuestions")
    Call<List<Question>> GetSeniorQuestions();
    @PUT("Questionnaire/PutQuestion")
    Call<Question> putQuestion(@Body Question question);
    @DELETE("Questionnaire/DeleteQuestion")
    Call<Question> deleteQuestion(@Query("id") int id);
    @GET("Questionnaire/GetOptionsWeightages")
    Call<List<OptionWeightage>> getOptionsWeightage();
}
