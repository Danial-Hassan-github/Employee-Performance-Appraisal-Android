package com.example.biitemployeeperformanceappraisalsystem.models;

public class StudentSupervisor {
    private int id;
    private int studentId;
    private int supervisorId;

    public StudentSupervisor() {

    }

    public StudentSupervisor(int id, int studentId, int supervisorId) {
        this.id = id;
        this.studentId = studentId;
        this.supervisorId = supervisorId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getSupervisorId() {
        return supervisorId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }
}

