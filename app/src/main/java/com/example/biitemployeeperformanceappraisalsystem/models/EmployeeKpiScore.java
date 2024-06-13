package com.example.biitemployeeperformanceappraisalsystem.models;

import android.hardware.lights.LightsManager;

import java.util.List;

public class EmployeeKpiScore {
    private Employee employee;
    private List<KpiScore> kpiScores;

    public List<KpiScore> getKpiScores() {
        return kpiScores;
    }

    public void setKpiScores(List<KpiScore> kpiScores) {
        this.kpiScores = kpiScores;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
