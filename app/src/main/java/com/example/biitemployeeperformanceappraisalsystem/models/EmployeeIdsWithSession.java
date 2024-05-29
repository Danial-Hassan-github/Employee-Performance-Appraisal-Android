package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class EmployeeIdsWithSession {
    private List<Integer> employeeIds;
    private int session_id;

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
