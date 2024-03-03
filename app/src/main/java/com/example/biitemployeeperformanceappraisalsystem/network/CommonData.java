package com.example.biitemployeeperformanceappraisalsystem.network;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.example.biitemployeeperformanceappraisalsystem.student.StudentMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonData {
    ApiNetwork apiNetwork;
    RetrofitClient retrofitClient;
    Context context;
    public CommonData(Context context){
        retrofitClient = new RetrofitClient();
        apiNetwork=retrofitClient.getRetrofitInstance().create(ApiNetwork.class);
        this.context=context;
    }

    public void loginStudent(String emailOrAridNo, String password){
        Call<Student> loginCall = apiNetwork.LoginStudent(emailOrAridNo, password);
        loginCall.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    // Check the type of user and navigate accordingly
                    Student user = response.body(); // You may need to cast this to the appropriate user type
                    context.startActivity(new Intent(context, StudentMainActivity.class));
                }else {
                    // Handle unsuccessful login response
                    // Show appropriate error message
                }
            }
            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                // Handle failure of login request
                // Show appropriate error message
            }
        });
    }

    public void loginEmployee(String emailOrAridNo, String password){
        Call<EmployeeDetails> loginCall = apiNetwork.LoginEmployee(emailOrAridNo.toString(), password.toString());
        loginCall.enqueue(new Callback<EmployeeDetails>() {
            @Override
            public void onResponse(Call<EmployeeDetails> call, Response<EmployeeDetails> response) {
                if (response.isSuccessful()) {
                    // Check the type of user and navigate accordingly
                    EmployeeDetails user = response.body(); // You may need to cast this to the appropriate user type
                    if (user!=null){
                        if (user.getDesignation().getName().equalsIgnoreCase("Director")){
                            context.startActivity(new Intent(context, DirectorMainActivity.class));
                        } else if (user.getDesignation().getName().equalsIgnoreCase("HOD")) {
                            context.startActivity(new Intent(context, HodMainActivity.class));
                        } else if (user.getDesignation().getName().equalsIgnoreCase("Teacher")) {

                        }
                    }
                }else {
                    // Handle unsuccessful login response
                    Toast.makeText(context,"Login failed",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<EmployeeDetails> call, Throwable t) {
                // Handle failure of login request
                Toast.makeText(context,"Login failed",Toast.LENGTH_LONG);
            }
        });
    }

    public void getSessions(final Consumer<List<Session>> onSuccess, final Consumer<String> onFailure) {
        Call<List<Session>> sessionCall = apiNetwork.GetSession();
        sessionCall.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching sessions");
            }
        });
    }

    public void populateSpinner(List<Session> sessionList, Spinner spinner) {
        if (sessionList != null && !sessionList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getSessionTitles(sessionList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Session list is empty", Toast.LENGTH_LONG).show();
        }
    }

    // Method to get session titles from the list of sessions
    public String[] getSessionTitles(List<Session> sessionList) {
        String[] titles = new String[sessionList.size()];
        for (int i = 0; i < sessionList.size(); i++) {
            titles[i] = sessionList.get(i).getTitle();
        }
        return titles;
    }

    public void getSessionPerformance(){

    }

    public void getSessionsPerformance(){

    }
}
