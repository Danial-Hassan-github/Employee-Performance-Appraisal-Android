package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.EvaluationPin;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EvaluationPinServiceListener;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationPinService {
    public Context context;
    EvaluationPinServiceListener evaluationPinServiceListener;
    public EvaluationPinService(Context context){
        evaluationPinServiceListener = RetrofitClient.getRetrofitInstance().create(EvaluationPinServiceListener.class);
        this.context = context;
    }

    public void postConfidentialEvaluationPin(EvaluationPin evaluationPin, final Consumer<EvaluationPin> onSuccess, final Consumer<String> onFailure){
        Call<EvaluationPin> call = evaluationPinServiceListener.postConfidentialEvaluationPin(evaluationPin);
        call.enqueue(new Callback<EvaluationPin>() {
            @Override
            public void onResponse(Call<EvaluationPin> call, Response<EvaluationPin> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<EvaluationPin> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }
}
