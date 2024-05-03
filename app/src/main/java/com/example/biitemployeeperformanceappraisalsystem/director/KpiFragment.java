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
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
public class KpiFragment extends Fragment {

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

        DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
        btnAddKpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directorMainActivity.replaceFragment(new AddKpiFragment());
            }
        });

//        btnAddGroupKpi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                directorMainActivity.replaceFragment(new AddGroupKpiFragment());
//            }
//        });

//        btnAddIndividualKpi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                directorMainActivity.replaceFragment(new AddIndividualKpiFragment());
//            }
//        });

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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected session
                Session selectedSession = sessionList.get(position);
                // Use the ID of the selected session
                int sessionId = selectedSession.getId();
                // Perform actions with the session ID
                Toast.makeText(getContext(), sessionId+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        showKpiGraph(view);

        return view;
    }

    private void showKpiGraph(View view){
        PieChart pieChart = view.findViewById(R.id.pie_chart_kpi);
        pieChart.getDescription().setTextColor(Color.TRANSPARENT);

        // Hide the bar chart
//        BarChart barChart = view.findViewById(R.id.bar_chart);
//        barChart.setVisibility(View.GONE);
//
//        pieChart.setVisibility(View.VISIBLE);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20, "Administrative"));
        entries.add(new PieEntry(25, "Academic"));
        entries.add(new PieEntry(25, "Punctuality"));
        entries.add(new PieEntry(30, "Project"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setValueTextSize(12f);

        // Set up the pie chart
        pieChartValues = new ArrayList<>();
        pieChartTitles = new ArrayList<>();
        // Add your pie chart data
        pieChartValues.add(20f);
        pieChartValues.add(25f);
        pieChartValues.add(25f);
        pieChartValues.add(30f);
        pieChartTitles.add("Administrative");
        pieChartTitles.add("Academic");
        pieChartTitles.add("Punctuality");
        pieChartTitles.add("Project");

        // Generate colors dynamically
        CommonMethods commonMethods=new CommonMethods();
        ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Extract data associated with the selected slice
                PieEntry entry = (PieEntry) e;
                String kpiName = entry.getLabel();
                float kpiValue = entry.getValue();

                // Pass data to editable form fragment
                AddGeneralKpiFragment addGeneralKpiFragment = AddGeneralKpiFragment.newInstance(kpiName, kpiValue, pieChartValues, pieChartTitles);

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

        // Other pie chart setup code...
    }
}
