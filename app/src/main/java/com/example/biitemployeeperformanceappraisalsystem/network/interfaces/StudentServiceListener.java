package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.ConfidentialEvaluatorStudent;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StudentServiceListener {
    @GET("Student/GetStudentsBySection")
    Call<List<Student>> getStudentsBySection(@Query("semester") int semester, @Query("section") String section);
    @GET("Student/GetStudentSessionTeacher")
    Call<List<Employee>> getStudentSessionTeacher(@Query("studentID") int studentID, @Query("sessionID") int sessionID);
    @POST("Student/PostConfidentialEvaluatorStudents")
    Call<String> postConfidentialEvaluatorStudents(@Body List<ConfidentialEvaluatorStudent> confidentialEvaluatorStudents);
}
