package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionScore;
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

    public void getEmployeeQuestionScore(int employeeID, int sessionID, int evaluationTypeID, final Consumer<List<EmployeeQuestionScore>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeQuestionScore>> emloyeeScoreCall = employeeQuestionScoreServiceListener.getQuestionsScoresByEvaluationId(employeeID, sessionID, evaluationTypeID);
        emloyeeScoreCall.enqueue(new Callback<List<EmployeeQuestionScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeQuestionScore>> call, Response<List<EmployeeQuestionScore>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeQuestionScore>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching scores");
            }
        });
    }
}
