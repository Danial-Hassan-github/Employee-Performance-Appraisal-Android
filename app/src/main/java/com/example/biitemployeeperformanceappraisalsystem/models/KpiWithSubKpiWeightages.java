package com.example.biitemployeeperformanceappraisalsystem.models;

import java.util.List;

public class KpiWithSubKpiWeightages {
    private KPI kpi;
    private KpiWeightage weightage;
    private List<SubKpiWeightage> subKpiWeightages;

    public List<SubKpiWeightage> getSubKpiWeightages() {
        return subKpiWeightages;
    }

    public void setSubKpiWeightages(List<SubKpiWeightage> subKpiWeightages) {
        this.subKpiWeightages = subKpiWeightages;
    }

    public KpiWeightage getWeightage() {
        return weightage;
    }

    public void setWeightage(KpiWeightage weightage) {
        this.weightage = weightage;
    }

    public KPI getKpi() {
        return kpi;
    }

    public void setKpi(KPI kpi) {
        this.kpi = kpi;
    }
}
