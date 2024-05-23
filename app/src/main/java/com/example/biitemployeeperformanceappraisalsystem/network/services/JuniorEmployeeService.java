package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.models.TeacherJunior;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.JuniorEmployeeServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JuniorEmployeeService {
    Context context;
    JuniorEmployeeServiceListener juniorEmployeeServiceListener;
    public JuniorEmployeeService(Context context){
        juniorEmployeeServiceListener = RetrofitClient.getRetrofitInstance().create(JuniorEmployeeServiceListener.class);
        this.context = context;
    }

    public void getTeacherJuniors(int teacherID, int sessionID, final Consumer<List<TeacherJunior>> onSuccess, final Consumer<String> onFailure){
        Call<List<TeacherJunior>> call = juniorEmployeeServiceListener.getTeacherJuniors(teacherID, sessionID);
        call.enqueue(new Callback<List<TeacherJunior>>() {
            @Override
            public void onResponse(Call<List<TeacherJunior>> call, Response<List<TeacherJunior>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TeacherJunior>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }
}
