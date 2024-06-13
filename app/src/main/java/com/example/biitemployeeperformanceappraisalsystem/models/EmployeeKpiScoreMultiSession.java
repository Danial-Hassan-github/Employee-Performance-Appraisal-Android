package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EmployeeKpiScoreMultiSession {
    private Session session;
    private List<KpiScore> scores;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<KpiScore> getScores() {
        return scores;
    }

    public void setScores(List<KpiScore> scores) {
        this.scores = scores;
    }
}
