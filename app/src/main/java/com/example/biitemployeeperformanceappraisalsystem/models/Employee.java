package com.example.biitemployeeperformanceappraisalsystem.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Employee{
    private int id;
    private String name;
    private String email;
    private String password;
    private Integer employeeTypeId; // Use Integer instead of int for nullable types
    private Double salary; // Use Double instead of decimal for compatibility
    private Date doj; // Use java.util.Date for DateTime
    private Boolean deleted; // Use Boolean instead of bool for nullable types
    private Integer departmentId; // Use Integer instead of int for nullable types
    private Integer designationId; // Use Integer instead of int for nullable types

    // Constructor
    public Employee() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(Integer employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getDoj() {
        return doj;
    }

    public void setDoj(Date doj) {
        this.doj = doj;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Integer designationId) {
        this.designationId = designationId;
    }
}

