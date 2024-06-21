package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.admin.AdminMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.LoginServiceListener;
import com.example.biitemployeeperformanceappraisalsystem.student.StudentMainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginService {
    LoginServiceListener loginServiceListener;
    SharedPreferencesManager sharedPreferencesManager;
    SessionService sessionService;
    Context context;

    public LoginService(Context context) {
        loginServiceListener = RetrofitClient.getRetrofitInstance().create(LoginServiceListener.class);
        this.context = context;
        sharedPreferencesManager = new SharedPreferencesManager(context);
        sessionService = new SessionService(context);
        // sessionService.getCurrentSession();
    }

    public void loginStudent(String emailOrAridNo, String password) {
        Call<Student> loginCall = loginServiceListener.LoginStudent(emailOrAridNo, password);
        loginCall.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    // Check the type of user and navigate accordingly
                    Student user = response.body(); // You may need to cast this to the appropriate user type
                    sharedPreferencesManager.saveStudentUserDetails(user);
                    context.startActivity(new Intent(context, StudentMainActivity.class));
                } else {
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

    public void loginEmployee(String emailOrAridNo, String password) {
        Call<EmployeeDetails> loginCall = loginServiceListener.LoginEmployee(emailOrAridNo.toString(), password.toString());
        loginCall.enqueue(new Callback<EmployeeDetails>() {
            @Override
            public void onResponse(Call<EmployeeDetails> call, Response<EmployeeDetails> response) {
                if (response.isSuccessful()) {
                    // Check the type of user and navigate accordingly
                    EmployeeDetails user = response.body(); // You may need to cast this to the appropriate user type
                    if (user != null) {
                        sharedPreferencesManager.saveEmployeeUserDetails(user);
                        if (user.getDesignation().getName().equalsIgnoreCase("Director")) {
                            context.startActivity(new Intent(context, DirectorMainActivity.class));
                        } else if (user.getDesignation().getName().equalsIgnoreCase("HOD")) {
                            context.startActivity(new Intent(context, HodMainActivity.class));
                        } else if (user.getDesignation().getName().equalsIgnoreCase("Teacher")) {
                            context.startActivity(new Intent(context, FacultyMain.class));
                        } else if (user.getDesignation().getName().equalsIgnoreCase("Administrative") && user.getEmployeeType().getTitle().equalsIgnoreCase("Administrative Staff")) {
                            context.startActivity(new Intent(context, AdminMainActivity.class));
                        }
                    }
                } else {
                    // Handle unsuccessful login response
                    Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EmployeeDetails> call, Throwable t) {
                // Handle failure of login request
                Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
