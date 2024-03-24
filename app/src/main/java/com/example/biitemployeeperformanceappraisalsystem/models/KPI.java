package com.example.biitemployeeperformanceappraisalsystem.models;

public class KPI {
    private int id;
    private String name;
    private KpiWeightage kpiWeightage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KpiWeightage getKpiWeightage() {
        return kpiWeightage;
    }

    public void setKpiWeightage(KpiWeightage kpiWeightage) {
        this.kpiWeightage = kpiWeightage;
    }
}
