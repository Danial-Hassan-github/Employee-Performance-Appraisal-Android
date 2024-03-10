package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.Designation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DesignationServiceListener {
    @GET("Designation/GetDesignations")
    Call<List<Designation>> getDesignations();
}
