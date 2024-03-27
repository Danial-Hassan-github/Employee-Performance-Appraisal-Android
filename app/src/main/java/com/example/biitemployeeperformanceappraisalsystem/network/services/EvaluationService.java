package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EvaluationServiceListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EvaluationService {
    EvaluationServiceListener evaluationServiceListener;
    Context context;
    public EvaluationService(Context context){
        evaluationServiceListener = RetrofitClient.getRetrofitInstance().create(EvaluationServiceListener.class);
        this.context=context;
    }

    public void postStudentEvaluations(List<StudentEvaluation> studentEvaluations) {
        Call<String> call = evaluationServiceListener.postStudentEvaluation(studentEvaluations);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle successful response if needed
                    Toast.makeText(context.getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful response if needed
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle failure if needed
            }
        });
    }
}
