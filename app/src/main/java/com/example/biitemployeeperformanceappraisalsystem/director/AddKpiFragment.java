package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.biitemployeeperformanceappraisalsystem.models.Designation;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddKpiFragment extends Fragment {
    Department department;
    KPI kpi;
    SharedPreferencesManager sharedPreferencesManager;
    KpiService kpiService;
    SubKpiService subKpiService;
    List<SubKpi> subKpiList;
    List<SubKpi> subKpiAdapterList;
    SubKpiListAdapter subKpiListAdapter;
    ListView subKpiListView;
    List<Department> departmentList;
    Spinner departmentSpinner,subKpiSpinner;
    Button btnSave;
    public AddKpiFragment() {
        // Required empty public constructor
    }

    public static AddKpiFragment newInstance(String param1, String param2) {
        AddKpiFragment fragment = new AddKpiFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_kpi, container, false);

        departmentSpinner=view.findViewById(R.id.spinner_department);
        subKpiSpinner = view.findViewById(R.id.spinner_sub_kpi);
        subKpiListView = view.findViewById(R.id.list_view_subKpi);
        btnSave = view.findViewById(R.id.btn_save_kpi);

        subKpiList = new ArrayList<>();
        subKpiAdapterList = new ArrayList<>();

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        kpiService = new KpiService(getContext());
        subKpiService = new SubKpiService(getContext());
        DepartmentService departmentService=new DepartmentService(view.getContext());

        // Initialize the subKpiList and the adapter
        subKpiListAdapter = new SubKpiListAdapter(getContext(), R.layout.sub_kpi_list_item_view, subKpiAdapterList);
        subKpiListView.setAdapter(subKpiListAdapter);

        subKpiService.getSubKPIs(
                10,
                subKpiList1 -> {
                    if (subKpiList1 != null) {
                        subKpiList = subKpiList1;
                        String subKpiTitles[] = subKpiService.getSubKpiTitles(subKpiList);
                        if (subKpiTitles != null) {
                            subKpiService.populateSpinner(subKpiList,subKpiSpinner);
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

        departmentService.getDepartments(
                departments -> {
                    departmentList=departments;
                    departmentService.populateDepartmentSpinner(departments,departmentSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                }
        );

        subKpiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    subKpiAdapterList.add(subKpiList.get(position));
                    // Notify the adapter to refresh the ListView
                    subKpiListAdapter.notifyDataSetChanged();
                }catch (Exception ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = departmentList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                kpiService.postKpi(
                        kpiWithSubKpiWeightages,
                        kpi1 -> {
                            Toast.makeText(getContext(), "Kpi Added Successfully", Toast.LENGTH_SHORT).show();
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return view;
    }



//    private void replaceFragment(int position) {
//        Fragment fragment = null;
//        switch (position) {
//            case 0:
//                fragment = new AddGeneralKpiFragment();
//                break;
//            case 1:
//                fragment = new AddGroupKpiFragment();
//                break;
//            case 2:
//                fragment = new AddIndividualKpiFragment();
//                break;
//            // Add more cases for additional tabs
//        }
//        if (fragment != null) {
//            getChildFragmentManager().beginTransaction()
//                    .replace(R.id.child_fragment_container, fragment)
//                    .commit();
//        }
//    }
}