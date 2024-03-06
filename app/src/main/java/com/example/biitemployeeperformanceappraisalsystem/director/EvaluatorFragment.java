package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.SessionData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluatorFragment extends Fragment {
    List<Session> sessionList;
    Spinner sessionSpinner,evaluatorSpinner,evaluateeSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_evaluator, container, false);
        sessionSpinner = view.findViewById(R.id.spinner_session);
        evaluatorSpinner = view.findViewById(R.id.spinner_evaluator);
        evaluateeSpinner = view.findViewById(R.id.spinner_evaluatee);

        CommonData data=new CommonData(getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evaluatorSpinner.setAdapter(adapter);

        List<String> evaluateeList=new ArrayList<>();
        evaluateeList.add("Select All");
        evaluateeList.addAll(data.generateNames());

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, evaluateeList);
        evaluateeSpinner.setAdapter(customSpinnerAdapter);

        SessionData sessionData = new SessionData(view.getContext());
        sessionData.getSessions(sessions -> {
                    // Handle the list of sessions here
                    sessionList = sessions;
                    // Populate the spinner with session titles
                    sessionData.populateSpinner(sessions,sessionSpinner);
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

        // Inflate the layout for this fragment
        return view;
    }
}