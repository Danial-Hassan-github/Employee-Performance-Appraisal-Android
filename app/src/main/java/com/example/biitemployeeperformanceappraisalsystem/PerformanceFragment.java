package com.example.biitemployeeperformanceappraisalsystem;

import android.graphics.Color;
import android.os.Bundle;

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
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
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
    List<Session> sessionList;
    LinearLayout sessionLayout, comparisonSessionLayout;
    PieChart pieChart;
    BarChart barChart;
    TabLayout tabLayout;
    Spinner sessionSpinner,fromSessionSpinner,toSessionSpinner,courseSpinner;
    Button show,compare;
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
                switch (position){
                    case 0:
                        barChart.setVisibility(View.GONE);
                        comparisonSessionLayout.setVisibility(View.GONE);

                        pieChart.setVisibility(View.VISIBLE);
                        sessionLayout.setVisibility(View.VISIBLE);

                        ArrayList<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(20, "Administrative"));
                        entries.add(new PieEntry(25, "Academic"));
                        entries.add(new PieEntry(25, "Punctuality"));
                        entries.add(new PieEntry(30, "Project"));

                        PieDataSet dataSet = new PieDataSet(entries, "");

                        // Generate colors dynamically
                        CommonMethods commonMethods=new CommonMethods();
                        ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
                        dataSet.setColors(colors);

                        PieData data = new PieData(dataSet);
                        pieChart.setData(data);
                        pieChart.invalidate();
                        break;
                    case 1:
                        pieChart.setVisibility(View.GONE);
                        sessionLayout.setVisibility(View.GONE);

                        barChart.setVisibility(View.VISIBLE);
                        comparisonSessionLayout.setVisibility(View.VISIBLE);

                        // Define the labels for each group
                        String[] groupLabels = new String[]{"Summer-2023", "Fall-2023", "Spring-2024"};

                        // Set custom labels for the x-axis
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setLabelCount(groupLabels.length); // Set the number of labels
                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                int index = (int) value;
                                if (index >= 0 && index < groupLabels.length) {
                                    return groupLabels[index];
                                } else {
                                    return "";
                                }
                            }
                        });

                        ArrayList<BarEntry> group1 = new ArrayList<>();
                        group1.add(new BarEntry(0, 27)); // Note the change in x-position
                        group1.add(new BarEntry(3, 23));
                        group1.add(new BarEntry(6, 35));

                        ArrayList<BarEntry> group2 = new ArrayList<>();
                        group2.add(new BarEntry(1, 32)); // Adjust the x-position to group the bars
                        group2.add(new BarEntry(4, 26)); // Note the difference in x-position
                        group2.add(new BarEntry(7, 40));

                        ArrayList<BarEntry> group3 = new ArrayList<>();
                        group3.add(new BarEntry(2, 28)); // Adjust the x-position to group the bars
                        group3.add(new BarEntry(5, 30)); // Note the difference in x-position
                        group3.add(new BarEntry(8, 35));

                        BarDataSet barDataSet1 = new BarDataSet(group1, "Academic");
                        barDataSet1.setColor(Color.rgb(0, 155, 0));

                        BarDataSet barDataSet2 = new BarDataSet(group2, "Project");
                        barDataSet2.setColor(Color.rgb(155, 0, 0));

                        BarDataSet barDataSet3 = new BarDataSet(group3, "Punctuality");
                        barDataSet3.setColor(Color.rgb(0, 0, 155));

// Adjust the bar width and spacing
                        float groupSpace = 0.2f; // space between groups of bars
                        float barSpace = 0.02f; // space between individual bars within a group
                        float barWidth = 0.15f; // width of each bar

                        BarData barData = new BarData(barDataSet1, barDataSet2, barDataSet3);
                        barData.setBarWidth(barWidth);

                        // Group the bars
                        barChart.setData(barData);
                        barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
                        barChart.invalidate();
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

//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // PieChart pieChart = view.findViewById(R.id.pie_chart);
//                // pieChart.getDescription().setTextColor(Color.TRANSPARENT);
//
//                // Hide the bar chart
//                // BarChart barChart = view.findViewById(R.id.bar_chart);
//                barChart.setVisibility(View.GONE);
//
//                pieChart.setVisibility(View.VISIBLE);
//
//                ArrayList<PieEntry> entries = new ArrayList<>();
//                entries.add(new PieEntry(20, "Administrative"));
//                entries.add(new PieEntry(25, "Academic"));
//                entries.add(new PieEntry(25, "Punctuality"));
//                entries.add(new PieEntry(30, "Project"));
//
//                PieDataSet dataSet = new PieDataSet(entries, "");
//
//                // Generate colors dynamically
//                CommonMethods commonMethods=new CommonMethods();
//                ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
//                dataSet.setColors(colors);
//
//                PieData data = new PieData(dataSet);
//                pieChart.setData(data);
//                pieChart.invalidate();
//            }
//        });
//
//        compare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // BarChart barChart = view.findViewById(R.id.bar_chart);
//                // barChart.getDescription().setTextColor(Color.TRANSPARENT);
//
//                // Hide the pie chart
//                // PieChart pieChart = view.findViewById(R.id.pie_chart);
//                pieChart.setVisibility(View.GONE);
//
//                barChart.setVisibility(View.VISIBLE);
//
//                ArrayList<BarEntry> group1 = new ArrayList<>();
//                group1.add(new BarEntry(0, 27)); // Note the change in x-position
//                group1.add(new BarEntry(3, 23));
//                group1.add(new BarEntry(6, 35));
//
//                ArrayList<BarEntry> group2 = new ArrayList<>();
//                group2.add(new BarEntry(1, 32)); // Adjust the x-position to group the bars
//                group2.add(new BarEntry(4, 26)); // Note the difference in x-position
//                group2.add(new BarEntry(7, 40));
//
//                ArrayList<BarEntry> group3 = new ArrayList<>();
//                group3.add(new BarEntry(2, 28)); // Adjust the x-position to group the bars
//                group3.add(new BarEntry(5, 30)); // Note the difference in x-position
//                group3.add(new BarEntry(8, 35));
//
//                BarDataSet barDataSet1 = new BarDataSet(group1, "Academic");
//                barDataSet1.setColor(Color.rgb(0, 155, 0));
//
//                BarDataSet barDataSet2 = new BarDataSet(group2, "Project");
//                barDataSet2.setColor(Color.rgb(155, 0, 0));
//
//                BarDataSet barDataSet3 = new BarDataSet(group3, "Punctuality");
//                barDataSet3.setColor(Color.rgb(0, 0, 155));
//
//// Adjust the bar width and spacing
//                float groupSpace = 0.2f; // space between groups of bars
//                float barSpace = 0.02f; // space between individual bars within a group
//                float barWidth = 0.15f; // width of each bar
//
//                BarData barData = new BarData(barDataSet1, barDataSet2, barDataSet3);
//                barData.setBarWidth(barWidth);
//
//                // Group the bars
//                barChart.setData(barData);
//                barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
//                barChart.invalidate();
//
//            }
//        });

        // Inflate the layout for this fragment
        return view;
    }
}