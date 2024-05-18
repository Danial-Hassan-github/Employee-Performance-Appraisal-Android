package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.SubKpiListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SubKpiService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGeneralKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGeneralKpiFragment extends Fragment {
    GroupKpiDetails groupKpiDetails;
    KPI kpi;
    List<Session> sessionList;
    ListView subKpiListView;
    Spinner sessionSpinner,employeeTypeSpinner,designationSpinner,departmentSpinner,subKpiSpinner;
    Button btnSave;
    List<SubKpi> subKpiList;
    List<SubKpi> subKpiAdapterList;
    SubKpiListAdapter subKpiListAdapter;
    SubKpiService subKpiService;

    public AddGeneralKpiFragment(){

    }

    public AddGeneralKpiFragment(GroupKpiDetails groupKpiDetails, KPI kpi){
        this.groupKpiDetails = groupKpiDetails;
        this.kpi = kpi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_general_kpi, container, false);

        subKpiSpinner =  view.findViewById(R.id.spinner_sub_kpi);
        subKpiListView = view.findViewById(R.id.list_view_subKpi);
        btnSave = view.findViewById(R.id.btn_save_kpi);
        subKpiService = new SubKpiService(getContext());

        subKpiList = new ArrayList<>();
        subKpiAdapterList = new ArrayList<>();

//        CommonData data=new CommonData(getContext());
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.getSubKPITypes());
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        subKpiSpinner.setAdapter(adapter);
        subKpiService.getSubKPIs(
                10,
                subKpiList1 -> {
                    if (subKpiList1 != null) {
                        subKpiList = subKpiList1;
                        String subKpiTitles[] = subKpiService.getSubKpiTitles(subKpiList);
                        if (subKpiTitles != null) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subKpiTitles);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            subKpiSpinner.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "No SubKPI titles available", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch SubKPIs", Toast.LENGTH_SHORT).show();
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        // Initialize the subKpiList and the adapter
        subKpiListAdapter = new SubKpiListAdapter(getContext(), R.layout.sub_kpi_list_item_view, subKpiAdapterList);
        subKpiListView.setAdapter(subKpiListAdapter);

        // Set the spinner item selected listener
        subKpiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubKpiName = parent.getItemAtPosition(position).toString();
                // Create a new SubKpi object with the selected name
                SubKpi selectedSubKpi = new SubKpi();
                // Add the selected SubKpi to the list
                subKpiAdapterList.add(subKpiList.get(position));
                // Notify the adapter to refresh the ListView
                subKpiListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Find your text boxes by their IDs
         EditText kpiNameEditText = view.findViewById(R.id.text_kpi_title);
         EditText kpiValueEditText = view.findViewById(R.id.text_kpi_weightage);

        if (kpi != null) {
            String kpiName = kpi.getName();
            float kpiValue = kpi.getKpiWeightage().getWeightage();

            // Set the values in your text boxes
            kpiNameEditText.setText(kpiName);
            kpiValueEditText.setText(String.valueOf(kpiValue));

            // Pass these values to the KpiWeightageAdjustmentFormFragment
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int sum = Integer.parseInt(kpiValueEditText.getText().toString());
                    for (int i = 0 ; i < groupKpiDetails.getKpiList().size(); i++){
                        if (kpi.getId() == groupKpiDetails.getKpiList().get(i).getId()){
                            KpiWeightage kpiWeightage = groupKpiDetails.getKpiList().get(i).getKpiWeightage();
                            kpiWeightage.setWeightage(Integer.parseInt(kpiValueEditText.getText().toString()));
                            groupKpiDetails.getKpiList().get(i).setKpiWeightage(kpiWeightage);
                        }else {
                            sum += groupKpiDetails.getKpiList().get(i).getKpiWeightage().getWeightage();
                        }
                    }

                    ArrayList<Float> pieChartValues = new ArrayList<>();
                    ArrayList<String> pieChartTitles = new ArrayList<>();
                    for (KPI item: groupKpiDetails.getKpiList()) {
                        pieChartTitles.add(item.getName());
                        pieChartValues.add((float)item.getKpiWeightage().getWeightage());
                    }

                    Toast.makeText(getContext(), sum+"", Toast.LENGTH_SHORT).show();
                    // Navigate to the KpiWeightageAdjustmentFormFragment and pass the pie chart values and titles
                    if (sum > 100){
                        KpiWeightageAdjustmentFormFragment fragment = new KpiWeightageAdjustmentFormFragment(pieChartValues, pieChartTitles);
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }

        CommonData commonData = new CommonData(getContext());
        List<String> subKpiNames = commonData.getSubKPITypes();

        // Inflate the layout for this fragment
        return view;
    }
}