package com.example.biitemployeeperformanceappraisalsystem.models;

public class EmployeeKpi {
    private Employee employee;
    private KPI kpi;
    private int group_kpi_id;
    private int weightage;
    private int session_id;

    public int getGroup_kpi_id() {
        return group_kpi_id;
    }

    public void setGroup_kpi_id(int group_kpi_id) {
        this.group_kpi_id = group_kpi_id;
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

    public KPI getKpi() {
        return kpi;
    }

    public void setKpi(KPI kpi) {
        this.kpi = kpi;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
