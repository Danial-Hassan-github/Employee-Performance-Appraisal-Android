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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiWithWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWithSubKpiWeightages;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DepartmentService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DesignationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.KpiService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SubKpiService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGroupKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGroupKpiFragment extends Fragment {
    int group_kpi_id = 0;
    int sum = 0;
    GroupKpiDetails groupKpiDetails;
    Department department;
    Designation designation;
    EmployeeType employeeType;
    KPI kpi;
    SharedPreferencesManager sharedPreferencesManager;
    KpiService kpiService;
    SubKpiService subKpiService;
    List<SubKpi> subKpiList;
    List<SubKpi> subKpiAdapterList;
    SubKpiListAdapter subKpiListAdapter;
    ListView subKpiListView;
    // List<Session> sessionList;
    List<Designation> designationList;
    List<Department> departmentList;
    List<EmployeeType> employeeTypeList;
    Spinner designationSpinner,departmentSpinner,employeeTypeSpinner,subKpiSpinner;
    Button btnSave;

    public AddGroupKpiFragment(){

    }
    public AddGroupKpiFragment(GroupKpiDetails details, KPI selectedKpi) {
        this.groupKpiDetails = details;
        this.kpi = selectedKpi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_group_kpi, container, false);
        // sessionSpinner = view.findViewById(R.id.spinner_session);
        designationSpinner=view.findViewById(R.id.spinner_designation);
        departmentSpinner=view.findViewById(R.id.spinner_department);
        employeeTypeSpinner=view.findViewById(R.id.spinner_employee_type);
        subKpiSpinner = view.findViewById(R.id.spinner_sub_kpi);
        subKpiListView = view.findViewById(R.id.list_view_subKpi);
        btnSave = view.findViewById(R.id.btn_save_kpi);
        LinearLayout depLayout = view.findViewById(R.id.department_layout);
        LinearLayout desgLayout = view.findViewById(R.id.designation_layout);
        LinearLayout empTypeLayout = view.findViewById(R.id.employee_type_layout);

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        kpiService = new KpiService(getContext());

        DesignationService designationService=new DesignationService(view.getContext());
        DepartmentService departmentService=new DepartmentService(view.getContext());
        SessionService sessionService=new SessionService(getContext());
        EmployeeService employeeService=new EmployeeService(getContext());
        subKpiService = new SubKpiService(getContext());

        subKpiList = new ArrayList<>();
        subKpiAdapterList = new ArrayList<>();

        subKpiService.getSubKPIs(
                sharedPreferencesManager.getSessionId(),
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

        // Initialize the subKpiList and the adapter
        subKpiListAdapter = new SubKpiListAdapter(getContext(), R.layout.sub_kpi_list_item_view, subKpiAdapterList);
        subKpiListView.setAdapter(subKpiListAdapter);

        // Set the spinner item selected listener
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

        designationService.getDesignations(
                designations -> {
                    designationList=designations;
                    designationService.populateDesignationSpinner(designations,designationSpinner);
                },errorMessage ->{
                    Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
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

        employeeService.getEmployeeTypes(
                employeeTypes -> {
                    employeeTypeList = employeeTypes;
                    employeeService.populateEmployeeTypeSpinner(employeeTypes,employeeTypeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                }
        );

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = departmentList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        designationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = designationList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        employeeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employeeType = employeeTypeList.get(position);
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

        if (kpi != null) {
            String kpiName = kpi.getName();
            float kpiValue = kpi.getKpiWeightage().getWeightage();

            depLayout.setVisibility(View.GONE);
            desgLayout.setVisibility(View.GONE);
            empTypeLayout.setVisibility(View.GONE);

            department = groupKpiDetails.getGroupKpi().getDepartment();
            designation = groupKpiDetails.getGroupKpi().getDesignation();
            employeeType = groupKpiDetails.getGroupKpi().getEmployeeType();

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
                try {
                    sum = Integer.parseInt(kpiValueEditText.getText().toString());
                }catch (Exception ex){
                    Toast.makeText(getContext(), "Weightage must be in numbers", Toast.LENGTH_SHORT).show();
                }
                ArrayList<Float> pieChartValues = new ArrayList<>();
                ArrayList<String> pieChartTitles = new ArrayList<>();

                KpiWithSubKpiWeightages kpiWithSubKpiWeightages = new KpiWithSubKpiWeightages();
                List<SubKpiWeightage> subKpiWeightages = new ArrayList<>();

                kpiService.getGroupKpiId(
                        department.getId(),
                        designation.getId(),
                        employeeType.getId(),
                        0,
                        k -> {
                            try {
                                group_kpi_id = k;
                                if (kpi == null){
                                    // TODO
                                    kpiService.getGroupKpi(
                                            group_kpi_id,
                                            sharedPreferencesManager.getSessionId(),
                                            kpis -> {
                                                int kpiWeightage = Integer.parseInt(kpiValueEditText.getText().toString());
                                                int weightageSum = Integer.parseInt(kpiValueEditText.getText().toString());
                                                KPI newKpi = new KPI();
                                                newKpi.setName(kpiNameEditText.getText().toString());
                                                KpiWeightage newKpiWeightage = new KpiWeightage();
                                                newKpiWeightage.setWeightage(weightageSum);
                                                newKpiWeightage.setSession_id(sharedPreferencesManager.getSessionId());
                                                newKpi.setKpiWeightage(newKpiWeightage);
                                                kpis.add(newKpi);
                                                groupKpiDetails = new GroupKpiDetails();
                                                groupKpiDetails.setKpiList(kpis);
                                                // dont include the last item weightage because already got it from input box
                                                for (int i = 0 ; i < groupKpiDetails.getKpiList().size() - 1; i++){
//                                        if (kpi.getId() == groupKpiDetails.getKpiList().get(i).getId()){
//                                            KpiWeightage kpiWeightage = groupKpiDetails.getKpiList().get(i).getKpiWeightage();
//                                            kpiWeightage.setWeightage(Integer.parseInt(kpiValueEditText.getText().toString()));
//                                            groupKpiDetails.getKpiList().get(i).setKpiWeightage(kpiWeightage);
//                                        }else {
                                                    weightageSum += groupKpiDetails.getKpiList().get(i).getKpiWeightage().getWeightage();
                                                    groupKpiDetails.getKpiList().get(i).setGroup_kpi_id(group_kpi_id);
                                                    // }
                                                }

                                                for (SubKpi s:subKpiAdapterList) {
                                                    //SubKpiWeightage subKpiWeightage = new SubKpiWeightage();
                                                    s.getSubKpiWeightage().setSession_id(sharedPreferencesManager.getSessionId());
                                                    s.getSubKpiWeightage().setSub_kpi_id(s.getId());
                                                    // s.setSubKpiWeightage(subKpiWeightage);
                                                    subKpiWeightages.add(s.getSubKpiWeightage());
                                                }

                                                GroupKpiWithWeightage groupKpiWithWeightage = new GroupKpiWithWeightage();
                                                groupKpiWithWeightage.setKpi(newKpi);
                                                groupKpiWithWeightage.setSession_id(sharedPreferencesManager.getSessionId());
                                                groupKpiWithWeightage.setWeightage(kpiWeightage);
                                                groupKpiWithWeightage.setDepartment_id(department.getId());
                                                groupKpiWithWeightage.setDesignation_id(designation.getId());
                                                groupKpiWithWeightage.setEmployee_type_id(employeeType.getId());
                                                groupKpiWithWeightage.setSubKpiWeightages(subKpiWeightages);

                                                kpiWithSubKpiWeightages.setKpi(newKpi);
                                                kpiWithSubKpiWeightages.setWeightage(newKpiWeightage);
                                                kpiWithSubKpiWeightages.setSubKpiWeightages(subKpiWeightages);

                                                kpis.get(kpis.size() - 1).setSubKpiWeightages(subKpiWeightages);

                                                if (group_kpi_id < 1){
                                                    kpiService.postGroupKpi(
                                                            groupKpiWithWeightage,
                                                            kpi1 -> {
                                                                Toast.makeText(getContext(), "Group Kpi Added Successfully", Toast.LENGTH_SHORT).show();
                                                            },
                                                            errorMessage -> {
                                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                                            });
                                                }
                                                else if (weightageSum > 100){
                                                    navigateToWeightageAdjustmentForm(groupKpiWithWeightage);
                                                }else {
                                                    // kpiWithSubKpiWeightages.getSubKpiWeightages().add();
                                                    kpiService.postGroupKpi(
                                                            groupKpiWithWeightage,
                                                            kpi1 -> {
                                                                Toast.makeText(getContext(), "Group Kpi Added Successfully", Toast.LENGTH_SHORT).show();
                                                            },
                                                            errorMessage -> {
                                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                                            });
                                                }
                                            },
                                            errorMessage -> {
                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                            }
                                    );
                                    // Toast.makeText(getContext(), sum+"", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    // Set the values in your text boxes
                                    kpiNameEditText.getText();
                                    kpiValueEditText.getText();

                                    KPI kpi1 = new KPI();
                                    kpi1.setGroup_kpi_id(group_kpi_id);
                                    kpi1.setName(kpiNameEditText.getText().toString());
                                    KpiWeightage kpiWeightage = new KpiWeightage();
                                    kpiWeightage.setWeightage(Integer.parseInt(kpiValueEditText.getText().toString()));
                                    kpi1.setKpiWeightage(kpiWeightage);

                                    GroupKpiWithWeightage groupKpiWithWeightage = new GroupKpiWithWeightage();
                                    groupKpiWithWeightage.setSession_id(sharedPreferencesManager.getSessionId());
                                    groupKpiWithWeightage.setKpi(kpi1);
                                    groupKpiWithWeightage.setSession_id(sharedPreferencesManager.getSessionId());
                                    groupKpiWithWeightage.setWeightage(Integer.parseInt(kpiValueEditText.getText().toString()));
                                    groupKpiWithWeightage.setDepartment_id(department.getId());
                                    groupKpiWithWeightage.setDesignation_id(designation.getId());
                                    groupKpiWithWeightage.setEmployee_type_id(employeeType.getId());
                                    groupKpiWithWeightage.setSubKpiWeightages(subKpiWeightages);

                                    for (int i = 0 ; i < groupKpiDetails.getKpiList().size(); i++){
                                        if (kpi.getId() == groupKpiDetails.getKpiList().get(i).getId()){
                                            // KpiWeightage kpiWeightage1 = groupKpiDetails.getKpiList().get(i).getKpiWeightage();
                                            kpiWeightage.setWeightage(Integer.parseInt(kpiValueEditText.getText().toString()));
                                            groupKpiDetails.getKpiList().get(i).getKpiWeightage().setWeightage(Integer.parseInt(kpiValueEditText.getText().toString()));
                                            groupKpiDetails.getKpiList().get(i).setName(kpiNameEditText.getText().toString());
                                        }else {
                                            sum += groupKpiDetails.getKpiList().get(i).getKpiWeightage().getWeightage();
                                        }
                                        groupKpiDetails.getKpiList().get(i).setGroup_kpi_id(group_kpi_id);
                                    }

                                    for (KPI item: groupKpiDetails.getKpiList()) {
                                        pieChartTitles.add(item.getName());
                                        pieChartValues.add((float)item.getKpiWeightage().getWeightage());
                                    }
                                    // Navigate to the KpiWeightageAdjustmentFormFragment and pass values
                                    if (sum > 100){
                                        navigateToWeightageAdjustmentForm(groupKpiWithWeightage);
                                    }else {
                                        kpiService.putGroupKpi(
                                                groupKpiDetails.getKpiList(),
                                                kpi2 -> {
                                                    Toast.makeText(getContext(), kpi2, Toast.LENGTH_SHORT).show();
                                                },
                                                errorMessage -> {
                                                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                                });

                                    }
                                }
                            }catch (Exception ex){
                                Log.e("Error", "Adding Group Kpi", ex);
                            }

                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                        );
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void navigateToWeightageAdjustmentForm(GroupKpiWithWeightage groupKpiWithWeightage) {
        KpiWeightageAdjustmentFormFragment fragment = new KpiWeightageAdjustmentFormFragment(groupKpiDetails, groupKpiWithWeightage);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}