package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
public class KpiFragment extends Fragment {

    List<Session> sessionList;
    Spinner sessionSpinner;
    Button btnAddKpi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kpi, container, false);

        sessionSpinner = view.findViewById(R.id.spinner_session);
        btnAddKpi = view.findViewById(R.id.btn_add_kpi);

        DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
        btnAddKpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directorMainActivity.replaceFragment(new AddKpiFragment());
            }
        });

        // Inflate the layout for this fragment
        SessionService sessionService = new SessionService(view.getContext());
        sessionService.getSessions(sessions -> {
                    // Handle the list of sessions here
                    sessionList = sessions;
                    // Populate the spinner with session titles
                    sessionService.populateSpinner(sessions,sessionSpinner);
                },
                // onFailure callback
                errorMessage -> {
                    // Handle failure
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });
        //Show Kpi Graph
        showKpiGraph(view);

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

        return view;
    }

    private void showKpiGraph(View view){
        PieChart pieChart = view.findViewById(R.id.pie_chart);

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

        PieDataSet dataSet = new PieDataSet(entries, "KPI Graph");

        // Generate colors dynamically
        CommonMethods commonMethods=new CommonMethods();
        ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

}
