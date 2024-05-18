package com.example.biitemployeeperformanceappraisalsystem.models;

public class KpiWeightage {
    private int id;
    private Integer group_kpi_id;
    private int kpi_id;
    private int session_id;
    private int weightage;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroup_kpi_id() {
        return group_kpi_id;
    }

    public void setGroup_kpi_id(int group_kpi_id) {
        this.group_kpi_id = group_kpi_id;
    }

    public int getKpi_id() {
        return kpi_id;
    }

    public void setKpi_id(int kpi_id) {
        this.kpi_id = kpi_id;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getWeightage() {
        return weightage;
    }

    public void setWeightage(int weightage) {
        this.weightage = weightage;
    }
}
