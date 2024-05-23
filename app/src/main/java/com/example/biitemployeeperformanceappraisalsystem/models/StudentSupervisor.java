package com.example.biitemployeeperformanceappraisalsystem.models;

public class StudentSupervisor {
    private int id;
    private int student_id;
    private int supervisor_id;

    public StudentSupervisor() {

    }

    public StudentSupervisor(int id, int student_id, int supervisor_id) {
        this.id = id;
        this.student_id = student_id;
        this.supervisor_id = supervisor_id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getStudentId() {
        return student_id;
    }

    public int getSupervisorId() {
        return supervisor_id;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(int studentId) {
        this.student_id = studentId;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisor_id = supervisorId;
    }
}

