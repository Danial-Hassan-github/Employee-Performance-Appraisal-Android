package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.SubKpiListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Department;
import com.example.biitemployeeperformanceappraisalsystem.models.DepartmentKPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWithSubKpiWeightages;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DepartmentService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.KpiService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SubKpiService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateKpiFragment extends Fragment {
    List<KPI> kpiList;
    List<SubKpi> subKpiAdapterList;
    SubKpiListAdapter subKpiListAdapter;
    boolean isSpinnerInitialized = false;
    boolean ignoreNextSelection = false;
    Department department;
    KPI kpi;
    SharedPreferencesManager sharedPreferencesManager;
    KpiService kpiService;
    SubKpiService subKpiService;
    List<SubKpi> subKpiList;
    ListView subKpiListView;
    ListView newSubKpiListView;
    List<Department> departmentList;
    List<DepartmentKPI> departmentKPIList;
    Spinner departmentSpinner,subKpiSpinner;
    Button btnSave;
    public UpdateKpiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public UpdateKpiFragment(List<KPI> kpiList, KPI kpi) {
        this.kpi = kpi;
        this.kpiList = kpiList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_kpi, container, false);
        subKpiSpinner = view.findViewById(R.id.spinner_sub_kpi);
        subKpiListView = view.findViewById(R.id.list_view_subKpi);
        newSubKpiListView = view.findViewById(R.id.list_view_new_subKpi);
        btnSave = view.findViewById(R.id.btn_save_kpi);

        subKpiList = new ArrayList<>();
        SubKpi subKpi = new SubKpi();
        subKpi.setName("Select SubKpi");
        subKpiList.add(subKpi);
        subKpiAdapterList = new ArrayList<>();

        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        int sessionId = sharedPreferencesManager.getSessionId();
        Log.d("Add Kpi Fragment", "Session ID: " + sessionId); // Debug log for session ID
        kpiService = new KpiService(getContext());
        subKpiService = new SubKpiService(getContext());
        DepartmentService departmentService=new DepartmentService(view.getContext());

        // Initialize the subKpiList and the adapter
        subKpiListAdapter = new SubKpiListAdapter(getContext(), R.layout.sub_kpi_list_item_view, subKpiAdapterList);
        subKpiListView.setAdapter(subKpiListAdapter);
        subKpiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true; // Set the flag to true after the first automatic selection
                    return; // Ignore the automatic selection
                }

                if (ignoreNextSelection) {
                    ignoreNextSelection = false; // Reset the flag
                    return; // Ignore this automatic selection
                }

                try {
                    SubKpi selectedSubKpi = subKpiList.get(position);
                    subKpiAdapterList.add(selectedSubKpi);
                    subKpiList.remove(position);
                    // Notify the adapter to refresh the Spinner and the ListView
                    subKpiService.populateSpinner(subKpiList, subKpiSpinner);
                    subKpiListAdapter.notifyDataSetChanged();

                    // Clear the selection and set the flag to ignore the next selection event
                    subKpiSpinner.setSelection(AdapterView.INVALID_POSITION);
                    ignoreNextSelection = true;

                } catch (Exception ex) {
                    // Handle the exception if necessary
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        subKpiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubKpi deletedSubKpi = subKpiAdapterList.get(position);
                subKpiAdapterList.remove(position);
                subKpiList.add(deletedSubKpi);
                // Notify the adapter to refresh the Spinner and the ListView
                subKpiService.populateSpinner(subKpiList, subKpiSpinner);
                subKpiListAdapter.notifyDataSetChanged();
            }
        });

        // Find your text boxes by their IDs
        EditText kpiNameEditText = view.findViewById(R.id.text_title);
        EditText kpiValueEditText = view.findViewById(R.id.text_weightage);

        kpiValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int sum = 0;
                // btnSave.setEnabled(false);
                try {
                    sum = Integer.parseInt(kpiValueEditText.getText().toString());
                    // btnSave.setEnabled(true);
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Weightage must be in numbers", Toast.LENGTH_SHORT).show();
                }
                if (sum > 100 || sum < 0){
                    Toast.makeText(getContext(), "Weightage cannot be more than 100 or less than 0", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(false);
                }else {
                    btnSave.setEnabled(true);
                }
            }
        });

        if (kpi != null) {
            String kpiName = kpi.getName();
            float kpiValue = kpi.getKpiWeightage().getWeightage();

            // Set the values in your text boxes
            kpiNameEditText.setText(kpiName);
            kpiValueEditText.setText(String.valueOf(kpiValue));

            subKpiService.getSubKPIsOfKpi(
                    kpi.getId(),
                    sharedPreferencesManager.getSessionId(),
                    subKpiList1 -> {
                        subKpiList = subKpiList1;
                        subKpiListAdapter.addAll(subKpiList);
                        subKpiListAdapter.notifyDataSetChanged();
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), "Failed to fetch SubKPIs", Toast.LENGTH_SHORT).show();
                    }
            );

            // Pass these values to the KpiWeightageAdjustmentFormFragment
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KpiWithSubKpiWeightages kpiWithSubKpiWeightages = new KpiWithSubKpiWeightages();
                List<SubKpiWeightage> subKpiWeightages = new ArrayList<>();

                int weightageSum = Integer.parseInt(kpiValueEditText.getText().toString());
                KPI newKpi = new KPI();
                newKpi.setName(kpiNameEditText.getText().toString());
                newKpi.setDepartment_id(department.getId());
                KpiWeightage newKpiWeightage = new KpiWeightage();
                newKpiWeightage.setWeightage(weightageSum);
                newKpiWeightage.setSession_id(sharedPreferencesManager.getSessionId());
                newKpi.setKpiWeightage(newKpiWeightage);

//                for (int i = 0 ; i < groupKpiDetails.getKpiList().size() - 1; i++){
//                    weightageSum += groupKpiDetails.getKpiList().get(i).getKpiWeightage().getWeightage();
//                }

                for (SubKpi s:subKpiAdapterList) {
                    //SubKpiWeightage subKpiWeightage = new SubKpiWeightage();
                    s.getSubKpiWeightage().setSession_id(sharedPreferencesManager.getSessionId());
                    s.getSubKpiWeightage().setSub_kpi_id(s.getId());
                    // s.setSubKpiWeightage(subKpiWeightage);
                    subKpiWeightages.add(s.getSubKpiWeightage());
                }

                kpiWithSubKpiWeightages.setKpi(newKpi);
                kpiWithSubKpiWeightages.setWeightage(newKpiWeightage);
                kpiWithSubKpiWeightages.setSubKpiWeightages(subKpiWeightages);

                navigateToWeightageAdjustmentForm(kpiWithSubKpiWeightages);

//                kpiService.postKpi(
//                        kpiWithSubKpiWeightages,
//                        kpi1 -> {
//                            Toast.makeText(getContext(), "Kpi Added Successfully", Toast.LENGTH_SHORT).show();
//                        },
//                        errorMessage -> {
//                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                        });
            }
        });

        return view;
    }

    public void navigateToWeightageAdjustmentForm(KpiWithSubKpiWeightages kpiWithSubKpiWeightages) {
        KpiWeightageAdjustmentFormFragment fragment = new KpiWeightageAdjustmentFormFragment(kpiList, kpiWithSubKpiWeightages);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}