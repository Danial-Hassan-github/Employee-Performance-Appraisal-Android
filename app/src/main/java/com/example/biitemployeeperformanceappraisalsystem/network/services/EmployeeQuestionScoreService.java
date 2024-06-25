package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.ApiRequestModels.QuestionsScoresRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionsScores;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionScore;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EmployeeQuestionScoreServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeQuestionScoreService {
    EmployeeQuestionScoreServiceListener employeeQuestionScoreServiceListener;
    Context context;
    public EmployeeQuestionScoreService(Context context){
        employeeQuestionScoreServiceListener = RetrofitClient.getRetrofitInstance().create(EmployeeQuestionScoreServiceListener.class);
        this.context = context;
    }

    public void getEmployeeQuestionScoreByEvaluationId(int employeeID, int sessionID, int evaluationTypeID, final Consumer<List<QuestionScore>> onSuccess, final Consumer<String> onFailure) {
        Call<List<QuestionScore>> employeeScoreCall = employeeQuestionScoreServiceListener.getQuestionsScoresByEvaluationId(employeeID, sessionID, evaluationTypeID);
        employeeScoreCall.enqueue(new Callback<List<QuestionScore>>() {
            @Override
            public void onResponse(Call<List<QuestionScore>> call, Response<List<QuestionScore>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<QuestionScore>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching scores");
            }
        });
    }

    public void getEmployeeQuestionScore(int employeeID1, int employeeID2, int sessionID, int evaluationTypeID, int courseID, final Consumer<List<QuestionScore>> onSuccess, final Consumer<String> onFailure) {
        Call<List<QuestionScore>> employeeScoreCall = employeeQuestionScoreServiceListener.getQuestionsScores(employeeID1, employeeID2, sessionID, evaluationTypeID, courseID);
        employeeScoreCall.enqueue(new Callback<List<QuestionScore>>() {
            @Override
            public void onResponse(Call<List<QuestionScore>> call, Response<List<QuestionScore>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<QuestionScore>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching scores: " + t.getMessage());
            }
        });
    }


    public void getMultiEmployeeQuestionsScores(QuestionsScoresRequest questionsScoresRequest, final Consumer<List<EmployeeQuestionsScores>> onSuccess, final Consumer<String> onFailure){
        Call<List<EmployeeQuestionsScores>> employeeScoreCall = employeeQuestionScoreServiceListener.getMultiEmployeeQuestionsScores(questionsScoresRequest);
        employeeScoreCall.enqueue(new Callback<List<EmployeeQuestionsScores>>() {
            @Override
            public void onResponse(Call<List<EmployeeQuestionsScores>> call, Response<List<EmployeeQuestionsScores>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeQuestionsScores>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching scores");
            }
        });
    }
}
