package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.ConfidentialEvaluatorStudent;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.StudentServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentService {
    private Context context;
    private StudentServiceListener studentServiceListener;
    public StudentService(Context context){
        this.context = context;
        studentServiceListener = RetrofitClient.getRetrofitInstance().create(StudentServiceListener.class);
    }

    public void getStudentsBySection(int semester, String section, final Consumer<List<Student>> onSuccess, final Consumer<String> onFailure){
        Call<List<Student>> call = studentServiceListener.getStudentsBySection(semester, section);

        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void getStudentSessionTeacher(int studentId, int sessionId, final Consumer<List<Employee>> onSuccess, final Consumer<String> onFailure){
        Call<List<Employee>> call = studentServiceListener.getStudentSessionTeacher(studentId, sessionId);

        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }

    public void postConfidentialEvaluatorStudents(List<ConfidentialEvaluatorStudent> confidentialEvaluatorStudents, final Consumer<String> onSuccess, final Consumer<String> onFailure){
        Call<String> call = studentServiceListener.postConfidentialEvaluatorStudents(confidentialEvaluatorStudents);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }
}
