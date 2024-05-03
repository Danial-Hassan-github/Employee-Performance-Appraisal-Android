package com.example.biitemployeeperformanceappraisalsystem.models;

public class EmployeeKpiScore {
    private int kpi_id;
    private int employee_id;
    private int session_id;
    private int score;
    private int total_score;

    public int getKpi_id() {
        return kpi_id;
    }

    public void setKpi_id(int kpi_id) {
        this.kpi_id = kpi_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }
}
