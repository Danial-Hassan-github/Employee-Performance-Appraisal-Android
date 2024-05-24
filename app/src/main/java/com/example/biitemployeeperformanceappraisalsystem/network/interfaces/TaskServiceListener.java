package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Task;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithRole;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TaskServiceListener {
    @GET("Task/GetTasks")
    Call<List<TaskWithEmployees>> getTasks();

    @GET("Task/GetPendingTasks")
    Call<List<TaskWithEmployees>> getPendingTasks();

    @GET("Task/GetCompletedTasks")
    Call<List<TaskWithEmployees>> getCompletedTasks();

    @GET("Task/GetEmployeeTasks")
    Call<List<TaskWithEmployees>> getEmployeeTasks(@Query("employeeID") int employeeID);

    @POST("Task/PostTask")
    Call<List<TaskWithEmployees>> postTask(@Body Task task);
    @POST("Task/PostRoleBasedTask")
    Call<List<TaskWithEmployees>> postRoleBasedTask(@Body TaskWithRole taskWithRole);
    @PUT("Task/PutTask")
    Call<Task> putTask(@Body Task task);
    @DELETE("Task/DeleteTask")
    Call<Task> deleteTask(@Query("id") int id);
}
