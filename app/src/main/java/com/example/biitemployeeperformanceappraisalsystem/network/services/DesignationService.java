package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.Department;
import com.example.biitemployeeperformanceappraisalsystem.models.Designation;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.DesignationServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesignationService {
    DesignationServiceListener designationServiceListener;
    Context context;

    public DesignationService(Context context) {
        designationServiceListener= RetrofitClient.getRetrofitInstance().create(DesignationServiceListener.class);
        this.context=context;
    }

    public void getDesignations(final Consumer<List<Designation>> onSuccess, final Consumer<String> onFailure) {
        Call<List<Designation>> designationCall = designationServiceListener.getDesignations();
        designationCall.enqueue(new Callback<List<Designation>>() {
            @Override
            public void onResponse(Call<List<Designation>> call, Response<List<Designation>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Designation>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching designations");
            }
        });
    }

    public void populateDesignationSpinner(List<Designation> designationList, Spinner spinner) {
        if (designationList != null && !designationList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getDesignationTitles(designationList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Department list is empty", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getDesignationTitles(List<Designation> designationList) {
        String[] titles = new String[designationList.size()];
        for (int i = 0; i < designationList.size(); i++) {
            titles[i] = designationList.get(i).getName();
        }
        return titles;
    }
}
