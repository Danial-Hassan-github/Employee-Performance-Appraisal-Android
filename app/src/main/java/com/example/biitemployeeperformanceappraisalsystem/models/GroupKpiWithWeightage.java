package com.example.biitemployeeperformanceappraisalsystem.models;

import com.example.biitemployeeperformanceappraisalsystem.models.KPI;

import java.util.List;

public class GroupKpiWithWeightage {
    private KPI kpi;
    public List<SubKpiWeightage> subKpiWeightages;
    private int department_id;
    private int designation_id;
    private int employee_type_id;
    private int employee_id;
    private int weightage;
    private int session_id;

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public List<SubKpiWeightage> getSubKpiWeightages() {
        return subKpiWeightages;
    }

    public void setSubKpiWeightages(List<SubKpiWeightage> subKpiWeightages) {
        this.subKpiWeightages = subKpiWeightages;
    }

    // Getter and Setter for kpi
    public KPI getKpi() {
        return kpi;
    }

    public void setKpi(KPI kpi) {
        this.kpi = kpi;
    }

    // Getter and Setter for department_id
    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    // Getter and Setter for designation_id
    public int getDesignation_id() {
        return designation_id;
    }

    public void setDesignation_id(int designation_id) {
        this.designation_id = designation_id;
    }

    // Getter and Setter for employee_type_id
    public int getEmployee_type_id() {
        return employee_type_id;
    }

    public void setEmployee_type_id(int employee_type_id) {
        this.employee_type_id = employee_type_id;
    }

    // Getter and Setter for weightage
    public int getWeightage() {
        return weightage;
    }

    public void setWeightage(int weightage) {
        this.weightage = weightage;
    }

    // Getter and Setter for session_id
    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }
}
