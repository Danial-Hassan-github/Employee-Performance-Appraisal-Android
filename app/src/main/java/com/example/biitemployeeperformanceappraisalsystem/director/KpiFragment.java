package com.example.biitemployeeperformanceappraisalsystem.director;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.CommonMethods;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.KpiService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KpiFragment extends Fragment {
    int sessionId;
    KpiService kpiService;
    SharedPreferencesManager sharedPreferencesManager;
    List<GroupKpiDetails> groupKpiDetailsList;
    List<Session> sessionList;
    Spinner sessionSpinner;
    Button btnAddKpi, btnAddGroupKpi, btnAddIndividualKpi;
    ArrayList<Float> pieChartValues;
    ArrayList<String> pieChartTitles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kpi, container, false);

        sessionSpinner = view.findViewById(R.id.spinner_session);
        btnAddKpi = view.findViewById(R.id.btn_add_general_kpi);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
        btnAddKpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directorMainActivity.replaceFragment(new AddKpiFragment());
            }
        });

        SessionService sessionService = new SessionService(view.getContext());
        sessionService.getSessions(sessions -> {
                    sessionList = sessions;
                    sessionService.populateSpinner(sessions, sessionSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                Session selectedSession = sessionList.get(position);
                sessionId = selectedSession.getId();
                showKpiGraph(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void showKpiGraph(View view) {
        ViewGroup container = view.findViewById(R.id.chart_container);
        kpiService = new KpiService(getContext());
        pieChartValues = new ArrayList<>();
        pieChartTitles = new ArrayList<>();
        kpiService.getSessionKpis(
                sessionId,
                groupKpiDetails -> {
                    groupKpiDetailsList = groupKpiDetails;
                    if (groupKpiDetailsList != null) {
                        for (GroupKpiDetails details : groupKpiDetailsList) {
                            if (details.getGroupKpi() != null) {
                                String department = "";
                                String designation = "";
                                String employeeType = "";
                                String employeeName = "";
                                if (details.getGroupKpi().getDesignation() != null) {
                                    designation = details.getGroupKpi().getDesignation().getName();
                                }
                                if (details.getGroupKpi().getDepartment() != null) {
                                    department = details.getGroupKpi().getDepartment().getName();
                                }
                                if (details.getGroupKpi().getEmployeeType() != null) {
                                    employeeType = details.getGroupKpi().getEmployeeType().getTitle();
                                }
                                TextView textView = new TextView(getContext());
                                if (details.getGroupKpi().getEmployee() != null){
                                    employeeName = details.getGroupKpi().getEmployee().getName();
                                    textView.setText(employeeName);
                                }
                                else {
                                    textView.setText(employeeType + " " + department + " " + designation);
                                }
                                textView.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                ));
                                // Set background color
                                textView.setBackgroundColor(getResources().getColor(R.color.brown));
                                // Set text color
                                textView.setTextColor(Color.WHITE);
                                // Add the TextView to the container
                                container.addView(textView);
                            }

                            PieChart pieChart = new PieChart(getContext());
                            pieChart.getDescription().setTextColor(Color.TRANSPARENT);
                            int chartHeight = (int) getResources().getDimension(R.dimen.pie_chart_height);
                            pieChart.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    chartHeight
                            ));

                            ArrayList<PieEntry> entries = new ArrayList<>();
                            float totalScore = 0;
                            for (KPI kpi : details.getKpiList()) {
                                float weightage = kpi.getKpiWeightage().getWeightage();
                                totalScore += weightage;
                                entries.add(new PieEntry(weightage, kpi.getName()));
                                pieChartValues.add(weightage);
                                pieChartTitles.add(kpi.getName());
                            }

                            // Calculate remaining percentage
                            float remainingPercentage = 100 - totalScore;
                            if (remainingPercentage > 0) {
                                entries.add(new PieEntry(remainingPercentage, ""));
                            }

                            PieDataSet dataSet = new PieDataSet(entries, "");
                            dataSet.setValueTextSize(20f);

                            pieChart.setHoleRadius(10);
                            pieChart.setTransparentCircleRadius(10f + 5f);

                            // Generate colors dynamically
                            CommonMethods commonMethods = new CommonMethods();
                            ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size() - 1); // exclude the transparent entry
                            if (remainingPercentage > 0) {
                                colors.add(Color.TRANSPARENT); // Add transparent color for the remaining space
                            }
                            dataSet.setColors(colors);

                            PieData data = new PieData(dataSet);
                            pieChart.setData(data);
                            pieChart.invalidate();

                            container.addView(pieChart);

                            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {
                                    PieEntry entry = (PieEntry) e;
                                    String kpiName = entry.getLabel();
                                    float kpiValue = entry.getValue();

                                    KPI selectedKpi = null;
                                    for (KPI kpi : details.getKpiList()) {
                                        if (kpi.getName().equals(kpiName) && kpi.getKpiWeightage().getWeightage() == kpiValue) {
                                            selectedKpi = kpi;
                                            break;
                                        }
                                    }

                                    if (details.getGroupKpi() == null){
                                        AddGeneralKpiFragment addGeneralKpiFragment = new AddGeneralKpiFragment(details, selectedKpi);

                                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_container, addGeneralKpiFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }else {
                                        if (details.getGroupKpi().getEmployee() == null){
                                            AddGroupKpiFragment addGroupKpiFragment = new AddGroupKpiFragment(details, selectedKpi);

                                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, addGroupKpiFragment);
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }else {
                                            AddIndividualKpiFragment addIndividualKpiFragment = new AddIndividualKpiFragment(details, selectedKpi);

                                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, addIndividualKpiFragment);
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected() {
                                }
                            });
                        }
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }
}
