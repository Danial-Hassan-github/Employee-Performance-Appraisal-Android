package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.Department;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.DepartmentServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentService {
    DepartmentServiceListener departmentServiceListener;
    Context context;
    public DepartmentService(Context context){
        departmentServiceListener= RetrofitClient.getRetrofitInstance().create(DepartmentServiceListener.class);
        this.context=context;
    }

    public void getDepartments(final Consumer<List<Department>> onSuccess, final Consumer<String> onFailure) {
        Call<List<Department>> departmentCall = departmentServiceListener.getDepartments();
        departmentCall.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching departments");
            }
        });
    }

    public void populateDepartmentSpinner(List<Department> departmentList, Spinner spinner) {
        if (departmentList != null && !departmentList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getDepartmentTitles(departmentList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Department list is empty", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getDepartmentTitles(List<Department> departmentList) {
        String[] titles = new String[departmentList.size()];
        for (int i = 0; i < departmentList.size(); i++) {
            titles[i] = departmentList.get(i).getName();
        }
        return titles;
    }
}
