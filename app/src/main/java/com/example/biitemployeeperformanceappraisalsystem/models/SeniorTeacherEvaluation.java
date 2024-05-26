package com.example.biitemployeeperformanceappraisalsystem.models;

public class SeniorTeacherEvaluation {
    private int senior_teacher_id;
    private int junior_teacher_id;
    private int course_id;
    private int session_id;
    private int question_id;
    private Integer score; // Nullable in Java

    // Getter and Setter for seniorTeacherId
    public int getSeniorTeacherId() {
        return senior_teacher_id;
    }

    public void setSeniorTeacherId(int senior_teacher_id) {
        this.senior_teacher_id = senior_teacher_id;
    }

    // Getter and Setter for juniorTeacherId
    public int getJuniorTeacherId() {
        return junior_teacher_id;
    }

    public void setJuniorTeacherId(int junior_teacher_id) {
        this.junior_teacher_id = junior_teacher_id;
    }

    // Getter and Setter for courseId
    public int getCourseId() {
        return course_id;
    }

    public void setCourseId(int course_id) {
        this.course_id = course_id;
    }

    // Getter and Setter for sessionId
    public int getSessionId() {
        return session_id;
    }

    public void setSessionId(int session_id) {
        this.session_id = session_id;
    }

    // Getter and Setter for questionId
    public int getQuestionId() {
        return question_id;
    }

    public void setQuestionId(int question_id) {
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

