package com.example.biitemployeeperformanceappraisalsystem.models;

public class SupervisorEvaluation {
    private int supervisor_id;
    private int subordinate_id;
    private int question_id;
    private int session_id;
    private int score;

    public int getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(int supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public int getSubordinate_id() {
        return subordinate_id;
    }

    public void setSubordinate_id(int subordinate_id) {
        this.subordinate_id = subordinate_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
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
}
