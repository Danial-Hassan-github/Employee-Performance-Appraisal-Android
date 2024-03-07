package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.TaskDetails;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.TaskServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskService {
    TaskServiceListener taskServiceListener;
    Context context;
    public TaskService(Context context){
        taskServiceListener=RetrofitClient.getRetrofitInstance().create(TaskServiceListener.class);
        this.context=context;
    }

    public void getTasks(final Consumer<List<TaskDetails>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskDetails>> employees = taskServiceListener.getTasks();
        employees.enqueue(new Callback<List<TaskDetails>>() {
            @Override
            public void onResponse(Call<List<TaskDetails>> call, Response<List<TaskDetails>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TaskDetails>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching tasks");
            }
        });
    }
}
