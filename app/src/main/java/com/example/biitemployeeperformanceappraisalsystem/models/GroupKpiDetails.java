package com.example.biitemployeeperformanceappraisalsystem.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GroupKpiDetails {
    private GroupKpi groupKpi;
    private List<KPI> kpiList = new ArrayList<>();

    public GroupKpi getGroupKpi() {
        return groupKpi;
    }

    public void setGroupKpi(GroupKpi groupKpi) {
        this.groupKpi = groupKpi;
    }

    public List<KPI> getKpiList() {
        return kpiList;
    }

    public void setKpiList(List<KPI> kpiList) {
        this.kpiList = kpiList;
    }
}
