package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.Date;

public class Task {
    private int id;
    private int assigned_to_id;
    private int assigned_by_id;
    private String task_description;
    private int status;
    private int weightage;
    private Date due_date;
    private int score;
    private Date assigned_date;
    private int session_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssigned_to_id() {
        return assigned_to_id;
    }

    public void setAssigned_to_id(int assigned_to_id) {
        this.assigned_to_id = assigned_to_id;
    }

    public int getAssigned_by_id() {
        return assigned_by_id;
    }

    public void setAssigned_by_id(int assigned_by_id) {
        this.assigned_by_id = assigned_by_id;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWeightage() {
        return weightage;
    }

    public void setWeightage(int weightage) {
        this.weightage = weightage;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getAssigned_date() {
        return assigned_date;
    }

    public void setAssigned_date(Date assigned_date) {
        this.assigned_date = assigned_date;
    }

    public Integer getSession_id() {
        return session_id;
    }

    public void setSession_id(Integer session_id) {
        this.session_id = session_id;
    }
}
