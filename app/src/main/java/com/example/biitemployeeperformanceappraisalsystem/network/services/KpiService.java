package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;

import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.KpiServiceListener;

public class KpiService {
    Context context;
    KpiServiceListener kpiServiceListener;
    public KpiService(Context context){
        kpiServiceListener=RetrofitClient.getRetrofitInstance().create(KpiServiceListener.class);
        this.context=context;
    }
}
