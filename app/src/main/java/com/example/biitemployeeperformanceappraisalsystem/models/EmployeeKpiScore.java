package com.example.biitemployeeperformanceappraisalsystem.models;

public class EmployeeKpiScore extends EmpScore {
    private int Kpi_id;
    private String kpi_title;

    public int getKpi_id() {
        return Kpi_id;
    }

    public void setKpi_id(int kpi_id) {
        Kpi_id = kpi_id;
    }

    public String getKpi_title() {
        return kpi_title;
    }

    public void setKpi_title(String kpi_title) {
        this.kpi_title = kpi_title;
    }
}
