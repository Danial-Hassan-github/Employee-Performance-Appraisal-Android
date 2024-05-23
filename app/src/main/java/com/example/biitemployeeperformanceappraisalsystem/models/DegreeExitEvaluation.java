package com.example.biitemployeeperformanceappraisalsystem.models;

public class DegreeExitEvaluation {
    private int id;
    private int student_id;
    private int teacher_id;
    private int session_id;
    private int question_id;
    private Integer score; // Nullable in Java

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for student_id
    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    // Getter and Setter for teacher_id
    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    // Getter and Setter for session_id
    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    // Getter and Setter for question_id
    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    // Getter and Setter for score
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}

