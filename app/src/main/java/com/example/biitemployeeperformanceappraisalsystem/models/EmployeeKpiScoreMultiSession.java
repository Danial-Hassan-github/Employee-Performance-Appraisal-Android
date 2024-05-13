package com.example.biitemployeeperformanceappraisalsystem.models;

public class EmployeeKpiScoreMultiSession {
    private Session session;
    private EmployeeKpiScore scores;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public EmployeeKpiScore getScores() {
        return scores;
    }

    public void setScores(EmployeeKpiScore scores) {
        this.scores = scores;
    }
}
