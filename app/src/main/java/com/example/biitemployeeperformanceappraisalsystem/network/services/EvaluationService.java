package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.ConfidentialEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.DegreeExitEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.DirectorEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.PeerEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.SeniorTeacherEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.SupervisorEvaluation;
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

    public void IsEvaluated(int evaluatorId, int evaluateeId, int courseId, int sessionId, String evaluationType, Consumer<Boolean> onSuccess, Consumer<Boolean> onFailure) {
        Call<String> call = evaluationServiceListener.isEvaluated(evaluatorId, evaluateeId, courseId, sessionId, evaluationType);
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
                Toast.makeText(context.getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postSeniorTeacherEvaluations(List<SeniorTeacherEvaluation> seniorTeacherEvaluations) {
        Call<String> call = evaluationServiceListener.postSeniorTeacherEvaluation(seniorTeacherEvaluations);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context.getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "something went wrong while posting senior teacher evaluation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postDegreeExitEvaluations(List<DegreeExitEvaluation> degreeExitEvaluations) {
        Call<String> call = evaluationServiceListener.postDegreeExitEvaluation(degreeExitEvaluations);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context.getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "something went wrong while posting degree exit evaluation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postConfidentialEvaluation(List<ConfidentialEvaluation> confidentialEvaluations) {
        Call<String> call = evaluationServiceListener.postConfidentialEvaluation(confidentialEvaluations);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context.getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "something went wrong while posting degree exit evaluation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postSupervisorEvaluation(List<SupervisorEvaluation> supervisorEvaluations) {
        Call<String> call = evaluationServiceListener.postSupervisorEvaluation(supervisorEvaluations);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context.getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "something went wrong while posting degree exit evaluation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postDirectorEvaluation(List<DirectorEvaluation> directorEvaluations) {
        Call<String> call = evaluationServiceListener.postDirectorEvaluation(directorEvaluations);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context.getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "something went wrong while posting degree exit evaluation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
