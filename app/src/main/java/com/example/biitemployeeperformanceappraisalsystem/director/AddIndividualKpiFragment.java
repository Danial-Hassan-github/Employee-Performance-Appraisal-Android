package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIndividualKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIndividualKpiFragment extends Fragment {
    Spinner employeeSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_individual_kpi, container, false);
        employeeSpinner = view.findViewById(R.id.spinner_employee);

        CommonData commonData = new CommonData(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, commonData.generateNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeSpinner.setAdapter(adapter);

        return view;
    }
}