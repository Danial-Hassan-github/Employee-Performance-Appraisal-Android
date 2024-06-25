package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.Evaluator;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluatorEvaluatess;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluatorResponse;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EvaluatorServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluatorService {
    Context context;
    EvaluatorServiceListener evaluatorServiceListener;
    public EvaluatorService(Context context){
        evaluatorServiceListener = RetrofitClient.getRetrofitInstance().create(EvaluatorServiceListener.class);
        this.context=context;
    }

    public void getEvaluatees(int evaluatorID, int sessionID, Consumer<List<Employee>> onSuccess, Consumer<String> onFailure){
        Call<List<Employee>> evaluatees = evaluatorServiceListener.getEvaluatees(evaluatorID, sessionID);

        evaluatees.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching evaluatees");
            }
        });
    }

    public void postEvaluator(EvaluatorEvaluatess evaluatorEvaluatees, Consumer<List<Evaluator>> onSuccess, Consumer<String> onFailure){
        Call<List<Evaluator>> evaluatorCall = evaluatorServiceListener.postEvaluator(evaluatorEvaluatees);

        evaluatorCall.enqueue(new Callback<List<Evaluator>>() {
            @Override
            public void onResponse(Call<List<Evaluator>> call, Response<List<Evaluator>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                    Toast.makeText(context, "Evaluator added successfully", Toast.LENGTH_SHORT).show();
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Evaluator>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getEmployeeEvaluators(int employeeID, int evaluationTypeID, int sessionID, int courseID, Consumer<EvaluatorResponse> onSuccess, Consumer<String> onFailure){
        Call<EvaluatorResponse> call = evaluatorServiceListener.getEmployeeEvaluators(employeeID, evaluationTypeID, sessionID, courseID);

        call.enqueue(new Callback<EvaluatorResponse>() {
            @Override
            public void onResponse(Call<EvaluatorResponse> call, Response<EvaluatorResponse> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<EvaluatorResponse> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }
}
