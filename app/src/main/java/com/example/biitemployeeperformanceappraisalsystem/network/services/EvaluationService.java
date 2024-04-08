package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.PeerEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EvaluationServiceListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationService {
    EvaluationServiceListener evaluationServiceListener;
    Context context;
    public EvaluationService(Context context){
        evaluationServiceListener = RetrofitClient.getRetrofitInstance().create(EvaluationServiceListener.class);
        this.context=context;
    }

    public void IsEvaluated(int studentId, int teacherId, int courseId, int sessionId, String evaluationType, Consumer<Boolean> onSuccess, Consumer<Boolean> onFailure) {
        Call<String> call = evaluationServiceListener.isEvaluated(studentId, teacherId, courseId, sessionId, evaluationType);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Check the response body to determine if evaluation is successful
                    String responseBody = response.body();
                    boolean isEvaluated = (responseBody != null && responseBody.equals("true"));
                    onSuccess.accept(true);
                } else {
                    onSuccess.accept(false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onFailure.accept(false);
            }
        });
    }


    public void postStudentEvaluations(List<StudentEvaluation> studentEvaluations) {
        Call<String> call = evaluationServiceListener.postStudentEvaluation(studentEvaluations);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle successful response if needed
                    Toast.makeText(context.getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful response if needed
                    Toast.makeText(context.getApplicationContext(), call.toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle failure if needed
                Toast.makeText(context.getApplicationContext(), "something went wrong while posting student evaluation",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postPeerEvaluations(List<PeerEvaluation> peerEvaluations) {
        Call<String> call = evaluationServiceListener.postPeerEvaluation(peerEvaluations);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle successful response if needed
                    Toast.makeText(context.getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful response if needed
                    Toast.makeText(context.getApplicationContext(), "something went wrong while posting peer evaluation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle failure if needed
                Toast.makeText(context.getApplicationContext(), call.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
