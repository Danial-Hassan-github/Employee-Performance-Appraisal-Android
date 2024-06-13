package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EmployeeSubKpiScore {
    private Employee employee;
    private List<SubKpiScore> subKpiPerformances;

    public List<SubKpiScore> getSubKpiPerformances() {
        return subKpiPerformances;
    }

    public void setSubKpiPerformances(List<SubKpiScore> subKpiPerformances) {
        this.subKpiPerformances = subKpiPerformances;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
