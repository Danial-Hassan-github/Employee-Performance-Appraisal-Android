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
import com.example.biitemployeeperformanceappraisalsystem.models.Department;
import com.example.biitemployeeperformanceappraisalsystem.models.DepartmentKPI;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DepartmentService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.KpiService;
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
    int sessionId;
    KpiService kpiService;
    DepartmentService departmentService;
    SharedPreferencesManager sharedPreferencesManager;
    List<GroupKpiDetails> groupKpiDetailsList;
    List<DepartmentKPI> departmentKpiList;
    List<Department> departmentList;
    List<Session> sessionList;
    Spinner sessionSpinner, departmentSpinner;
    Button btnAddKpi;
    ArrayList<Float> pieChartValues;
    ArrayList<String> pieChartTitles;
    Department selectedDepartment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kpi, container, false);

        sessionSpinner = view.findViewById(R.id.spinner_session);
        departmentSpinner = view.findViewById(R.id.spinner_department);
        btnAddKpi = view.findViewById(R.id.btn_add_general_kpi);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        departmentService = new DepartmentService(view.getContext());

        DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
        btnAddKpi.setOnClickListener(v -> directorMainActivity.replaceFragment(new AddKpiFragment()));

        SessionService sessionService = new SessionService(view.getContext());
        sessionService.getSessions(sessions -> {
            sessionList = sessions;
            sessionService.populateSpinner(sessions, sessionSpinner);
        }, errorMessage -> Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show());

        departmentService.getDepartments(departments -> {
            departmentList = departments;
            departmentService.populateDepartmentSpinner(departments, departmentSpinner);
        }, errorMessage -> Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                Session selectedSession = sessionList.get(position);
                sessionId = selectedSession.getId();
                if (selectedDepartment != null) {
                    showKpiGraph(view);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                selectedDepartment = departmentList.get(position);
                if (sessionId != 0) {
                    showKpiGraph(view);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void showKpiGraph(View view) {
        try {
            ViewGroup container = view.findViewById(R.id.chart_container);
            container.removeAllViews(); // Clear previous charts
            kpiService = new KpiService(getContext());
            pieChartValues = new ArrayList<>();
            pieChartTitles = new ArrayList<>();
            kpiService.getSessionKpis(sessionId, departmentKPIS -> {
                departmentKpiList = departmentKPIS;
                for (DepartmentKPI departmentKPI : departmentKpiList) {
                    if (departmentKPI.getDepartmentId() == selectedDepartment.getId()) {
                        createPieChart(view, container, departmentKPI.getKpiList());
                    }
                }
            }, errorMessage -> Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());
        }catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createPieChart(View view, ViewGroup container, List<KPI> kpiList) {
        PieChart pieChart = new PieChart(getContext());
        pieChart.getDescription().setTextColor(Color.TRANSPARENT);
        int chartHeight = (int) getResources().getDimension(R.dimen.pie_chart_height);
        pieChart.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                chartHeight
        ));

        ArrayList<PieEntry> entries = new ArrayList<>();
        float totalScore = 0;
        for (KPI kpi : kpiList) {
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
        pieChart.setTransparentCircleRadius(15f);

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
                for (KPI kpi : kpiList) {
                    if (kpi.getName().equals(kpiName) && kpi.getKpiWeightage().getWeightage() == kpiValue) {
                        selectedKpi = kpi;
                        break;
                    }
                }

                if (selectedKpi != null) {
                    AddKpiFragment addKpiFragment = new AddKpiFragment();

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, addKpiFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }
}
