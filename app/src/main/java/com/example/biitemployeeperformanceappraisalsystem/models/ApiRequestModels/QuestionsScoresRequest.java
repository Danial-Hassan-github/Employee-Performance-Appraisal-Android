package com.example.biitemployeeperformanceappraisalsystem.models.ApiRequestModels;

import java.util.List;

public class QuestionsScoresRequest {
    private List<Integer> employeeIDs;
    private int sessionID;
    private int evaluationTypeID;

    // Getters and Setters
    public List<Integer> getEmployeeIDs() {
        return employeeIDs;
    }

    public void setEmployeeIDs(List<Integer> employeeIDs) {
        this.employeeIDs = employeeIDs;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getEvaluationTypeID() {
        return evaluationTypeID;
    }

    public void setEvaluationTypeID(int evaluationTypeID) {
        this.evaluationTypeID = evaluationTypeID;
    }
}

