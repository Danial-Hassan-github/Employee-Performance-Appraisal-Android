package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.ApiRequestModels.EmployeeIdsWithSession;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EmployeeKpiScoreServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeKpiScoreService {
    EmployeeKpiScoreServiceListener employeeKpiScoreServiceListener;
    Context context;
    public EmployeeKpiScoreService(Context context){
        employeeKpiScoreServiceListener = RetrofitClient.getRetrofitInstance().create(EmployeeKpiScoreServiceListener.class);
        this.context = context;
    }

    public void getEmployeeKpiScore(int employeeID, int sessionID, final Consumer<List<KpiScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<KpiScore>> employeeKpiScoresCall = employeeKpiScoreServiceListener.getEmployeeKpiScore(employeeID, sessionID);
        employeeKpiScoresCall.enqueue(new Callback<List<KpiScore>>() {
            @Override
            public void onResponse(Call<List<KpiScore>> call, Response<List<KpiScore>> response) {
                if (response.isSuccessful())
                    onSuccess.accept(response.body());
                else
                    onFailure.accept(response.message());
            }

            @Override
            public void onFailure(Call<List<KpiScore>> call, Throwable t) {
                onFailure.accept(t.toString());
            }
        });
    }

    public void compareMultiSessionKpiEmployeePerformance(List<Integer> sessionIDs, int employeeId, int kpi_id, final Consumer<List<KpiScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<KpiScore>> employeeKpiScoresCall = employeeKpiScoreServiceListener.compareMultiSessionKpiEmployeePerformance(sessionIDs, employeeId, kpi_id);
        employeeKpiScoresCall.enqueue(new Callback<List<KpiScore>>() {
            @Override
            public void onResponse(Call<List<KpiScore>> call, Response<List<KpiScore>> response) {
                if (response.isSuccessful())
                    onSuccess.accept(response.body());
                else
                    onFailure.accept(response.message());
            }

            @Override
            public void onFailure(Call<List<KpiScore>> call, Throwable t) {
                onFailure.accept(t.toString());
            }
        });
    }

    public void compareEmployeeSingleKpiScore(List<Integer> employeeIds, int session_id, int kpi_id, final Consumer<List<EmployeeKpiScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<EmployeeKpiScore>> employeeKpiScoresCall = employeeKpiScoreServiceListener.compareEmployeeSingleKpiScore(employeeIds, session_id, kpi_id);
        employeeKpiScoresCall.enqueue(new Callback<List<EmployeeKpiScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeKpiScore>> call, Response<List<EmployeeKpiScore>> response) {
                if (response.isSuccessful())
                    onSuccess.accept(response.body());
                else
                    onFailure.accept(response.message());
            }

            @Override
            public void onFailure(Call<List<EmployeeKpiScore>> call, Throwable t) {
                onFailure.accept(t.toString());
            }
        });
    }

    public void compareEmployeeKpiScore(EmployeeIdsWithSession employeeIdsWithSession, final Consumer<List<EmployeeKpiScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<EmployeeKpiScore>> employeeKpiScoresCall = employeeKpiScoreServiceListener.compareEmployeeKpiScore(employeeIdsWithSession);
        employeeKpiScoresCall.enqueue(new Callback<List<EmployeeKpiScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeKpiScore>> call, Response<List<EmployeeKpiScore>> response) {
                if (response.isSuccessful())
                    onSuccess.accept(response.body());
                else
                    onFailure.accept(response.message());
            }

            @Override
            public void onFailure(Call<List<EmployeeKpiScore>> call, Throwable t) {
                onFailure.accept(t.toString());
            }
        });
    }

    public void getEmployeeKpiScoreMultiSession(int employeeID, int startingSessionID, int endingSessionID, final Consumer<List<EmployeeKpiScoreMultiSession>> onSuccess, final Consumer<String> onFailure){
        Call<List<EmployeeKpiScoreMultiSession>> employeeKpiScoresCall = employeeKpiScoreServiceListener.getEmployeeKpiScoreMultiSession(employeeID, startingSessionID, endingSessionID);
        employeeKpiScoresCall.enqueue(new Callback<List<EmployeeKpiScoreMultiSession>>() {
            @Override
            public void onResponse(Call<List<EmployeeKpiScoreMultiSession>> call, Response<List<EmployeeKpiScoreMultiSession>> response) {
                if (response.isSuccessful())
                    onSuccess.accept(response.body());
                else
                    onFailure.accept(response.message());
            }

            @Override
            public void onFailure(Call<List<EmployeeKpiScoreMultiSession>> call, Throwable t) {
                onFailure.accept(t.toString());
            }
        });
    }

    public void compareMultiSessionAllKpiEmployeePerformance(List<Integer> sessionIDs, int employeeId, final Consumer<List<EmployeeKpiScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<EmployeeKpiScore>> employeeKpiScoresCall = employeeKpiScoreServiceListener.compareMultiSessionAllKpiEmployeePerformance(sessionIDs, employeeId);
        employeeKpiScoresCall.enqueue(new Callback<List<EmployeeKpiScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeKpiScore>> call, Response<List<EmployeeKpiScore>> response) {
                if (response.isSuccessful())
                    onSuccess.accept(response.body());
                else
                    onFailure.accept(response.message());
            }

            @Override
            public void onFailure(Call<List<EmployeeKpiScore>> call, Throwable t) {
                onFailure.accept(t.toString());
            }
        });
    }

    public void compareYearlyEmployeeKpiScore(List<Integer> employeeIds, String year, int kpi_id, final Consumer<List<EmployeeKpiScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<EmployeeKpiScore>> employeeKpiScoresCall = employeeKpiScoreServiceListener.compareYearlyKpiEmployeePerformance(employeeIds, year, kpi_id);
        employeeKpiScoresCall.enqueue(new Callback<List<EmployeeKpiScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeKpiScore>> call, Response<List<EmployeeKpiScore>> response) {
                if (response.isSuccessful())
                    onSuccess.accept(response.body());
                else
                    onFailure.accept(response.message());
            }

            @Override
            public void onFailure(Call<List<EmployeeKpiScore>> call, Throwable t) {
                onFailure.accept(t.toString());
            }
        });
    }
}
