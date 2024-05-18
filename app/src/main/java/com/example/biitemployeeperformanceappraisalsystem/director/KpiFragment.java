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
    Button btnAddKpi,btnAddGroupKpi,btnAddIndividualKpi;
    ArrayList<Float> pieChartValues;
    ArrayList<String> pieChartTitles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kpi, container, false);

        sessionSpinner = view.findViewById(R.id.spinner_session);
        btnAddKpi = view.findViewById(R.id.btn_add_general_kpi);
        // btnAddGroupKpi =view.findViewById(R.id.btn_add_group_kpi);
        // btnAddIndividualKpi = view.findViewById(R.id.btn_add_individual_kpi);
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
                    sessionService.populateSpinner(sessions,sessionSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });

        // Set an item selected listener for the session spinner
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                // Get the selected session
                Session selectedSession = sessionList.get(position);
                // Use the ID of the selected session
                sessionId = selectedSession.getId();
                // Perform actions with the session ID
                showKpiGraph(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        // showKpiGraph(view);

        return view;
    }

    private void showKpiGraph(View view) {
        ViewGroup container = view.findViewById(R.id.chart_container); // Assuming chart container is a ViewGroup in your layout XML
        kpiService = new KpiService(getContext());
        pieChartValues = new ArrayList<>();
        pieChartTitles = new ArrayList<>();
        kpiService.getSessionKpis(
                sessionId,
                groupKpiDetails -> {
                    groupKpiDetailsList = groupKpiDetails;
                    if (groupKpiDetailsList != null) {
                        for (GroupKpiDetails details : groupKpiDetailsList) {
                            // Create a new pie chart for each item in the list
                            PieChart pieChart = new PieChart(getContext());
                            pieChart.getDescription().setTextColor(Color.TRANSPARENT);
                            int chartHeight = (int) getResources().getDimension(R.dimen.pie_chart_height);
                            pieChart.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    chartHeight
                            ));

                            ArrayList<PieEntry> entries = new ArrayList<>();
                            // Assuming details contains data necessary to populate the pie chart
                            for (KPI kpi: details.getKpiList()) {
                                float weightage = kpi.getKpiWeightage().getWeightage();
                                entries.add(new PieEntry(weightage, kpi.getName()));
                                pieChartValues.add(weightage);
                                pieChartTitles.add(kpi.getName());
                            }

                            PieDataSet dataSet = new PieDataSet(entries, "");
                            dataSet.setValueTextSize(20f);

                            // Generate colors dynamically
                            CommonMethods commonMethods = new CommonMethods();
                            ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
                            dataSet.setColors(colors);

                            PieData data = new PieData(dataSet);
                            pieChart.setData(data);
                            pieChart.invalidate();

                            // Add the pie chart to the container
                            container.addView(pieChart);

                            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {
                                    // Extract data associated with the selected slice
                                    PieEntry entry = (PieEntry) e;
                                    String kpiName = entry.getLabel();
                                    float kpiValue = entry.getValue();

                                    // Find the selected KPI
                                    KPI selectedKpi = null;
                                    for (KPI kpi : details.getKpiList()) {
                                        if (kpi.getName().equals(kpiName) && kpi.getKpiWeightage().getWeightage() == kpiValue) {
                                            selectedKpi = kpi;
                                            break;
                                        }
                                    }

                                    // Pass data to editable form fragment
                                    AddGeneralKpiFragment addGeneralKpiFragment = new AddGeneralKpiFragment(details, selectedKpi);

                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_container, addGeneralKpiFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }

                                @Override
                                public void onNothingSelected() {
                                    // Handle case where no slice is selected
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
