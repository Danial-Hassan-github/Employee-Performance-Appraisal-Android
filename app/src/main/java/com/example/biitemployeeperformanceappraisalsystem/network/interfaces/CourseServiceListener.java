package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CourseServiceListener {
    @GET("Course/GetCourses")
    Call<List<Course>> getCourses();
    @GET("Course/GetTeacherCourses")
    Call<List<Course>> getTeacherCourses(@Query("teacherID") int teacherID, @Query("sessionID") int sessionID);
    @GET("Course/GetTeacherCourses")
    Call<List<Course>> getTeacherCourses(@Query("teacherID") int teacherID);
    @GET("Course/GetStudentCourses")
    Call<List<Course>> getStudentCourses(@Query("studentID") int studentID,@Query("sessionID") int sessionID);
    @GET("Course/GetCourseTeachers")
    Call<List<Employee>> getCourseTeachers(@Query("studentID") int studentID, @Query("courseID") int courseID, @Query("sessionID") int sessionID);
}
