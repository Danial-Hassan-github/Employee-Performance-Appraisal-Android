package com.example.biitemployeeperformanceappraisalsystem.models;

public class SubKpiScore extends EmpScore{
    private int subkpi_id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubKpi_id() {
        return subkpi_id;
    }

    public void setSubKpi_id(int subkpi_id) {
        this.subkpi_id = subkpi_id;
    }
}
