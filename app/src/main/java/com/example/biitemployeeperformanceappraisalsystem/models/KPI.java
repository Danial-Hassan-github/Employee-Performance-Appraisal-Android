package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class KPI {
    private int group_kpi_id;
    private int id;
    private String name;
    private KpiWeightage kpiWeightage;
    private List<SubKpiWeightage> subKpiWeightages;

    public int getGroup_kpi_id() {
        return group_kpi_id;
    }

    public void setGroup_kpi_id(int group_kpi_id) {
        this.group_kpi_id = group_kpi_id;
    }

    public List<SubKpiWeightage> getSubKpiWeightages() {
        return subKpiWeightages;
    }

    public void setSubKpiWeightages(List<SubKpiWeightage> subKpiWeightages) {
        this.subKpiWeightages = subKpiWeightages;
    }

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
