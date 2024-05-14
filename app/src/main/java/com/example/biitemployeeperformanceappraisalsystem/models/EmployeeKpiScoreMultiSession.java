package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EmployeeKpiScoreMultiSession {
    private Session session;
    private List<EmployeeKpiScore> scores;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<EmployeeKpiScore> getScores() {
        return scores;
    }

    public void setScores(List<EmployeeKpiScore> scores) {
        this.scores = scores;
    }
}
