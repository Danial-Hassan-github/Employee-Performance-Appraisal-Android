package com.example.biitemployeeperformanceappraisalsystem.network.interfaces;

import com.example.biitemployeeperformanceappraisalsystem.models.OptionWeightage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface OptionWeightageServiceListener {
    @GET("OptionsWeightage/GetOptionsWeightages")
    Call<List<OptionWeightage>> getOptionsWeightage();
    @PUT("OptionsWeightage/PutOptionsWeightage")
    Call<List<OptionWeightage>> putOptionsWeightage(@Body List<OptionWeightage> optionWeightageList);
}