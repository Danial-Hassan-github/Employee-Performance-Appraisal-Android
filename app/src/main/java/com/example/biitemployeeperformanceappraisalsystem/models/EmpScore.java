package com.example.biitemployeeperformanceappraisalsystem.models;

public class EmpScore {
    private int employee_id;
    private int score;
    private int weightage;
    private String session_title;

    public String getSession_title() {
        return session_title;
    }

    public void setSession_title(String session_title) {
        this.session_title = session_title;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWeightage() {
        return weightage;
    }

    public void setWeightage(int weightage) {
        this.weightage = weightage;
    }
}
