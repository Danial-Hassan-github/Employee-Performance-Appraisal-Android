package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.OptionWeightage;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.OptionWeightageServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionWeightageService {
    OptionWeightageServiceListener optionWeightageServiceListener;
    Context context;
    public OptionWeightageService(Context context){
        optionWeightageServiceListener = RetrofitClient.getRetrofitInstance().create(OptionWeightageServiceListener.class);
        this.context=context;
    }
    public void getOptionsWeightage(final Consumer<List<OptionWeightage>> onSuccess, final Consumer<String> onFailure){
        Call<List<OptionWeightage>> optionsWeightage = optionWeightageServiceListener.getOptionsWeightage();
        optionsWeightage.enqueue(new Callback<List<OptionWeightage>>() {
            @Override
            public void onResponse(Call<List<OptionWeightage>> call, Response<List<OptionWeightage>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<OptionWeightage>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching options weightage");
            }
        });
    }

    public void putOptionsWeightage(List<OptionWeightage> optionWeightageList, final Consumer<List<OptionWeightage>> onSuccess, final Consumer<String> onFailure){
        Call<List<OptionWeightage>> optionsWeightageCall = optionWeightageServiceListener.putOptionsWeightage(optionWeightageList);
        optionsWeightageCall.enqueue(new Callback<List<OptionWeightage>>() {
            @Override
            public void onResponse(Call<List<OptionWeightage>> call, Response<List<OptionWeightage>> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                    Toast.makeText(context, "Weightages updated successfully", Toast.LENGTH_SHORT).show();
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<OptionWeightage>> call, Throwable t) {
                onFailure.accept(t.getMessage());
            }
        });
    }
}
