package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class DepartmentKPI {
    private int departmentId;
    private List<KPI> kpiList;

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public List<KPI> getKpiList() {
        return kpiList;
    }

    public void setKpiList(List<KPI> kpiList) {
        this.kpiList = kpiList;
    }
}
