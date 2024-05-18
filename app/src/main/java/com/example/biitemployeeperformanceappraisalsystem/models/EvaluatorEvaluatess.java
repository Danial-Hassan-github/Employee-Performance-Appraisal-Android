package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EvaluatorEvaluatess {
    private int evaluator_id;
    private int session_id;
    private List<Integer> evaluatee_ids;

    // Getter for evaluator_id
    public int getEvaluator_id() {
        return evaluator_id;
    }

    // Setter for evaluator_id
    public void setEvaluator_id(int evaluator_id) {
        this.evaluator_id = evaluator_id;
    }

    // Getter for session_id
    public int getSession_id() {
        return session_id;
    }

    // Setter for session_id
    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    // Getter for evaluatee_ids
    public List<Integer> getEvaluatee_ids() {
        return evaluatee_ids;
    }

    // Setter for evaluatee_ids
    public void setEvaluatee_ids(List<Integer> evaluatee_ids) {
        this.evaluatee_ids = evaluatee_ids;
    }
}
