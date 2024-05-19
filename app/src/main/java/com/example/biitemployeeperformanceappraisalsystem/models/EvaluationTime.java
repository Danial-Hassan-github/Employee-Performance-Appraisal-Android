package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.Date;

public class EvaluationTime {
    private int id;
    private int session_id;
    private String evaluation_type;
    private Date start_time;
    private Date end_time;

    // Default constructor
    public EvaluationTime() {
    }

    // Parameterized constructor
    public EvaluationTime(int id, int session_id, String evaluation_type, Date start_time, Date end_time) {
        this.id = id;
        this.session_id = session_id;
        this.evaluation_type = evaluation_type;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    // Getters and setters for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for session_id
    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    // Getters and setters for evaluation_type
    public String getEvaluation_type() {
        return evaluation_type;
    }

    public void setEvaluation_type(String evaluation_type) {
        this.evaluation_type = evaluation_type;
    }

    // Getters and setters for start_time
    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    // Getters and setters for end_time
    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}

