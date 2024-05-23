package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.Objects;

public class Designation {
    private int id;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Designation that = (Designation) o;
        return id == that.id;  // Compare by ID or other unique field
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
