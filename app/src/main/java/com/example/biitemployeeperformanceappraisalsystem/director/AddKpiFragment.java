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
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Converter;

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

    public static AddKpiFragment newInstance(String kpiName, float kpiValue, ArrayList<Float> pieChartValues, ArrayList<String> pieChartTitles) {
        AddKpiFragment fragment = new AddKpiFragment();
        Bundle args = new Bundle();
        args.putString("kpiName", kpiName);
        args.putFloat("kpiValue", kpiValue);
        args.putFloatArray("pieChartValues", convertFloatArrayListToArray(pieChartValues));
        args.putStringArrayList("pieChartTitles", pieChartTitles);
        fragment.setArguments(args);
        return fragment;
    }

    // Helper method to convert ArrayList<Float> to float[]
    private static float[] convertFloatArrayListToArray(ArrayList<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

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
        btnSave = view.findViewById(R.id.btn_save_kpi);

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



        // Inside onCreateView method
//        Bundle args = getArguments();
//        if (args != null) {
//            // Retrieve pie chart values and titles
//            float[] pieChartValues = args.getFloatArray("pieChartValues");
//            ArrayList<String> pieChartTitles = args.getStringArrayList("pieChartTitles");

//            EditText kpiName = view.findViewById(R.id.text_kpi_title);// Get KpiName from input field
//            EditText kpiValue = view.findViewById(R.id.text_kpi_weightage);// Get KpiValue from input field
//
//            // Pass these values to the KpiWeightageAdjustmentFormFragment
//            btnSave.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Retrieve the entered KpiName and KpiValue
//                    String kpiNameArg = args.getString("kpiName");
//                    float kpiValueArg = args.getFloat("kpiValue");
//
//                    kpiName.setText(kpiNameArg);
//                    kpiValue.setText(String.valueOf(kpiValueArg));
//
//                    // Navigate to the KpiWeightageAdjustmentFormFragment and pass the KpiName, KpiValue, pie chart values, and titles
//                    KpiWeightageAdjustmentFormFragment fragment = KpiWeightageAdjustmentFormFragment.newInstance(kpiName.getText().toString(), Float.parseFloat(kpiValue.getText().toString()), pieChartValues, pieChartTitles);
//                    FragmentTransaction transaction = getParentFragment().getChildFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, fragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                }
//            });
//        }



        // Find your text boxes by their IDs
         EditText kpiNameEditText = view.findViewById(R.id.text_kpi_title);
         EditText kpiValueEditText = view.findViewById(R.id.text_kpi_weightage);

        // Retrieve the passed data from the arguments bundle
        Bundle args = getArguments();
        if (args != null) {
            String kpiName = args.getString("kpiName");
            float kpiValue = args.getFloat("kpiValue");
            // Retrieve pie chart values and titles
            float[] pieChartValues = args.getFloatArray("pieChartValues");
            ArrayList<String> pieChartTitles = args.getStringArrayList("pieChartTitles");

            // Set the values in your text boxes
            kpiNameEditText.setText(kpiName);
            kpiValueEditText.setText(String.valueOf(kpiValue));

            // Pass these values to the KpiWeightageAdjustmentFormFragment
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Navigate to the KpiWeightageAdjustmentFormFragment and pass the KpiName, KpiValue, pie chart values, and titles
                    KpiWeightageAdjustmentFormFragment fragment = KpiWeightageAdjustmentFormFragment.newInstance(kpiName, kpiValue, pieChartValues, pieChartTitles);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }

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