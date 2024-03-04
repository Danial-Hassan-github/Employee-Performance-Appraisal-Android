package com.example.biitemployeeperformanceappraisalsystem.network;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskDetails;
import com.example.biitemployeeperformanceappraisalsystem.student.StudentMainActivity;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonData {
    ApiNetwork apiNetwork;
    RetrofitClient retrofitClient;
    Context context;

    public CommonData(Context context) {
        retrofitClient = new RetrofitClient();
        apiNetwork = retrofitClient.getRetrofitInstance().create(ApiNetwork.class);
        this.context = context;
    }

    public void getEmployees(final Consumer<List<EmployeeDetails>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeDetails>> employees = apiNetwork.getEmployees();
        employees.enqueue(new Callback<List<EmployeeDetails>>() {
            @Override
            public void onResponse(Call<List<EmployeeDetails>> call, Response<List<EmployeeDetails>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeDetails>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void GetEmployeesWithKpiScores(final Consumer<List<EmployeeDetailsScore>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeDetailsScore>> employeeDetailsScoreList = apiNetwork.GetEmployeesWithKpiScores();
        employeeDetailsScoreList.enqueue(new Callback<List<EmployeeDetailsScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeDetailsScore>> call, Response<List<EmployeeDetailsScore>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeDetailsScore>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void getTasks(final Consumer<List<TaskDetails>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskDetails>> employees = apiNetwork.getTasks();
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

    public void getSessionPerformance() {

    }

    public void getSessionsPerformance() {

    }
}
