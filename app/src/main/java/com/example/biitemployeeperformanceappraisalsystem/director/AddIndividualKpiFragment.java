package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiWithWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.KpiService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIndividualKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIndividualKpiFragment extends Fragment {
    int sum = 0;
    int group_id = 0;
    KPI kpi;
    Employee employee;
    GroupKpiDetails groupKpiDetails;
    SharedPreferencesManager sharedPreferencesManager;
    List<KPI> kpiList;
    List<Employee> employeeList;
    EmployeeService employeeService;
    KpiService kpiService;
    Spinner employeeSpinner,kpiSpinner;
    Button btnSave;
    EditText editTextWeightage;

    public AddIndividualKpiFragment(){

    }

    public AddIndividualKpiFragment(GroupKpiDetails groupKpiDetails, KPI kpi){
        this.groupKpiDetails = groupKpiDetails;
        this.kpi = kpi;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_individual_kpi, container, false);
        employeeSpinner = view.findViewById(R.id.spinner_employee);
        kpiSpinner = view.findViewById(R.id.spinner_kpi);
        LinearLayout kpiLayout = view.findViewById(R.id.kpi_spinner_layout);
        LinearLayout employeeLayout = view.findViewById(R.id.employee_spinner_layout);
        btnSave = view.findViewById(R.id.btn_save_kpi);
        editTextWeightage = view.findViewById(R.id.text_weightage);

        kpiService = new KpiService(getContext());
        employeeService = new EmployeeService(getContext());
        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        kpiService.getKpis(
                kpis -> {
                    kpiList = kpis;
                    kpiService.populateSpinner(kpiList, kpiSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        employeeService.getEmployees(
                employeeList1 -> {
                    employeeList = employeeList1;
                    employeeService.populateEmployeesSpinner(employeeList, employeeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    employee = employeeList.get(position);
                }catch (Exception e){
                    Toast.makeText(getContext(), "fetching employee...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kpiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    kpi = kpiList.get(position);
                }catch (Exception e){
                    Toast.makeText(getContext(), "fetching kpi's...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (kpi != null) {
            float kpiValue = kpi.getKpiWeightage().getWeightage();
            editTextWeightage.setText(String.valueOf(kpiValue));

            employeeLayout.setVisibility(View.GONE);
            kpiLayout.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Float> pieChartValues = new ArrayList<>();
                ArrayList<String> pieChartTitles = new ArrayList<>();

                EmployeeKpi employeeKpi = new EmployeeKpi();
                employeeKpi.setKpi(kpi);
                employeeKpi.setEmployee(employee);
                int weightage = 0;
                try {
                    weightage = Integer.parseInt(editTextWeightage.getText().toString());
                }catch (Exception e){
                    Toast.makeText(getContext(), "Try entering correct weightage value in numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
                employeeKpi.setWeightage(weightage);
                employeeKpi.setSession_id(sharedPreferencesManager.getSessionId());

                kpiService.getGroupKpiId(
                        employee != null ? employee.getDepartmentId() : 0,
                        employee != null ? employee.getDesignationId() : 0,
                        employee != null ? employee.getEmployeeTypeId() : 0,
                        employee != null ? employee.getId() : 0,
                        id -> {
                            // group_id = id;
                            try {
                                group_id = id;
                                if (kpi == null){
                                    // TODO
                                    kpiService.getGroupKpi(
                                            group_id,
                                            sharedPreferencesManager.getSessionId(),
                                            kpis -> {
                                                int kpiWeightage = Integer.parseInt(editTextWeightage.getText().toString());
                                                int weightageSum = Integer.parseInt(editTextWeightage.getText().toString());
                                                KPI newKpi = new KPI();
                                                // newKpi.setName(kpiNameEditText.getText().toString());
                                                KpiWeightage newKpiWeightage = new KpiWeightage();
                                                newKpiWeightage.setWeightage(weightageSum);
                                                newKpiWeightage.setSession_id(sharedPreferencesManager.getSessionId());
                                                newKpi.setKpiWeightage(newKpiWeightage);
                                                kpis.add(newKpi);
                                                groupKpiDetails = new GroupKpiDetails();
                                                groupKpiDetails.setKpiList(kpis);
                                                // dont include the last item weightage because already got it from input box
                                                for (int i = 0 ; i < groupKpiDetails.getKpiList().size() - 1; i++){
                                                    weightageSum += groupKpiDetails.getKpiList().get(i).getKpiWeightage().getWeightage();
                                                    groupKpiDetails.getKpiList().get(i).setGroup_kpi_id(group_id);
                                                }


                                                EmployeeKpi employeeKpi1 = new EmployeeKpi();
                                                employeeKpi1.setEmployee(employee);
                                                employeeKpi1.setKpi(newKpi);
                                                employeeKpi1.setWeightage(kpiWeightage);
                                                employeeKpi1.setSession_id(sharedPreferencesManager.getSessionId());
                                                employeeKpi1.setGroup_kpi_id(group_id);

                                                kpis.add(newKpi);

                                                if (group_id < 1){
                                                    kpiService.putEmployeeKpi(
                                                            kpis,
                                                            kpi1 -> {
                                                                Toast.makeText(getContext(), "Employee Kpi updated Successfully", Toast.LENGTH_SHORT).show();
                                                            },
                                                            errorMessage -> {
                                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                                            });
                                                }
                                                else if (weightageSum > 100){
                                                    navigateToWeightageAdjustmentForm(employeeKpi1);
                                                }else {
                                                    // kpiWithSubKpiWeightages.getSubKpiWeightages().add();
                                                    kpiService.postEmployeeKpi(
                                                            employeeKpi1,
                                                            kpi1 -> {
                                                                Toast.makeText(getContext(), "Employee Kpi Added Successfully", Toast.LENGTH_SHORT).show();
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

                                    KPI kpi1 = new KPI();
                                    kpi1.setGroup_kpi_id(group_id);
                                    // kpi1.setName(kpiNameEditText.getText().toString());
                                    KpiWeightage kpiWeightage = new KpiWeightage();
                                    kpiWeightage.setWeightage(Integer.parseInt(editTextWeightage.getText().toString()));
                                    kpi1.setKpiWeightage(kpiWeightage);

                                    for (int i = 0 ; i < groupKpiDetails.getKpiList().size(); i++){
                                        if (kpi.getId() == groupKpiDetails.getKpiList().get(i).getId()){
                                            // KpiWeightage kpiWeightage1 = groupKpiDetails.getKpiList().get(i).getKpiWeightage();
                                            kpiWeightage.setWeightage(Integer.parseInt(editTextWeightage.getText().toString()));
                                            groupKpiDetails.getKpiList().get(i).getKpiWeightage().setWeightage(Integer.parseInt(editTextWeightage.getText().toString()));
                                            // groupKpiDetails.getKpiList().get(i).setName(kpiNameEditText.getText().toString());
                                        }else {
                                            sum += groupKpiDetails.getKpiList().get(i).getKpiWeightage().getWeightage();
                                        }
                                        groupKpiDetails.getKpiList().get(i).setGroup_kpi_id(group_id);
                                    }

                                    for (KPI item: groupKpiDetails.getKpiList()) {
                                        pieChartTitles.add(item.getName());
                                        pieChartValues.add((float)item.getKpiWeightage().getWeightage());
                                    }
                                    // Navigate to the KpiWeightageAdjustmentFormFragment and pass values
                                    if (sum > 100){
                                        navigateToWeightageAdjustmentForm(employeeKpi);
                                    }else {
                                        kpiService.putEmployeeKpi(
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
                                Log.e("Error", "Adding Employee Kpi", ex);
                            }
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );

            }
        });

        return view;
    }

    public void navigateToWeightageAdjustmentForm(EmployeeKpi employeeKpi) {
        KpiWeightageAdjustmentFormFragment fragment = new KpiWeightageAdjustmentFormFragment(groupKpiDetails, employeeKpi);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}