package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.OptionWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.KpiServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KpiService {
    Context context;
    KpiServiceListener kpiServiceListener;
    public KpiService(Context context){
        kpiServiceListener= RetrofitClient.getRetrofitInstance().create(KpiServiceListener.class);
        this.context=context;
    }

    public void getSessionKpis(int sessionID, final Consumer<List<GroupKpiDetails>> onSuccess, final Consumer<String> onFailure){
        Call<List<GroupKpiDetails>> kpiCall = kpiServiceListener.getSessionKpis(sessionID);

        kpiCall.enqueue(new Callback<List<GroupKpiDetails>>() {
            @Override
            public void onResponse(Call<List<GroupKpiDetails>> call, Response<List<GroupKpiDetails>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<GroupKpiDetails>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getGroupKpi(int groupID, final Consumer<List<KPI>> onSuccess, final Consumer<String> onFailure){
        Call<List<KPI>> call = kpiServiceListener.getKpiGroup(groupID);
        call.enqueue(new Callback<List<KPI>>() {
            @Override
            public void onResponse(Call<List<KPI>> call, Response<List<KPI>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<KPI>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void populateSpinner(List<KPI> kpiList, Spinner spinner) {
        if (kpiList != null && !kpiList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getKpiTitles(kpiList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Session list is empty", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getKpiTitles(List<KPI> kpiList) {
        String[] titles = new String[kpiList.size()];
        for (int i = 0; i < kpiList.size(); i++) {
            titles[i] = kpiList.get(i).getName();
        }
        return titles;
    }
}
