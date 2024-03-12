package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddKpiFragment extends Fragment {
    List<Session> sessionList;
    ListView subKpiListView;
    Spinner sessionSpinner,employeeTypeSpinner,designationSpinner,departmentSpinner,subKpiSpinner;
    Button btnSave;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_kpi, container, false);
        sessionSpinner = view.findViewById(R.id.spinner_session);
        //employeeTypeSpinner = view.findViewById(R.id.spinner_employee_type);
        //designationSpinner = view.findViewById(R.id.spinner_designation);
        //departmentSpinner = view.findViewById(R.id.spinner_department);
        subKpiSpinner =  view.findViewById(R.id.spinner_sub_kpi);
        //subKpiListView = view.findViewById(R.id.list_view_subKpi);

        CommonData data=new CommonData(getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.getSubKPITypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        employeeTypeSpinner.setAdapter(adapter);
//
//        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateDepartments());
//        departmentSpinner.setAdapter(adapter);
//
//        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateDesignations());
//        designationSpinner.setAdapter(adapter);
//
//        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.getSubKPITypes());
        subKpiSpinner.setAdapter(adapter);


        CommonData commonData = new CommonData(getContext());
        List<String> subKpiNames = commonData.getSubKPITypes();

        SessionService sessionService=new SessionService(getContext());
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