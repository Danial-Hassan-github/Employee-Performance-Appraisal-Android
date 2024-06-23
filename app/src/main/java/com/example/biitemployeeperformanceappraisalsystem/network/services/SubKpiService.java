package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.SubKpiServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubKpiService {
    private Context context;
    SubKpiServiceListener subKpiServiceListener;
    public SubKpiService(Context context){
        subKpiServiceListener = RetrofitClient.getRetrofitInstance().create(SubKpiServiceListener.class);
        this.context = context;
    }

    public void getAvailableSubKpis(int kpiID, int sessionID, final Consumer<List<SubKpi>> onSuccess, final Consumer<String> onFailure){
        Call<List<SubKpi>> call = subKpiServiceListener.getAvailableSubKpis(kpiID, sessionID);
        call.enqueue(new Callback<List<SubKpi>>() {
            @Override
            public void onResponse(Call<List<SubKpi>> call, Response<List<SubKpi>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SubKpi>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getSubKPIs(int sessionID, final Consumer<List<SubKpi>> onSuccess, final Consumer<String> onFailure){
        Call<List<SubKpi>> call = subKpiServiceListener.getSubKPIs(sessionID);
        call.enqueue(new Callback<List<SubKpi>>() {
            @Override
            public void onResponse(Call<List<SubKpi>> call, Response<List<SubKpi>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SubKpi>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getSubKPIsOfKpi(int kpi_id, int sessionID, final Consumer<List<SubKpi>> onSuccess, final Consumer<String> onFailure){
        Call<List<SubKpi>> call = subKpiServiceListener.getSubKPIsOfKpi(kpi_id, sessionID);
        call.enqueue(new Callback<List<SubKpi>>() {
            @Override
            public void onResponse(Call<List<SubKpi>> call, Response<List<SubKpi>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SubKpi>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void populateSpinner(List<SubKpi> subKpiList, Spinner spinner) {
        if (subKpiList != null && !subKpiList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getSubKpiTitles(subKpiList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "SubKpi list is empty", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getSubKpiTitles(List<SubKpi> subKpiList) {
        String[] titles = new String[subKpiList.size()];
        for (int i = 0; i < subKpiList.size(); i++) {
            titles[i] = subKpiList.get(i).getName();
        }
        return titles;
    }
}
