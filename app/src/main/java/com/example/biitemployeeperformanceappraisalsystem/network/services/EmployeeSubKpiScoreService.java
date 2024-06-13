package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeSubKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EmployeeSubKpiScoreServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeSubKpiScoreService {
    private Context context;
    private EmployeeSubKpiScoreServiceListener employeeSubKpiScoreServiceListener;
    public EmployeeSubKpiScoreService(Context context){
        this.context = context;
        employeeSubKpiScoreServiceListener = RetrofitClient.getRetrofitInstance().create(EmployeeSubKpiScoreServiceListener.class);
    }

    public void getSubKpiEmployeePerformance(int employeeID, int sessionID, final Consumer<List<SubKpiScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<SubKpiScore>> employeeSubKpiScoreCall = employeeSubKpiScoreServiceListener.getSubKpiEmployeePerformance(employeeID, sessionID);
        employeeSubKpiScoreCall.enqueue(new Callback<List<SubKpiScore>>() {
            @Override
            public void onResponse(Call<List<SubKpiScore>> call, Response<List<SubKpiScore>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SubKpiScore>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getSubKpiMultiEmployeePerformance(List<Integer> employeeIDs, int sessionID, final Consumer<List<EmployeeSubKpiScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<EmployeeSubKpiScore>> employeeSubKpiScoreCall = employeeSubKpiScoreServiceListener.getSubKpiMultiEmployeePerformance(employeeIDs, sessionID);
        employeeSubKpiScoreCall.enqueue(new Callback<List<EmployeeSubKpiScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeSubKpiScore>> call, Response<List<EmployeeSubKpiScore>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeSubKpiScore>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }
}
