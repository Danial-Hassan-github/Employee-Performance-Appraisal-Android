package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.EvaluationTime;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.StudentSupervisor;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EvaluationTimeServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationTimeService {
    private Context context;
    private EvaluationTimeServiceListener evaluationTimeServiceListener;
    public EvaluationTimeService(Context context){
        evaluationTimeServiceListener = RetrofitClient.getRetrofitInstance().create(EvaluationTimeServiceListener.class);
        this.context = context;
    }

    public void isEvaluationTime(int sessionID, String evaluationType, final Consumer<Boolean> onSuccess, final Consumer<String> onFailure){
        Call<Boolean> call = evaluationTimeServiceListener.isEvaluationTime(sessionID, evaluationType);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void checkDegreeExitEligibility(int studentID, final Consumer<StudentSupervisor> onSuccess, final Consumer<String> onFailure){
        Call<StudentSupervisor> call = evaluationTimeServiceListener.checkDegreeExitEligibility(studentID);
        call.enqueue(new Callback<StudentSupervisor>() {
            @Override
            public void onResponse(Call<StudentSupervisor> call, Response<StudentSupervisor> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<StudentSupervisor> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void checkConfidentialPin(int sessionID, String pin, final Consumer<Boolean> onSuccess, final Consumer<String> onFailure){
        Call<Boolean> call = evaluationTimeServiceListener.checkConfidentialPin(sessionID, pin);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void postEvaluationTime(EvaluationTime evaluationTime, final Consumer<EvaluationTime> onSuccess, final Consumer<String> onFailure){
        Call<EvaluationTime> call = evaluationTimeServiceListener.postEvaluationTime(evaluationTime);
        call.enqueue(new Callback<EvaluationTime>() {
            @Override
            public void onResponse(Call<EvaluationTime> call, Response<EvaluationTime> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<EvaluationTime> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void putEvaluationTime(EvaluationTime evaluationTime, final Consumer<EvaluationTime> onSuccess, final Consumer<String> onFailure){
        Call<EvaluationTime> call = evaluationTimeServiceListener.putEvaluationTime(evaluationTime);
        call.enqueue(new Callback<EvaluationTime>() {
            @Override
            public void onResponse(Call<EvaluationTime> call, Response<EvaluationTime> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<EvaluationTime> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }
}
