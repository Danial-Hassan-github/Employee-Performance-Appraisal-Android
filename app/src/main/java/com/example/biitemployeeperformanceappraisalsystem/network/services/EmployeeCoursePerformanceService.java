package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.util.Log;

import com.example.biitemployeeperformanceappraisalsystem.models.CourseScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCourseScore;
import com.example.biitemployeeperformanceappraisalsystem.models.MultiEmployeeCoursePerformanceRequest;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.EmployeeCoursePerformanceServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeCoursePerformanceService {
    Context context;
    EmployeeCoursePerformanceServiceListener employeeCoursePerformanceServiceListener;
    public EmployeeCoursePerformanceService(Context context){
        employeeCoursePerformanceServiceListener = RetrofitClient.getRetrofitInstance().create(EmployeeCoursePerformanceServiceListener.class);
        this.context = context;
    }

    public void getEmployeeCoursePerformance(int employeeID, int sessionID, int courseID, final Consumer<CourseScore> onSuccess, final Consumer<String> onFailure){
        Call<CourseScore> call = employeeCoursePerformanceServiceListener.getEmployeeCoursePerformance(employeeID, sessionID, courseID);
        call.enqueue(new Callback<CourseScore>() {
            @Override
            public void onResponse(Call<CourseScore> call, Response<CourseScore> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<CourseScore> call, Throwable t) {
                Log.e("single"," person",t);
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getEmployeeCoursesPerformance(int teacherID, int sessionID, final Consumer<List<CourseScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<CourseScore>> call = employeeCoursePerformanceServiceListener.getEmployeeCoursesPerformance(teacherID, sessionID);
        call.enqueue(new Callback<List<CourseScore>>() {
            @Override
            public void onResponse(Call<List<CourseScore>> call, Response<List<CourseScore>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CourseScore>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getMultiEmployeeCoursePerformance(MultiEmployeeCoursePerformanceRequest multiEmployeeCoursePerformanceRequest, final Consumer<List<EmployeeCourseScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<EmployeeCourseScore>> call = employeeCoursePerformanceServiceListener.getMultiEmployeeCoursePerformance(multiEmployeeCoursePerformanceRequest);
        call.enqueue(new Callback<List<EmployeeCourseScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeCourseScore>> call, Response<List<EmployeeCourseScore>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeCourseScore>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void compareEmployeeCoursePerformance(int employeeID1, int employeeID2, int sessionID, int courseID, final Consumer<List<CourseScore>> onSuccess, final Consumer<String> onFailure){
        Call<List<CourseScore>> call = employeeCoursePerformanceServiceListener.compareEmployeeCoursePerformance(employeeID1, employeeID2, sessionID, courseID);
        call.enqueue(new Callback<List<CourseScore>>() {
            @Override
            public void onResponse(Call<List<CourseScore>> call, Response<List<CourseScore>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CourseScore>> call, Throwable t) {
                Log.e("","",t);
                onFailure.accept(t.getMessage());
            }
        });
    }
}
