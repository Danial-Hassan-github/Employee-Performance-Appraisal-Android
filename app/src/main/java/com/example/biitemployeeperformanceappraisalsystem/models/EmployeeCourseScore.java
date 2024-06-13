package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EmployeeCourseScore {
    private Employee employee;
    private List<CourseScore> performance;

    public List<CourseScore> getPerformance() {
        return performance;
    }

    public void setPerformance(List<CourseScore> performance) {
        this.performance = performance;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
