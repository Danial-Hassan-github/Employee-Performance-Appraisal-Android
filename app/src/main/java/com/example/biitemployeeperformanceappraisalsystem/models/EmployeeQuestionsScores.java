package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EmployeeQuestionsScores {
    private Employee employee;
    List<QuestionScore> questionScores;

    public List<QuestionScore> getQuestionScores() {
        return questionScores;
    }

    public void setQuestionScores(List<QuestionScore> questionScores) {
        this.questionScores = questionScores;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
