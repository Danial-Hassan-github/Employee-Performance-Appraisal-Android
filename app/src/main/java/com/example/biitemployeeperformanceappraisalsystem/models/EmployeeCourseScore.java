package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EmployeeCourseScore {
    private Employee employee;
    private Course course;
    private double average;
    private List<EmployeeQuestionScore> employeeQuestionScores;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<EmployeeQuestionScore> getEmployeeQuestionScores() {
        return employeeQuestionScores;
    }

    public void setEmployeeQuestionScores(List<EmployeeQuestionScore> employeeQuestionScores) {
        this.employeeQuestionScores = employeeQuestionScores;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
