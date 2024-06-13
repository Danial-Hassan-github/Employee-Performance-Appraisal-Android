package com.example.biitemployeeperformanceappraisalsystem.models;

public class SubKpiScore extends EmpScore{
    private int subKpi_id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubKpi_id() {
        return subKpi_id;
    }

    public void setSubKpi_id(int subKpi_id) {
        this.subKpi_id = subKpi_id;
    }
}
