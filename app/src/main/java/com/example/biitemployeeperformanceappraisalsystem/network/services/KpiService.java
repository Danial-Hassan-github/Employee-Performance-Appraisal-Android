package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiWithWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWithSubKpiWeightages;
import com.example.biitemployeeperformanceappraisalsystem.models.OptionWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.KpiServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

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

    public void getKpis(final Consumer<List<KPI>> onSuccess, final Consumer<String> onFailure){
        Call<List<KPI>> call = kpiServiceListener.getKpis();
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

    public void getGroupKpi(int groupID, int sessionID, final Consumer<List<KPI>> onSuccess, final Consumer<String> onFailure){
        Call<List<KPI>> call = kpiServiceListener.getKpiGroup(groupID, sessionID);
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
                Log.e("Error","While adding general kpi",t);
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void postGeneralKpi(KpiWithSubKpiWeightages kpiWithSubKpiWeightages, final Consumer<KPI> onSuccess, final Consumer<String> onFailure){
        Call<KPI> call = kpiServiceListener.postGeneralKpi(kpiWithSubKpiWeightages);
        call.enqueue(new Callback<KPI>() {
            @Override
            public void onResponse(Call<KPI> call, Response<KPI> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }
                else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<KPI> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void postGroupKpi(GroupKpiWithWeightage groupKpiWithWeightage, final Consumer<String> onSuccess, final Consumer<String> onFailure){
        Call<String> call = kpiServiceListener.postGroupKpi(groupKpiWithWeightage);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void postEmployeeKpi(EmployeeKpi employeeKpi, final Consumer<String> onSuccess, final Consumer<String> onFailure){
        Call<String> call = kpiServiceListener.postEmployeeKpi(employeeKpi);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void putGeneralKpi(List<KPI> kpiList, final Consumer<String> onSuccess, final Consumer<String> onFailure){
        Call<String> call = kpiServiceListener.putGeneralKpi(kpiList);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }
                else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void putGroupKpi(List<KPI> kpiList, final Consumer<String> onSuccess, final Consumer<String> onFailure){
        Call<String> call = kpiServiceListener.putGroupKpi(kpiList);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getGroupKpiId( int department_id, int designation_id, int employee_type_id, int employee_id, final Consumer<Integer> onSuccess, final Consumer<String> onFailure){
        Call<Integer> call = kpiServiceListener.getKpiGroupId(department_id, designation_id, employee_type_id, employee_id);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void putEmployeeKpi(List<KPI> employeeKpis, final Consumer<String> onSuccess, final Consumer<String> onFailure){
        Call<String> call = kpiServiceListener.putEmployeeKpi(employeeKpis);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
