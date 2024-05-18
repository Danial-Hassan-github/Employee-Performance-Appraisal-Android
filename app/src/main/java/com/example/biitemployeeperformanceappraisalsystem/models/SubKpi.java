package com.example.biitemployeeperformanceappraisalsystem.models;

public class SubKpi {
    private int id;
    private String name;
    private SubKpiWeightage subKpiWeightage;

    public SubKpiWeightage getSubKpiWeightage() {
        return subKpiWeightage;
    }

    public void setSubKpiWeightage(SubKpiWeightage subKpiWeightage) {
        this.subKpiWeightage = subKpiWeightage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
