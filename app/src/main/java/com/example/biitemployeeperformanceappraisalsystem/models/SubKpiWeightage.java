package com.example.biitemployeeperformanceappraisalsystem.models;

public class SubKpiWeightage {
    private Integer kpi_id;
    private int sub_kpi_id;
    private int session_id;
    private int weightage;

    public int getWeightage() {
        return weightage;
    }

    public void setWeightage(int weightage) {
        this.weightage = weightage;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getSub_kpi_id() {
        return sub_kpi_id;
    }

    public void setSub_kpi_id(int sub_kpi_id) {
        this.sub_kpi_id = sub_kpi_id;
    }

    public Integer getKpi_id() {
        return kpi_id;
    }

    public void setKpi_id(Integer kpi_id) {
        this.kpi_id = kpi_id;
    }
}
