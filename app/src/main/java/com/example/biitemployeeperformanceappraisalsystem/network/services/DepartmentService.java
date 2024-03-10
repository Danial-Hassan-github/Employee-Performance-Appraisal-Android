package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.Department;
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

    public void getDesignations(final Consumer<List<Department>> onSuccess, final Consumer<String> onFailure) {
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
}
