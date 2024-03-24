package com.example.biitemployeeperformanceappraisalsystem.models;

public class TaskWithEmployees {
    private Task task;
    private Employee assigned_to;
    private Employee assigned_by;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Employee getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(Employee assigned_to) {
        this.assigned_to = assigned_to;
    }

    public Employee getAssigned_by() {
        return assigned_by;
    }

    public void setAssigned_by(Employee assigned_by) {
        this.assigned_by = assigned_by;
    }
}
