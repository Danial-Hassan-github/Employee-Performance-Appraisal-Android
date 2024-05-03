package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.Task;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithRole;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
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
        taskServiceListener= RetrofitClient.getRetrofitInstance().create(TaskServiceListener.class);
        this.context=context;
    }

    public void getTasks(final Consumer<List<TaskWithEmployees>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskWithEmployees>> tasks = taskServiceListener.getTasks();
        tasks.enqueue(new Callback<List<TaskWithEmployees>>() {
            @Override
            public void onResponse(Call<List<TaskWithEmployees>> call, Response<List<TaskWithEmployees>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TaskWithEmployees>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching tasks");
            }
        });
    }

    public void getPendingTasks(final Consumer<List<TaskWithEmployees>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskWithEmployees>> tasks = taskServiceListener.getPendingTasks();
        tasks.enqueue(new Callback<List<TaskWithEmployees>>() {
            @Override
            public void onResponse(Call<List<TaskWithEmployees>> call, Response<List<TaskWithEmployees>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TaskWithEmployees>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching tasks");
            }
        });
    }

    public void getCompletedTasks(final Consumer<List<TaskWithEmployees>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskWithEmployees>> tasks = taskServiceListener.getCompletedTasks();
        tasks.enqueue(new Callback<List<TaskWithEmployees>>() {
            @Override
            public void onResponse(Call<List<TaskWithEmployees>> call, Response<List<TaskWithEmployees>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TaskWithEmployees>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching tasks");
            }
        });
    }

    public void getEmployeeTasks(int employeeId, final Consumer<List<TaskWithEmployees>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskWithEmployees>> tasks = taskServiceListener.getEmployeeTasks(employeeId);
        tasks.enqueue(new Callback<List<TaskWithEmployees>>() {
            @Override
            public void onResponse(Call<List<TaskWithEmployees>> call, Response<List<TaskWithEmployees>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TaskWithEmployees>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching tasks");
            }
        });
    }

    public void postTask(Task task, final Consumer<List<TaskWithEmployees>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskWithEmployees>> tasks = taskServiceListener.postTask(task);
        tasks.enqueue(new Callback<List<TaskWithEmployees>>() {
            @Override
            public void onResponse(Call<List<TaskWithEmployees>> call, Response<List<TaskWithEmployees>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TaskWithEmployees>> call, Throwable t) {
                onFailure.accept("Something went wrong while adding task");
            }
        });
    }

    public void postRoleBasedTask(TaskWithRole taskWithRole, final Consumer<List<TaskWithEmployees>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskWithEmployees>> tasks = taskServiceListener.postRoleBasedTask(taskWithRole);
        tasks.enqueue(new Callback<List<TaskWithEmployees>>() {
            @Override
            public void onResponse(Call<List<TaskWithEmployees>> call, Response<List<TaskWithEmployees>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TaskWithEmployees>> call, Throwable t) {
                onFailure.accept("Something went wrong while adding task");
            }
        });
    }

    public void putTask(Task task, final Consumer<Task> onSuccess, final Consumer<String> onFailure) {
        Call<Task> tasks = taskServiceListener.putTask(task);
        tasks.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                onFailure.accept("Something went wrong while updating task");
            }
        });
    }
}
