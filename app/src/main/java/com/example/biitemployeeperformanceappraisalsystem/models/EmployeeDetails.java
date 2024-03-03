package com.example.biitemployeeperformanceappraisalsystem.models;

import com.google.gson.annotations.SerializedName;

public class EmployeeDetails {
    @SerializedName("employee")
    private Employee employee;
    private Designation designation;
    private Department department;
    private EmployeeType employeeType;
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }
}
