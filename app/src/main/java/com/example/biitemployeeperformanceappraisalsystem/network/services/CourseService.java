package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.CourseServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseService {

    private CourseServiceListener courseServiceListener;
    private Context context;

    public CourseService(Context context){
        courseServiceListener= RetrofitClient.getRetrofitInstance().create(CourseServiceListener.class);
        this.context=context;
    }

    public void getCourses(final Consumer<List<Course>> onSuccess,final Consumer<String> onFailure){
        Call<List<Course>> courseCall = courseServiceListener.getCourses();
        courseCall.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching courses");
            }
        });
    }

    public void getTeacherCourses(int teacherID, int sessionID, final Consumer<List<Course>> onSuccess,final Consumer<String> onFailure){
        Call<List<Course>> courseCall = courseServiceListener.getTeacherCourses(teacherID, sessionID);
        courseCall.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching courses");
            }
        });
    }

    public void getStudentCourses(int studentID,int sessionID,final Consumer<List<Course>> onSuccess,final Consumer<String> onFailure){
        Call<List<Course>> courseCall = courseServiceListener.getStudentCourses(studentID,sessionID);
        courseCall.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching student courses");
            }
        });
    }

    public void getTeacherCourses(int teacherID, final Consumer<List<Course>> onSuccess, final Consumer<String> onFailure){
        Call<List<Course>> courseCall = courseServiceListener.getTeacherCourses(teacherID);
        courseCall.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching teacher courses");
            }
        });
    }

    public void getCourseTeachers(int studentID, int courseID, int sessionID, final Consumer<List<Employee>> onSuccess, final Consumer<String> onFailure){
        Call<List<Employee>> courseCall = courseServiceListener.getCourseTeachers(studentID, courseID, sessionID);
        courseCall.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching teacher courses");
            }
        });
    }
}
