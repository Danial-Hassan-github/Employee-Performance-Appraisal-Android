package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class MultiEmployeeCoursePerformanceRequest {
    private List<Integer> employeeIds;
    private int courseId;
    private int sessionId;
    public MultiEmployeeCoursePerformanceRequest()
    {

    }

    public MultiEmployeeCoursePerformanceRequest(int courseId, int sessionId, List<Integer> employeeIds)
    {
        this.courseId = courseId;
        this.sessionId = sessionId;
        this.employeeIds = employeeIds;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
