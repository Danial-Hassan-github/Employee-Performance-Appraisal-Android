package com.example.biitemployeeperformanceappraisalsystem.models;

public class TaskWithRole {
    private Task task;
    private EmployeeRole role;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }
}
