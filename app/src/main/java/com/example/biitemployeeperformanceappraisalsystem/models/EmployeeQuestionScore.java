package com.example.biitemployeeperformanceappraisalsystem.models;

public class EmployeeQuestionScore {
    private Question question;
    private int obtainedScore;
    private int totalScore;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getObtainedScore() {
        return obtainedScore;
    }

    public void setObtainedScore(int obtainedScore) {
        this.obtainedScore = obtainedScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
