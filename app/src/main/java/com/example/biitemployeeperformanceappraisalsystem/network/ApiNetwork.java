package com.example.biitemployeeperformanceappraisalsystem.network;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.example.biitemployeeperformanceappraisalsystem.models.Task;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiNetwork {
    @GET("Login/Login")
    Call<Student> LoginStudent(@Query("emailOrAridNo") String emailOrAridNo, @Query("password") String password);
    @GET("Login/Login")
    Call<EmployeeDetails> LoginEmployee(@Query("emailOrAridNo") String emailOrAridNo, @Query("password") String password);
    @GET("Employee/GetEmployees")
    Call<List<EmployeeDetails>> getEmployees();
    @GET("Employee/GetEmployeesWithKpiScores")
    Call<List<EmployeeDetailsScore>> GetEmployeesWithKpiScores();
    @GET("Session/GetSessions")
    Call<List<Session>> GetSession();
    @GET("Task/getTasksDetail")
    Call<List<TaskDetails>> getTasks();
    @GET("Task/GetEmployeeTasks")
    Call<List<TaskDetails>> GetEmployeeTasks(@Query("employeeID") int employeeID);
    @GET("Questionaire/GetConfidentialQuestions")
    Call<List<Question>> GetConfidentialQuestions();
    @GET("Questionaire/GetQuestionnaireTypes")
    Call<List<QuestionnaireType>> getQuestionnaireTypes();
}
