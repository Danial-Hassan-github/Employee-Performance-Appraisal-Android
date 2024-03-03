package com.example.biitemployeeperformanceappraisalsystem;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieChartPerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartPerformanceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart_performance, container, false);
        PieChart pieChart = view.findViewById(R.id.pie_chart);
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
        pieChart.invalidate(); // Refresh chart

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