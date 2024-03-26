package com.example.biitemployeeperformanceappraisalsystem.models;

public class PeerEvaluation {
    private int evaluator_id;
    private int evaluatee_id;
    private int question_id;
    private int course_id;
    private int session_id;
    private int score;

    public int getEvaluator_id() {
        return evaluator_id;
    }

    public void setEvaluator_id(int evaluator_id) {
        this.evaluator_id = evaluator_id;
    }

    public int getEvaluatee_id() {
        return evaluatee_id;
    }

    public void setEvaluatee_id(int evaluatee_id) {
        this.evaluatee_id = evaluatee_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
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

