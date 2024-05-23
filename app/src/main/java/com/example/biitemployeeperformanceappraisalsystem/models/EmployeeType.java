package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.Objects;

public class EmployeeType {
        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeType that = (EmployeeType) o;
        return id == that.id;  // Compare by ID or other unique field
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    }

