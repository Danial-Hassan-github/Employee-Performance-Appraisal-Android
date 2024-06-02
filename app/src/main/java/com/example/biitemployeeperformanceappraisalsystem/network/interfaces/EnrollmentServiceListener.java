package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EnrollmentServiceListener {
    @Multipart
    @POST("Enrollment/UploadFile")
    Call<ResponseBody> uploadEnrollmentFile(@Part MultipartBody.Part file);
}
