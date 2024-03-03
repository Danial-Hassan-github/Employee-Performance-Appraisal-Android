package com.example.biitemployeeperformanceappraisalsystem.models;

import com.google.gson.annotations.SerializedName;

public class Student{
    private int id;
    private String name;
    private String arid_no;
    private int semester;
    private String section;
    private Double cgpa;
    private String discipline;
    private String password;

    // Constructor
    public Student() {
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

    public String getAridNo() {
        return arid_no;
    }

    public void setAridNo(String aridNo) {
        this.arid_no = aridNo;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

