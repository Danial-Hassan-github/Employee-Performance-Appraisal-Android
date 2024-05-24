package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.EvaluationPin;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EvaluationPinServiceListener;

import java.util.function.Consumer;

import retrofit2.Call;

public class EvaluationPinService {
    public Context context;
    EvaluationPinServiceListener evaluationPinServiceListener;
    public EvaluationPinService(Context context){
        evaluationPinServiceListener = RetrofitClient.getRetrofitInstance().create(EvaluationPinServiceListener.class);
        this.context = context;
    }

    public void postConfidentialEvaluationPin(EvaluationPin evaluationPin, final Consumer<Boolean> onSuccess, final Consumer<String> onFailure){
        Call<EvaluationPin> call = evaluationPinServiceListener.postConfidentialEvaluationPin(evaluationPin);
    }
}
