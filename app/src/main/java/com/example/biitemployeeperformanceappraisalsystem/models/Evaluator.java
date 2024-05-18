package com.example.biitemployeeperformanceappraisalsystem.models;

public class Evaluator {
    private int id;
    private int session_id;
    private int evaluatee_id;

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for session_id
    public int getSession_id() {
        return session_id;
    }

    // Setter for session_id
    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    // Getter for evaluatee_id
    public int getEvaluatee_id() {
        return evaluatee_id;
    }

    // Setter for evaluatee_id
    public void setEvaluatee_id(int evaluatee_id) {
        this.evaluatee_id = evaluatee_id;
    }
}
