package com.example.biitemployeeperformanceappraisalsystem;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.CommonData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerformanceFragment extends Fragment {
    List<Session> sessionList;
    Spinner sessionSpinner,fromSessionSpinner,toSessionSpinner;
    Button show,compare;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        show=view.findViewById(R.id.btn_show);
        compare=view.findViewById(R.id.btn_compare);

        sessionSpinner = view.findViewById(R.id.spinner_session);
        fromSessionSpinner = view.findViewById(R.id.spinner_session_from);
        toSessionSpinner = view.findViewById(R.id.spinner_session_to);

        CommonData data = new CommonData(view.getContext());
        data.getSessions(sessions -> {
                    // Handle the list of sessions here
                    sessionList = sessions;
                    // Populate the spinner with session titles
                    CommonData commonData=new CommonData(view.getContext());
                    commonData.populateSpinner(sessions,sessionSpinner);
                    commonData.populateSpinner(sessions,fromSessionSpinner);
                    commonData.populateSpinner(sessions,toSessionSpinner);
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

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PieChart pieChart = view.findViewById(R.id.pie_chart);

                // Hide the bar chart
                BarChart barChart = view.findViewById(R.id.bar_chart);
                barChart.setVisibility(View.GONE);

                pieChart.setVisibility(View.VISIBLE);

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(18.5f, "Green"));
                entries.add(new PieEntry(26.7f, "Red"));
                entries.add(new PieEntry(24.0f, "Blue"));
                entries.add(new PieEntry(30.8f, "Yellow"));

                PieDataSet dataSet = new PieDataSet(entries, "Pie Chart");

                // Generate colors dynamically
                ArrayList<Integer> colors = generateRandomColors(entries.size());
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                pieChart.setData(data);
                pieChart.invalidate();
            }
        });

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarChart barChart = view.findViewById(R.id.bar_chart);

                // Hide the pie chart
                PieChart pieChart = view.findViewById(R.id.pie_chart);
                pieChart.setVisibility(View.GONE);

                barChart.setVisibility(View.VISIBLE);

                ArrayList<BarEntry> group1 = new ArrayList<>();
                group1.add(new BarEntry(0f, 10f)); // Note the change in x-position
                group1.add(new BarEntry(1f, 20f));
                group1.add(new BarEntry(2f, 30f));

                ArrayList<BarEntry> group2 = new ArrayList<>();
                group2.add(new BarEntry(0.33f, 15f)); // Adjust the x-position to group the bars
                group2.add(new BarEntry(1.33f, 25f)); // Note the difference in x-position
                group2.add(new BarEntry(2.33f, 35f));

                BarDataSet barDataSet1 = new BarDataSet(group1, "Group 1");
                barDataSet1.setColor(Color.rgb(0, 155, 0));

                BarDataSet barDataSet2 = new BarDataSet(group2, "Group 2");
                barDataSet2.setColor(Color.rgb(155, 0, 0));

// Adjust the bar width and spacing
                float groupSpace = 0.3f; // space between groups of bars
                float barSpace = 0.04f; // space between individual bars within a group
                float barWidth = 0.3f; // width of each bar

                BarData barData = new BarData(barDataSet1, barDataSet2);
                barData.setBarWidth(barWidth);

                // Group the bars
                barChart.setData(barData);
                barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
                barChart.invalidate();

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private ArrayList<Integer> generateRandomColors(int count) {
        ArrayList<Integer> colors = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            colors.add(color);
        }
        return colors;
    }
}