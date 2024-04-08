package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.PeerEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EvaluationServiceListener {
    @POST("Evaluation/PostStudentEvaluation")
    Call<String> postStudentEvaluation(@Body List<StudentEvaluation> studentEvaluation);
    @POST("Evaluation/PostPeerEvaluation")
    Call<String> postPeerEvaluation(@Body List<PeerEvaluation> peerEvaluation);
    @GET("Evaluation/IsEvaluated")
    Call<String> isEvaluated(@Query("studentId") int studentId, @Query("teacherId") int teacherId, @Query("courseId") int courseId, @Query("sessionId") int sessionId, @Query("evaluationType") String evaluationType);
}
