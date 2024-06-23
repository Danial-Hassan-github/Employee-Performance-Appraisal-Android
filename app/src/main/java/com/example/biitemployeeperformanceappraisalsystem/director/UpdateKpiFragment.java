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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class UpdateKpiFragment extends Fragment {
    List<KPI> kpiList;
    List<SubKpi> subKpiAdapterList;
    SubKpiListAdapter subKpiListAdapter;
    SubKpiListAdapter newSubKpiListAdapter;
    List<SubKpi> deletedSubKpis;
    boolean isSpinnerInitialized = false;
    boolean ignoreNextSelection = false;
    Department department;
    KPI kpi;
    SharedPreferencesManager sharedPreferencesManager;
    KpiService kpiService;
    SubKpiService subKpiService;
    List<SubKpi> subKpiList;
    List<SubKpi> newSubKpiList;
    ListView subKpiListView;
    ListView newSubKpiListView;
    List<Department> departmentList;
    List<DepartmentKPI> departmentKPIList;
    Spinner departmentSpinner, subKpiSpinner;
    Button btnSave;

    public UpdateKpiFragment() {
        // Required empty public constructor
    }

    public UpdateKpiFragment(List<KPI> kpiList, KPI kpi) {
        this.kpi = kpi;
        this.kpiList = kpiList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle arguments if necessary
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_kpi, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        subKpiSpinner = view.findViewById(R.id.spinner_sub_kpi);
        subKpiListView = view.findViewById(R.id.list_view_subKpi);
        newSubKpiListView = view.findViewById(R.id.list_view_new_subKpi);
        btnSave = view.findViewById(R.id.btn_save_kpi);

        subKpiList = new ArrayList<>();
        newSubKpiList = new ArrayList<>();
        deletedSubKpis = new ArrayList<>();
        SubKpi subKpi = new SubKpi();
        subKpi.setName("Select SubKpi");
        subKpiList.add(subKpi);
        subKpiAdapterList = new ArrayList<>();

        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        int sessionId = sharedPreferencesManager.getSessionId();
        Log.d("Update Kpi Fragment", "Session ID: " + sessionId); // Debug log for session ID
        kpiService = new KpiService(getContext());
        subKpiService = new SubKpiService(getContext());
        DepartmentService departmentService = new DepartmentService(view.getContext());

        // Initialize the subKpiList and the adapters
        subKpiListAdapter = new SubKpiListAdapter(getContext(), R.layout.sub_kpi_list_item_view, subKpiAdapterList);
        subKpiListView.setAdapter(subKpiListAdapter);

        subKpiListAdapter.setOnSubKpiRemoveListener(s -> {
            // Move removed subKpi to deletedSubKpis list
            deletedSubKpis.add(s);
            subKpiListAdapter.notifyDataSetChanged();
        });

        newSubKpiListAdapter = new SubKpiListAdapter(getContext(), R.layout.sub_kpi_list_item_view, newSubKpiList);
        newSubKpiListView.setAdapter(newSubKpiListAdapter);

        newSubKpiListAdapter.setOnSubKpiRemoveListener(s -> {
            // Remove subKpi from newSubKpiList and add it back to subKpiList
            subKpiList.add(s);
            newSubKpiList.remove(s);
            subKpiService.populateSpinner(subKpiList, subKpiSpinner);
            newSubKpiListAdapter.notifyDataSetChanged();
            subKpiSpinner.setSelection(AdapterView.INVALID_POSITION);
            ignoreNextSelection = true;
        });

        subKpiService.getAvailableSubKpis(
                kpi.getId(),
                sharedPreferencesManager.getSessionId(),
                subKpiList1 -> {
                    if (subKpiList1 != null) {
                        subKpiList.addAll(subKpiList1);
//                        String subKpiTitles[] = subKpiService.getSubKpiTitles(subKpiList);
                        subKpiService.populateSpinner(subKpiList, subKpiSpinner);
//                        if (subKpiTitles != null) {
//                        } else {
//                            Toast.makeText(getContext(), "No SubKPI titles available", Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch SubKPIs", Toast.LENGTH_SHORT).show();
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

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
                    newSubKpiList.add(selectedSubKpi);
                    subKpiList.remove(position);
                    // Notify the adapters to refresh the Spinner and the ListView
                    subKpiService.populateSpinner(subKpiList, subKpiSpinner);
                    newSubKpiListAdapter.notifyDataSetChanged();

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

//        subKpiListView.setOnItemClickListener((parent, view1, position, id) -> {
//            SubKpi deletedSubKpi = subKpiAdapterList.get(position);
//            subKpiAdapterList.remove(position);
//            deletedSubKpis.add(deletedSubKpi);
//            // Notify the adapter to refresh the Spinner and the ListView
//            subKpiService.populateSpinner(subKpiList, subKpiSpinner);
//            subKpiListAdapter.notifyDataSetChanged();
//        });

        // Find your text boxes by their IDs
        EditText kpiNameEditText = view.findViewById(R.id.text_title);
        EditText kpiValueEditText = view.findViewById(R.id.text_weightage);

        kpiValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                int sum = 0;
                // btnSave.setEnabled(false);
                try {
                    sum = Integer.parseInt(kpiValueEditText.getText().toString());
                    // btnSave.setEnabled(true);
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "Weightage must be in numbers", Toast.LENGTH_SHORT).show();
                }
                if (sum > 100 || sum < 0) {
                    Toast.makeText(getContext(), "Weightage cannot be more than 100 or less than 0", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(false);
                } else {
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
                        // subKpiList = subKpiList1;
                        subKpiListAdapter.addAll(subKpiList1);
                        subKpiListAdapter.notifyDataSetChanged();
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), "Failed to fetch SubKPIs", Toast.LENGTH_SHORT).show();
                    }
            );

            // Pass these values to the KpiWeightageAdjustmentFormFragment
        }

//        newSubKpiListView.setOnItemClickListener((parent, view1, position, id) -> {
//            SubKpi subKpi1 = newSubKpiList.get(position);
//            newSubKpiList.remove(position);
//            subKpiList.add(subKpi1);
//            // Notify the adapter to refresh the Spinner and the ListView
//            subKpiService.populateSpinner(subKpiList, subKpiSpinner);
//            newSubKpiListAdapter.notifyDataSetChanged();
//        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KpiWithSubKpiWeightages kpiWithSubKpiWeightages = new KpiWithSubKpiWeightages();
                List<SubKpiWeightage> subKpiWeightages = new ArrayList<>();
                List<SubKpiWeightage> deletedSubKpiWeightages = new ArrayList<>();

                kpiList.remove(kpi);

                int weightageSum = Integer.parseInt(kpiValueEditText.getText().toString());
                // KPI newKpi = new KPI();
                kpi.setName(kpiNameEditText.getText().toString());
                // newKpi.setDepartment_id(department.getId());
                KpiWeightage newKpiWeightage = new KpiWeightage();
                newKpiWeightage.setWeightage(weightageSum);
                newKpiWeightage.setSession_id(sharedPreferencesManager.getSessionId());
                kpi.setKpiWeightage(newKpiWeightage);

                for (SubKpi s : subKpiAdapterList) {
                    s.getSubKpiWeightage().setSession_id(sharedPreferencesManager.getSessionId());
                    s.getSubKpiWeightage().setSub_kpi_id(s.getId());
                    s.getSubKpiWeightage().setKpi_id(kpi.getId());
                    subKpiWeightages.add(s.getSubKpiWeightage());
                }

                for (SubKpi s : newSubKpiList) {
                    s.getSubKpiWeightage().setSession_id(sharedPreferencesManager.getSessionId());
                    s.getSubKpiWeightage().setSub_kpi_id(s.getId());
                    s.getSubKpiWeightage().setKpi_id(kpi.getId());
                    subKpiWeightages.add(s.getSubKpiWeightage());
                }

                for (SubKpi s : deletedSubKpis) {
                    s.getSubKpiWeightage().setSession_id(sharedPreferencesManager.getSessionId());
                    s.getSubKpiWeightage().setSub_kpi_id(s.getId());
                    s.getSubKpiWeightage().setKpi_id(kpi.getId());
                    deletedSubKpiWeightages.add(s.getSubKpiWeightage());
                }

                // kpiWithSubKpiWeightages.setKpi(newKpi);
                // kpiWithSubKpiWeightages.setWeightage(newKpiWeightage);
                kpi.setKpiWeightage(newKpiWeightage);
                kpi.setSubKpiWeightages(subKpiWeightages);
                kpi.setDeletedSubKpis(deletedSubKpiWeightages);

                kpiList.add(kpi);

                navigateToWeightageAdjustmentForm(null);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        subKpiListView.setVisibility(View.VISIBLE);
                        newSubKpiListView.setVisibility(View.GONE);
                        break;
                    case 1:
                        subKpiListView.setVisibility(View.GONE);
                        newSubKpiListView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
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
