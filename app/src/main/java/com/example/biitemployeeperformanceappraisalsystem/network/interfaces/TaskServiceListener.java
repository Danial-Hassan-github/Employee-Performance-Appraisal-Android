package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.TaskDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TaskServiceListener {
    @GET("Task/getTasksDetail")
    Call<List<TaskDetails>> getTasks();

    @GET("Task/GetEmployeeTasks")
    Call<List<TaskDetails>> GetEmployeeTasks(@Query("employeeID") int employeeID);
}
