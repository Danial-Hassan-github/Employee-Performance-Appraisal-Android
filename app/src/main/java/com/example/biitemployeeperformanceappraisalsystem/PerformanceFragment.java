package com.example.biitemployeeperformanceappraisalsystem;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.helper.CommonMethods;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeKpiScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerformanceFragment extends Fragment {
    int employeeID;
    List<EmployeeKpiScore> employeeKpiScoreList;
    List<EmployeeKpiScoreMultiSession> employeeKpiScoreMultiSessionList;
    List<Session> sessionList;
    LinearLayout sessionLayout, comparisonSessionLayout;
    PieChart pieChart;
    BarChart barChart;
    TabLayout tabLayout;
    Spinner sessionSpinner,fromSessionSpinner,toSessionSpinner,courseSpinner;
    Button show,compare;

    public PerformanceFragment(int employeeID){
        this.employeeID = employeeID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        pieChart = view.findViewById(R.id.pie_chart);
        barChart = view.findViewById(R.id.bar_chart);
        tabLayout = view.findViewById(R.id.performance_type_tab);
        sessionLayout = view.findViewById(R.id.session_spinner_layout);
        comparisonSessionLayout = view.findViewById(R.id.comparison_sessions_layout);
        // show=view.findViewById(R.id.btn_show);
        // compare=view.findViewById(R.id.btn_compare);

        pieChart.getDescription().setTextColor(Color.TRANSPARENT);
        barChart.getDescription().setTextColor(Color.TRANSPARENT);

        courseSpinner = view.findViewById(R.id.spinner_course);
        sessionSpinner = view.findViewById(R.id.spinner_session);
        fromSessionSpinner = view.findViewById(R.id.spinner_session_from);
        toSessionSpinner = view.findViewById(R.id.spinner_session_to);

        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
        CommonData commonData=new CommonData(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, commonData.generateCourseNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);

        SessionService sessionService = new SessionService(view.getContext());
        sessionService.getSessions(sessions -> {
                    // Handle the list of sessions here
                    sessionList = sessions;
                    // Populate the spinner with session titles
                    sessionService.populateSpinner(sessions,sessionSpinner);
                    sessionService.populateSpinner(sessions,fromSessionSpinner);
                    sessionService.populateSpinner(sessions,toSessionSpinner);
                },
                // onFailure callback
                errorMessage -> {
                    // Handle failure
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
                updatePieChart();
                // Perform actions with the session ID
                Toast.makeText(getContext(), sessionId+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        // Set an item selected listener for the from_session spinner
        fromSessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected session
                Session selectedSession = sessionList.get(position);
                // Use the ID of the selected session
                int sessionId = selectedSession.getId();
                // Perform actions with the session ID
                Toast.makeText(getContext(), sessionId+"", Toast.LENGTH_LONG).show();
                updateBarChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        // Set an item selected listener for the to_session spinner
        toSessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected session
                Session selectedSession = sessionList.get(position);
                // Use the ID of the selected session
                int sessionId = selectedSession.getId();
                // Perform actions with the session ID
                Toast.makeText(getContext(), sessionId+"", Toast.LENGTH_LONG).show();
                updateBarChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                CommonMethods commonMethods=new CommonMethods();
                switch (position){
                    case 0:
                        updatePieChart();
                        break;
                    case 1:
                        updateBarChart();
//                        ArrayList<BarEntry> group1 = new ArrayList<>();
//                        group1.add(new BarEntry(0, 27)); // Note the change in x-position
//                        group1.add(new BarEntry(3, 23));
//                        group1.add(new BarEntry(6, 35));
//
//                        ArrayList<BarEntry> group2 = new ArrayList<>();
//                        group2.add(new BarEntry(1, 32)); // Adjust the x-position to group the bars
//                        group2.add(new BarEntry(4, 26)); // Note the difference in x-position
//                        group2.add(new BarEntry(7, 40));
//
//                        ArrayList<BarEntry> group3 = new ArrayList<>();
//                        group3.add(new BarEntry(2, 28)); // Adjust the x-position to group the bars
//                        group3.add(new BarEntry(5, 30)); // Note the difference in x-position
//                        group3.add(new BarEntry(8, 35));
//
//                        BarDataSet barDataSet1 = new BarDataSet(group1, "Academic");
//                        barDataSet1.setColor(Color.rgb(0, 155, 0));
//
//                        BarDataSet barDataSet2 = new BarDataSet(group2, "Project");
//                        barDataSet2.setColor(Color.rgb(155, 0, 0));
//
//                        BarDataSet barDataSet3 = new BarDataSet(group3, "Punctuality");
//                        barDataSet3.setColor(Color.rgb(0, 0, 155));
//
//// Adjust the bar width and spacing
//                        float groupSpace = 0.2f; // space between groups of bars
//                        float barSpace = 0.02f; // space between individual bars within a group
//                        float barWidth = 0.15f; // width of each bar
//
//                        BarData barData = new BarData(barDataSet1, barDataSet2, barDataSet3);
//                        barData.setBarWidth(barWidth);
//
//                        // Group the bars
//                        barChart.setData(barData);
//                        barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
//                        barChart.invalidate();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void updatePieChart() {
        barChart.setVisibility(View.GONE);
        comparisonSessionLayout.setVisibility(View.GONE);

        pieChart.setVisibility(View.VISIBLE);
        sessionLayout.setVisibility(View.VISIBLE);
        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
        employeeKpiScoreService.getEmployeeKpiScore(
                employeeID,
                sessionList.get(sessionSpinner.getSelectedItemPosition()).getId(),
                employeeKpiScores -> {
                    employeeKpiScoreList = employeeKpiScores;
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    for (EmployeeKpiScore e:employeeKpiScores) {
                        entries.add(new PieEntry(e.getScore(),e.getKpi_title()+"="+e.getWeightage()));
                    }
//                                    entries.add(new PieEntry(20, "Administrative"));
//                                    entries.add(new PieEntry(25, "Academic"));
//                                    entries.add(new PieEntry(25, "Punctuality"));
//                                    entries.add(new PieEntry(30, "Project"));

                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setValueTextSize(20f);

                    // Generate colors dynamically
                    CommonMethods commonMethods = new CommonMethods();
                    ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
                    dataSet.setColors(colors);

                    PieData data = new PieData(dataSet);
                    pieChart.setData(data);
                    pieChart.invalidate();
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void updateBarChart() {
        pieChart.setVisibility(View.GONE);
        sessionLayout.setVisibility(View.GONE);

        barChart.setVisibility(View.VISIBLE);
        comparisonSessionLayout.setVisibility(View.VISIBLE);

        // Define the labels for each group
        List<String> groupLabels = new ArrayList<>();

        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());

        employeeKpiScoreService.getEmployeeKpiScoreMultiSession(
                employeeID,
                sessionList.get(fromSessionSpinner.getSelectedItemPosition()).getId(),
                sessionList.get(toSessionSpinner.getSelectedItemPosition()).getId(),
                employeeKpiScoreMultiSessions -> {
                    BarData barData = new BarData();
                    employeeKpiScoreMultiSessionList = employeeKpiScoreMultiSessions;
                    List<ArrayList<BarEntry>> groups = new ArrayList<>(employeeKpiScoreMultiSessionList.size());
                    if (employeeKpiScoreMultiSessionList.size() > 0) {
                        int xCounter = 0;
                        int xInitialCounter = 1;
                        for (int i = 0; i < employeeKpiScoreMultiSessionList.size(); i++) {
                            EmployeeKpiScoreMultiSession employeeKpiScoreMultiSession = employeeKpiScoreMultiSessionList.get(i);
                            ArrayList<BarEntry> group = new ArrayList<>();
                            List<EmployeeKpiScore> scores = employeeKpiScoreMultiSession.getScores();
                            groupLabels.add(employeeKpiScoreMultiSession.getSession().getTitle());
                            String kpiTitle = null;
                            for (int k = 0; k < scores.size(); k++) {
                                group.add(new BarEntry(xCounter, scores.get(k).getScore()));
                                xCounter = xCounter + 3;
                                kpiTitle = scores.get(k).getKpi_title();
                            }
                            xCounter = xInitialCounter;
                            xInitialCounter++;
                            BarDataSet barDataSet = new BarDataSet(group, kpiTitle);
                            CommonMethods commonMethods = new CommonMethods();
                            ArrayList<Integer> colors = commonMethods.generateRandomColors(employeeKpiScoreMultiSessionList.size());
                            barDataSet.setColor(colors.get(i));
                            barData.addDataSet(barDataSet);
                        }

                        // Ensure there are at least 2 BarDataSets
                        if (barData.getDataSetCount() < 2) {
                            barData.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
                        }

                        float groupSpace = 0.2f; // space between groups of bars
                        float barSpace = 0.02f; // space between individual bars within a group
                        float barWidth = 0.15f; // width of each bar

                        barData.setBarWidth(barWidth);
                        barChart.setData(barData);

                        barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
                        barChart.invalidate();

                        // Set custom labels for the x-axis
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setLabelCount(groupLabels.size());
                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                int index = (int) value;
                                if (index >= 0 && index < groupLabels.size()) {
                                    return groupLabels.get(index);
                                } else {
                                    return "";
                                }
                            }
                        });
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });
    }

}