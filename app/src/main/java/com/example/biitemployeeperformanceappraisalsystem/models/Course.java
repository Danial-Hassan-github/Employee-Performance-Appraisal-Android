package com.example.biitemployeeperformanceappraisalsystem.models;

public class Course {
    private int id;
    private String title;
    private String course_code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course(String title, String course_code) {
        this.title = title;
        this.course_code = course_code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return title;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCode() {
        return course_code;
    }
}
