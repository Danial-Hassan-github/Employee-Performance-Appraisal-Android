package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EmployeeCoursesPerformanceRequest {
    private int employeeID;
    private int sessionID;
    private List<Integer> coursesIds;

    public EmployeeCoursesPerformanceRequest(){

    }

    public EmployeeCoursesPerformanceRequest(int employeeID, int sessionID, List<Integer> coursesIds) {
        this.employeeID = employeeID;
        this.sessionID = sessionID;
        this.coursesIds = coursesIds;
    }

    // Getters and setters
    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public List<Integer> getCoursesIds() {
        return coursesIds;
    }

    public void setCoursesIds(List<Integer> coursesIds) {
        this.coursesIds = coursesIds;
    }
}

