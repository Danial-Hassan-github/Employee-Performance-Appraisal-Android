package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.ConfidentialEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.DegreeExitEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.DirectorEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.PeerEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.SeniorTeacherEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.SupervisorEvaluation;

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
    @POST("Evaluation/PostSeniorTeacherEvaluation")
    Call<String> postSeniorTeacherEvaluation(@Body List<SeniorTeacherEvaluation> seniorTeacherEvaluations);
    @POST("Evaluation/PostDegreeExitEvaluation")
    Call<String> postDegreeExitEvaluation(@Body List<DegreeExitEvaluation> degreeExitEvaluations);
    @POST("Evaluation/PostConfidentialEvaluation")
    Call<String> postConfidentialEvaluation(@Body List<ConfidentialEvaluation> confidentialEvaluations);
    @POST("Evaluation/PostSupervisorEvaluation")
    Call<String> postSupervisorEvaluation(@Body List<SupervisorEvaluation> supervisorEvaluations);
    @POST("Evaluation/PostDirectorEvaluation")
    Call<String> postDirectorEvaluation(@Body List<DirectorEvaluation> directorEvaluations);
    @GET("Evaluation/IsEvaluated")
    Call<String> isEvaluated(@Query("evaluatorId") int evaluatorId, @Query("evaluateeId") int evaluateeId, @Query("courseId") int courseId, @Query("sessionId") int sessionId, @Query("evaluationType") String evaluationType);
}
