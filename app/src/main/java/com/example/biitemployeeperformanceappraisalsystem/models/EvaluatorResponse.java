package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EvaluatorResponse {
    private List<Student> students;
    private List<Employee> employees;

    // Getters and setters
    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

