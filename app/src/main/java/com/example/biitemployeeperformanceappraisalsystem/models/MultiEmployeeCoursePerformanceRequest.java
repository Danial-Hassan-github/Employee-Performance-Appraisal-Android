package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class MultiEmployeeCoursePerformanceRequest {
    private List<Integer> employeeIds;
    private List<Integer> courseIds;
    private int sessionId;
    public MultiEmployeeCoursePerformanceRequest()
    {

    }

    public MultiEmployeeCoursePerformanceRequest(List<Integer> courseIds, int sessionId, List<Integer> employeeIds)
    {
        this.courseIds = courseIds;
        this.sessionId = sessionId;
        this.employeeIds = employeeIds;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public List<Integer> getCourseId() {
        return courseIds;
    }

    public void setCourseId(List<Integer> courseIds) {
        this.courseIds = courseIds;
    }

    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
